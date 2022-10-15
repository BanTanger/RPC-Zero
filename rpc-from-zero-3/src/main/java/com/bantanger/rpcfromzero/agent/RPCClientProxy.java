package com.bantanger.rpcfromzero.agent;

import com.bantanger.rpcfromzero.client.RPCClient;
import com.bantanger.rpcfromzero.common.RPCRequest;
import com.bantanger.rpcfromzero.common.RPCResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/10 19:56
 */

@AllArgsConstructor
public class RPCClientProxy implements InvocationHandler {
    // 传入参数Service接口的class对象，反射封装成一个request
//    private String host;
//    private int port;

    // 只需要传入客户端类变量即可，传入不同的client(simple,netty), 即可调用公共的接口
    private RPCClient client;

    // jdk 动态代理，每次代理对象调用方法时，都会经过此方法进行增强
    // (反射获取request对象， socket发送至客户端)
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // request的构建，使用lombok的builder简化代码
        RPCRequest request = RPCRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName()).params(args)
                .paramsTypes(method.getParameterTypes()).build();
        // 数据传输
        RPCResponse response =
//                IOClient.sendRequest(host, port, request); // 调用底层的IOClient
                client.sendRequest(request); // 调用客户端的sendRequest 发送请求

        return response.getData(); // 返回方法调用后的结果
    }
    public <T> T getProxy(Class<T> clazz) {
        // 获取动态代理实例
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }
}

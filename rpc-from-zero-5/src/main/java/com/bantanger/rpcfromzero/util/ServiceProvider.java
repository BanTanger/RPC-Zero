package com.bantanger.rpcfromzero.util;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 服务端接口暴露类
 * @Date 2022/10/11 22:41
 */

import com.bantanger.rpcfromzero.register.ServiceRegister;
import com.bantanger.rpcfromzero.register.ZkServiceRegister;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 利用class对象自动得到服务接口名
 * 之前这里使用Map简单实现的
 * 存放服务接口名与服务端对应的实现类
 * 服务启动时要暴露其相关的实现类
 * 根据request中的interface调用服务端中相关实现类
 */
public class ServiceProvider {
    /**
     * 一个实现类可能实现多个接口
     */
    private Map<String, Object> interfaceProvider;

    private String host;
    private Integer port;
    private ServiceRegister serviceRegister;

    public ServiceProvider() {
        this.interfaceProvider = new ConcurrentHashMap<>();
    }

    public ServiceProvider(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.interfaceProvider = new ConcurrentHashMap<>();
        this.serviceRegister = new ZkServiceRegister();
    }

    public void provideServiceInterface(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces(); // 直接得到这个类下的所有接口

        for (Class<?> aClass : interfaces) {
            // 本机的映射
            interfaceProvider.put(aClass.getName(), service);
            // 在注册中心注册服务
            serviceRegister.register(aClass.getName(), new InetSocketAddress(host, port));
        }
    }

    public Object getService(String interfaceName) {
        return interfaceProvider.get(interfaceName);
    }
}

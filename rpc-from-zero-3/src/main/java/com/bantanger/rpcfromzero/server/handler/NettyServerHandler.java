package com.bantanger.rpcfromzero.server.handler;

import com.bantanger.rpcfromzero.common.RPCRequest;
import com.bantanger.rpcfromzero.common.RPCResponse;
import com.bantanger.rpcfromzero.util.ServiceProvider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/16 19:43
 */

@Slf4j
@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<RPCRequest> {

    private ServiceProvider serviceProvider;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest msg) throws Exception {
        log.debug("RPCRequest: {}", msg);
        RPCResponse response = getResponse(msg);
        ctx.writeAndFlush(response); // 将返回数据写入客户端
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    RPCResponse getResponse(RPCRequest request) {
        // 获取服务端接口名称
        String interfaceName = request.getInterfaceName();
        // 通过动态代理，只使用服务端的接口名称来调用具体接口方法（实际上是使用动态代理来执行这个方法）
        Object service = serviceProvider.getService(interfaceName);
        // 反射调用方法
        Method method = null;
        try {
            method = service.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
            Object invoke = method.invoke(service, request.getParams());
            return RPCResponse.success(invoke);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            log.debug("方法执行错误");
            return RPCResponse.fail();
        }
    }
}

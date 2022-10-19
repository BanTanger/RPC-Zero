package com.bantanger.rpcfromzero.client.impl;

import com.bantanger.rpcfromzero.client.RPCClient;
import com.bantanger.rpcfromzero.client.initializer.NettyClientInitializer;
import com.bantanger.rpcfromzero.common.RPCRequest;
import com.bantanger.rpcfromzero.common.RPCResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/16 19:28
 */

@Slf4j
public class NettyRPCClient implements RPCClient {

    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;
    private String host;
    private Integer port;

    public NettyRPCClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    // netty客户端初始化，重复使用
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    @Override
    public RPCResponse sendRequest(RPCRequest request) {
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            // 发送数据
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 实际上不应通过阻塞，可通过回调函数
            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
            RPCResponse response = channel.attr(key).get();

            log.info("{}", response);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

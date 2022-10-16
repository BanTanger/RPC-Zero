package com.bantanger.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/16 9:44
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException, IOException {
        // 1. 启动器类， 启动客户端
        new Bootstrap()
                .group(new NioEventLoopGroup())
                // 2. 选择客户端 Socket 实现类， NioSocketChannel 表示基于NIO的客户端实现
                .channel(NioSocketChannel.class)
                // 3. ChannelInitializer 处理器（仅执行一次）
                // 它的作用是待客户端SocketChannel建立连接后，
                // 执行initChannel以便添加更多的处理器
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // 消息会经过通道 handler 处理，这里是将 String => ByteBuf 编码发出
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 指定要连接的服务器和端口
                .connect(new InetSocketAddress("localhost", 8080))
                // Netty 中很多方法都是异步的，例如 connect
                // 需要使用 sync 方法等待 connect 建立连接完毕
                .sync()
                // 获取 Channel 对象，为通道抽象，可以进行数据读写操作
                .channel()
                // 写入消息并清空缓冲区
                .writeAndFlush("hello, world");
        System.in.read();
    }
}

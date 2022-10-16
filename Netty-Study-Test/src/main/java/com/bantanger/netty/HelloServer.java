package com.bantanger.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/16 9:44
 */
public class HelloServer {

    private static final Logger logger = LoggerFactory.getLogger(HelloServer.class);

    public static void main(String[] args) {
        // 1. 启动器类， 负责装配netty组件，启动服务器
        new ServerBootstrap()
                // 2. 创建 NioEventLoopGroup，可以简单理解为 线程池 + Selector
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                // 3. 定制 Channel通道的类型为 Nio
                .channel(NioServerSocketChannel.class)
                // 4. child(worker线程) 负责处理读写，该方法明确了 child线程 执行那些操作
                // ChannelInitializer 处理器(只执行一次)
                // 它的作用是待客户端 SocketChannel 建立连接后，执行 initChannel 以便添加更多的处理器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 5. SocketChannel 的处理器，使用 StringDecoder 解码， ByteBuf => String
                        ch.pipeline().addLast(new StringDecoder());
                        // 6. SocketChannel 的业务处理，使用上一个处理器的处理结果
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(
                                    ChannelHandlerContext channelHandlerContext,
                                    String s) throws Exception {
                                logger.info("{}", s);
                            }
                        });
                    }
                    // 7. ServerSocketChannel绑定8080端口
                }).bind(8080);
        logger.info("main");
    }
}

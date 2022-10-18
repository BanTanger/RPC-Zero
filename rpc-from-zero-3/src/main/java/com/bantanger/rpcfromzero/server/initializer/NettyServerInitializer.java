package com.bantanger.rpcfromzero.server.initializer;

import com.bantanger.rpcfromzero.server.handler.NettyServerHandler;
import com.bantanger.rpcfromzero.util.ServiceProvider;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.AllArgsConstructor;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 预处理工具类: 主要负责序列化的编码解码， 需要解决netty的粘包问题
 * @Date 2022/10/16 19:35
 */

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 消息格式 [长度][消息体], 解决粘包半包
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        // 计算当前待发送信息的长度, 写入到前4个字节中
        pipeline.addLast(new LengthFieldPrepender(4)); // LengthFieldPrepender可获得消息的二进制长度

        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
            @Override
            public Class<?> resolve(String className) throws ClassNotFoundException {
                return Class.forName(className);
            }
        }));

        pipeline.addLast(new NettyServerHandler(serviceProvider));
    }
}

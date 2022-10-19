package com.bantanger.rpcfromzero.server.initializer;

import com.bantanger.rpcfromzero.codec.impl.JsonSerializer;
import com.bantanger.rpcfromzero.codec.mycoder.MyDecode;
import com.bantanger.rpcfromzero.codec.mycoder.MyEncode;
import com.bantanger.rpcfromzero.server.handler.NettyServerHandler;
import com.bantanger.rpcfromzero.util.ServiceProvider;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
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
        // 使用自定义的编解码器
        pipeline.addLast(new MyDecode());
        // 编码需要传入序列化器，这里是json，还支持ObjectSerializer，也可以自己实现其他的
        pipeline.addLast(new MyEncode(new JsonSerializer()));
        pipeline.addLast(new NettyServerHandler(serviceProvider));
    }
}

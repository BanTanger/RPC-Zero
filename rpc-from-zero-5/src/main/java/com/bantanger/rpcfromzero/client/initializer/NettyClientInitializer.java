package com.bantanger.rpcfromzero.client.initializer;

import com.bantanger.rpcfromzero.client.handler.NettyClientHandler;
import com.bantanger.rpcfromzero.codec.impl.JsonSerializer;
import com.bantanger.rpcfromzero.codec.mycoder.MyDecode;
import com.bantanger.rpcfromzero.codec.mycoder.MyEncode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/16 20:09
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 使用自定义的编解码器
        pipeline.addLast(new MyDecode());
        // 编码需要传入序列化器，这里是json，还支持ObjectSerializer，也可以自己实现其他的
        pipeline.addLast(new MyEncode(new JsonSerializer()));
        pipeline.addLast(new NettyClientHandler());
    }

}

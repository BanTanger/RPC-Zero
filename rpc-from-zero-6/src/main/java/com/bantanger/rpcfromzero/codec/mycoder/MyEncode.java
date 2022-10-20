package com.bantanger.rpcfromzero.codec.mycoder;

import com.bantanger.rpcfromzero.codec.Serializer;
import com.bantanger.rpcfromzero.common.RPCRequest;
import com.bantanger.rpcfromzero.common.RPCResponse;
import com.bantanger.rpcfromzero.util.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 自定义解码器
 * @Date 2022/10/18 20:54
 */

/**
 * 依次按照自定义的消息格式写入，传入的数据为request或者response
 * 需要持有一个serialize器，负责将传入的对象序列化成字节数组
 */

@AllArgsConstructor
public class MyEncode extends MessageToByteEncoder {

    private Serializer serializer;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 写入消息类型, 后续解码就通过读取类型来分辨是请求消息还是返回消息
        if(msg instanceof RPCRequest) {
            out.writeShort(MessageType.REQUEST.getCode());
        } else if(msg instanceof RPCResponse) {
            out.writeShort(MessageType.RESPONSE.getCode());
        }
        // 写入序列化方式
        out.writeShort(serializer.getType());
        // 得到序列化数组
        byte[] serialize = serializer.serialize(msg);
        // 写入长度
        out.writeInt(serialize.length);
        // 写入序列化具体数据
        out.writeBytes(serialize);
    }

}

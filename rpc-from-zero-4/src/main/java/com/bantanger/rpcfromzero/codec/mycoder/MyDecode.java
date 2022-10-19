package com.bantanger.rpcfromzero.codec.mycoder;

import com.bantanger.rpcfromzero.codec.Serializer;
import com.bantanger.rpcfromzero.util.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/18 20:54
 */

/**
 * 按照自定义的消息格式解码数据
 */

@Slf4j
@AllArgsConstructor
public class MyDecode extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1. 读取消息类型
        short messageType = in.readShort();
        // 现在只支持request与response
        if (messageType != MessageType.REQUEST.getCode() &&
                messageType != MessageType.RESPONSE.getCode()) {
            log.info("暂不支持此种数据类型 {}", messageType);
            return ;
        }
        // 2. 读取序列化类型
        short serializerType = in.readShort();
        // 根据类型得到对应的序列化器
        Serializer serializer = Serializer.getSerializer(serializerType);
        if(serializer == null) throw new RuntimeException("不存在对应类型的序列化器");
        // 3. 读取数据序列化后的字节长度
        int length = in.readInt();
        // 4. 读取序列化数组
        byte[] bytes = new byte[length]; // 创建空byte数组
        in.readBytes(bytes); // 空 byte 数组接收 ByteBuf 的 Byte
        Object deserialize = serializer.deserialize(bytes, messageType);
        out.add(deserialize);
    }

}

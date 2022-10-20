package com.bantanger.rpcfromzero.codec;

import com.bantanger.rpcfromzero.codec.impl.JsonSerializer;
import com.bantanger.rpcfromzero.codec.impl.ObjectSerializer;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/18 20:09
 */

public interface Serializer {
    /**
     * 将对象序列化成字节数组
     * @param obj 具体类型的对象
     * @return 序列化后的字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 将字节数组反序列化成对象消息
     *
     * 使用 java 自带序列化方式不用 messageType 也能得到相应的对象（序列化字节数组里包含类信息）
     * 其它方式需指定消息格式，再根据 message 转化成相应的对象
     * @param bytes 字节数组
     * @param messageType 指定的消息转换格式
     * @return 反序列化后的具体对象(需转型)
     */
    Object deserialize(byte[] bytes, int messageType);

    /**
     * 返回使用的序列器，是哪个
     * @return 0：java自带序列化方式, 1: json序列化方式
     */
    int getType();

    /**
     * 根据 getType() 进而选择不同的序列化方式
     * @param code 0：java自带序列化方式, 1: json序列化方式
     * @return 具体的序列化实现类
     */
    static Serializer getSerializer(int code) {
        switch (code) {
            case 0:
                return new ObjectSerializer();
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}

package com.bantanger.rpcfromzero.codec.impl;

import com.bantanger.rpcfromzero.codec.Serializer;

import java.io.*;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/18 20:14
 */
public class ObjectSerializer implements Serializer {

    // JDK版的序列化转化手段，OutputStream
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = null;
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
        ) {
            oos.writeObject(obj); // 序列化
            oos.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    // JDK版的反序列化转化手段，InputStream
    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis)
        ) {
            obj = ois.readObject();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    // 0 表示 Java原生序列化器
    @Override
    public int getType() {
        return 0;
    }
}

package com.bantanger.rpcfromzero.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 底层的通讯方法(抽象)
 * 客户端根据不同的Service进行动态代理：
 * 代理对象增强的公共行为：把不同的Service方法封装成统一的Request对象格式，并且建立与Server的通信
 * @Date 2022/10/10 19:45
 */


public class IOClient {
    // 这里负责底层与服务端的通信，发送的Request，接受的是Response对象
    // 客户端发起一次请求调用，Socket建立连接，发起请求Request，得到响应Response
    // 这里的request是封装好的（上层进行封装），不同的service需要进行不同的封装，
    // 客户端只知道Service接口，需要一层动态代理根据反射封装不同的Service

    private static final Logger logger = LoggerFactory.getLogger(IOClient.class);

    public static RPCResponse sendRequest(String host, Integer port, RPCRequest request) {
        try {
            Socket socket = new Socket(host, port);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 序列化请求传输给服务端
            logger.info("客户端发送请求 {}", request);
            oos.writeObject(request);
            oos.flush();
            // 反序列化服务端的返回数据
            RPCResponse response = (RPCResponse) ois.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println();
            return null;
        }
    }
}

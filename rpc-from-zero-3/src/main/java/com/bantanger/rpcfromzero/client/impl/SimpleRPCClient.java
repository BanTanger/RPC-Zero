package com.bantanger.rpcfromzero.client.impl;

import com.bantanger.rpcfromzero.client.RPCClient;
import com.bantanger.rpcfromzero.common.RPCRequest;
import com.bantanger.rpcfromzero.common.RPCResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/12 23:00
 */

// SimpleRPCClient 实现这个接口，不同的网络方式有着不同的实现
@AllArgsConstructor
public class SimpleRPCClient implements RPCClient {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRPCClient.class);

    private String host;
    private int port;

    // 客户端发起一次请求调用，Socket建立连接，发起请求Request，得到响应Response
    // 这里的request是封装好的，不同的service需要进行不同的封装，
    // 客户端只知道Service接口，需要一层动态代理根据反射封装不同的Service
    @Override
    public RPCResponse sendRequest(RPCRequest request) {
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

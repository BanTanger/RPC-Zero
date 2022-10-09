package com.bantanger.rpcfromzero.server;

import com.bantanger.rpcfromzero.dao.User;
import com.bantanger.rpcfromzero.mapper.UserService;
import com.bantanger.rpcfromzero.mapper.impl.UserServiceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/9 20:39
 */

public class RPCServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        try {
            // 创建服务端Socket: ServerSocket
            ServerSocket serverSocket = new ServerSocket(8899);
            System.out.println("服务端启动...");
            // BIO(传统网络IO模式)方式监听Socket
            while(true) {
                // 三次握手
                Socket socket = serverSocket.accept(); // 连接客户端的socket并进入监听状态
                // 开启一个线程去处理(不合理之处，每一个客户端都建立一个线程，完毕销毁，会占用大量CPU资源，以及CPU上下文切换的关系)
                new Thread(() -> {
                    try {
                        // 序列化工具: ObjectOutputStream, 反序列化工具: ObjectInputStream
                        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                        // 读取客户端传来的id，ObjectInputStream( 反序列化 )
                        int id = is.readInt();
                        User userByUserId = userService.getUserByUserId(id);
                        // 将用户对象传给客户端, ObjectOutputStream( 序列化 )
                        os.writeObject(userByUserId);
                        os.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("从IO中读取数据错误");
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败");
        }

    }
}

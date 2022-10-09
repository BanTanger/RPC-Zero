package com.bantanger.rpcfromzero.client;

import com.bantanger.rpcfromzero.dao.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/9 18:57
 */

public class RPCClient {
    public static void main(String[] args) {
        try {
            // 建立socket连接
            Socket socket = new Socket("127.0.0.1", 8899);
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            // 要传递的服务器id，这里模拟一个
            os.writeInt(new Random().nextInt());
            os.flush();
            // 服务器查询数据，返回对应对象
            User user = (User) is.readObject();
            System.out.println("服务器返回的User: " + user);
        } catch (IOException | ClassNotFoundException e) { // JDK特性：将多个catch合并在一起
            e.printStackTrace();
            System.out.println("客户端启动失败");
        }
    }
}

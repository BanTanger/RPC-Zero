package com.bantanger.rpcfromzero.server;

import com.bantanger.rpcfromzero.common.RPCRequest;
import com.bantanger.rpcfromzero.common.RPCResponse;
import com.bantanger.rpcfromzero.mapper.UserService;
import com.bantanger.rpcfromzero.mapper.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 服务端接受解析reuqest与封装发送response对象
 * @Date 2022/10/9 20:39
 */

public class RPCServer {

    private static final Logger logger = LoggerFactory.getLogger(RPCServer.class);

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        try {
            // 创建服务端Socket: ServerSocket
            ServerSocket serverSocket = new ServerSocket(8899);
            logger.info("服务端启动...");
            // BIO(传统网络IO模式)方式监听Socket
            while(true) {
                // 三次握手
                Socket socket = serverSocket.accept(); // 连接客户端的socket并进入监听状态
                // 开启一个线程去处理,这个类负责的功能太复杂，以后代码重构中，这部分功能要分离出来
                new Thread(() -> {
                    try {
                        // 序列化工具: ObjectOutputStream, 反序列化工具: ObjectInputStream
                        ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
                        // 读取客户端传来的id，ObjectInputStream( 反序列化 )
                        RPCRequest request = (RPCRequest) is.readObject();
                        // 将用户对象传给客户端, ObjectOutputStream( 序列化 )
                            // 反射调用对应方法
                        Method method = userService.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
                        Object invoke = method.invoke(userService, request.getParams());// 反射调用具体方法，返回动态代理的对象
                        // 封装。写入response
                        os.writeObject(RPCResponse.success(invoke));
                        os.flush();
                    } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        logger.info("从IO中读取数据错误");
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("服务器启动失败");
        }

    }
}

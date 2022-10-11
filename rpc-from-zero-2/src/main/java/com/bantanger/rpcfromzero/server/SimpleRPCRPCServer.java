package com.bantanger.rpcfromzero.server;

import com.bantanger.rpcfromzero.util.ServiceProvider;
import com.bantanger.rpcfromzero.util.WorkThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/11 21:43
 */
public class SimpleRPCRPCServer implements RPCServer {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRPCRPCServer.class);

    // 存放服务接口名 -> service对象的Map
    private ServiceProvider serviceProvider;

    public SimpleRPCRPCServer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            logger.info("服务端启动...");
            // BIO方式监听Socket
            while (true) {
                Socket socket = serverSocket.accept();
                // 开启一个新的线程去处理
                new Thread(new WorkThread(socket, serviceProvider)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("服务器启动失败");
        }
    }

    @Override
    public void stop() {

    }
}

package com.bantanger.rpcfromzero.server.impl;

import com.bantanger.rpcfromzero.server.RPCServer;
import com.bantanger.rpcfromzero.util.ServiceProvider;
import com.bantanger.rpcfromzero.util.WorkThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 线程池版服务端启动类
 * @Date 2022/10/11 23:02
 */
public class ThreadPoolRPCRPCServer implements RPCServer {

    private static final Logger logger = LoggerFactory.getLogger(SimpleRPCRPCServer.class);

    private final ThreadPoolExecutor threadPool;
    private ServiceProvider serviceProvider;

    // 默认构造函数
    public ThreadPoolRPCRPCServer(ServiceProvider serviceProvider) {
        threadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
        this.serviceProvider = serviceProvider;
    }

    // 自定义构造函数
    public ThreadPoolRPCRPCServer(ServiceProvider serviceProvider,
                                  int maximumPoolSize,
                                  int corePoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue) {
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        logger.info("BIO线程池版服务端启动了...");
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while(true) {
                Socket socket = serverSocket.accept();
                threadPool.execute(new WorkThread(socket, serviceProvider));
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("BIO线程池版服务端方法执行出错!");
        }
    }

    @Override
    public void stop() {

    }
}

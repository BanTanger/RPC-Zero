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
 * @Description 抽象RPCServer，开闭原则
 * @Date 2022/10/9 20:39
 */

// 以后的服务端实现这个接口即可
public interface RPCServer {
    void start(int port);
    void stop();
}

package com.bantanger.rpcfromzero.server;

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

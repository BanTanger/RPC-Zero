package com.bantanger.rpcfromzero.server;

import com.bantanger.rpcfromzero.mapper.BlogService;
import com.bantanger.rpcfromzero.mapper.UserService;
import com.bantanger.rpcfromzero.mapper.impl.BlogServiceImpl;
import com.bantanger.rpcfromzero.mapper.impl.UserServiceImpl;
import com.bantanger.rpcfromzero.server.impl.NettyRPCServer;
import com.bantanger.rpcfromzero.util.ServiceProvider;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/11 22:35
 */
public class TestServer {

    private static final Integer port = 8899;

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);

        NettyRPCServer nettyRPCServer = new NettyRPCServer(serviceProvider);
        nettyRPCServer.start(port);
    }
}

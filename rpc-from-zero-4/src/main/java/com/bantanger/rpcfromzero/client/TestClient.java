package com.bantanger.rpcfromzero.client;

import com.bantanger.rpcfromzero.agent.RPCClientProxy;
import com.bantanger.rpcfromzero.client.impl.NettyRPCClient;
import com.bantanger.rpcfromzero.dao.Blog;
import com.bantanger.rpcfromzero.dao.User;
import com.bantanger.rpcfromzero.mapper.BlogService;
import com.bantanger.rpcfromzero.mapper.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/12 23:03
 */

@Slf4j
public class TestClient {

    private static final String host = "127.0.0.1";
    private static final Integer port = 8899;
    private static final ConcurrentHashMap<String, Class<?>> interfaces = new ConcurrentHashMap<>();

    static {
        interfaces.put("UserService", UserService.class);
        interfaces.put("BlogService", BlogService.class);
    }

    public static void main(String[] args) {
        NettyRPCClient nettyRPCClient = new NettyRPCClient(host, port);
        // 使用代理类调用不同的方法
        RPCClientProxy clientProxy = new RPCClientProxy(nettyRPCClient);
        UserService userService = (UserService) clientProxy.getProxy(interfaces.get("UserService"));

        // 执行服务1
        User user = userService.getUserByUserId(new Random().nextInt());
        log.info("服务器端发送返回消息 User = {}", user);
        // 执行服务2
        User user1 = User.builder().userName("张三").id(100).sex(true).build();
        Integer idx = userService.insertUser(user1);
        log.info("服务器端发送返回消息 插入数据位置 = {}", idx);

        // 新增接口测试方法
        BlogService blogService = (BlogService) clientProxy.getProxy(interfaces.get("BlogService"));
        Blog blogById = blogService.getBlogById(100);
        log.info("服务器端发送返回消息 Blog = {}", blogById);
    }
}

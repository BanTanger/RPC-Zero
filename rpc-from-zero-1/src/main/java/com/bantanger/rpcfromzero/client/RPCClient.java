package com.bantanger.rpcfromzero.client;

import com.bantanger.rpcfromzero.agent.ClientProxy;
import com.bantanger.rpcfromzero.dao.User;
import com.bantanger.rpcfromzero.mapper.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 客户端调用不同的方法
 * @Date 2022/10/9 18:57
 */

public class RPCClient {

    private static final Logger logger = LoggerFactory.getLogger(RPCClient.class);

    private static final String host = "127.0.0.1";
    private static final Integer port = 8899;
    private static final ConcurrentHashMap<String, Class<?>> interfaces = new ConcurrentHashMap<>();

    static {
        interfaces.put("UserService", UserService.class);
    }

    public static void main(String[] args) {
        // 使用代理类调用不同的方法
        ClientProxy clientProxy = new ClientProxy(host, port);
        UserService proxy = (UserService) clientProxy.getProxy(interfaces.get("UserService"));

        // 执行服务1
        User user = proxy.getUserByUserId(new Random().nextInt());
        logger.info("服务器端发送返回消息 User = {}", user);
        // 执行服务2
        User user1 = User.builder().userName("张三").id(100).sex(true).build();
        Integer idx = proxy.insertUser(user1);
        logger.info("服务器端发送返回消息 插入数据位置 = {}", idx);
    }
}

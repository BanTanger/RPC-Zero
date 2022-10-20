package com.bantanger.rpcfromzero.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description zookeeper服务注册接口的实现类
 * @Date 2022/10/20 14:46
 */

@Slf4j
public class ZkServiceRegister implements ServiceRegister {

    /**
     * Curator 提供的客户端
     */
    private CuratorFramework client;

    /**
     * Zookeeper 根路径节点
     */
    private static final String ZK_ROOT_PATH = "MyRPC";

    private static final String ZK_CONNECT_PATH = "127.0.0.1:2181";

    private static final Integer ZK_SESSION_TIMEOUT = 40000;

    /**
     * 负责 Zk 客户端的初始化，并于 Zk 服务端建立连接
     */
    public ZkServiceRegister () {
        // 指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        // 服务端，客户端都与 Zk 建立连接
        // 使用Builder建造者模式达到更细粒度的建造
        // sessionTimeoutMs 与 zoo.cfg 中的 tickTime 有关系
        // 默认启用心跳监听保证双方通讯正常
        this.client = CuratorFrameworkFactory.builder()
                .connectString(ZK_CONNECT_PATH)
                .sessionTimeoutMs(ZK_SESSION_TIMEOUT)
                .retryPolicy(policy)
                .namespace(ZK_ROOT_PATH)
                .build();
        this.client.start(); // 启动服务端
        log.info("Zookeeper 连接成功");
    }

    @Override
    public void register(String serviceName, InetSocketAddress serviceAddress) {
        try {
            // 如果不存在该路径, 就注册, 并设置成永久节点: 不删除服务名, 只删除服务地址
            if(client.checkExists().forPath("/" + serviceName) == null) {
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath("/" + serviceName);
            }
            // 如果存在该路径, 就创建临时节点: 服务器下线就删除节点地址
            // 格式为 MyRPC/UserService/
            String path = "/" + serviceName + "/" + getServiceAddress(serviceAddress);
            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("此服务已存在");
        }
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            // 得到的是 服务接口地址集合(分布式环境下多台机器拥有着相同的接口名但不同的接口地址)
            List<String> strings = client.getChildren().forPath("/" + serviceName);
            // 这里默认使用第一个，后面启用负载均衡模式
            String string = strings.get(0);
            return parseAddress(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 地址 -> XXX.XXX.XXX.XXX(主机名):port(端口) 字符串
     * @param serviceAddress 服务接口地址路径
     * @return String 类型的服务接口地址路径
     */
    private String getServiceAddress(InetSocketAddress serviceAddress) {
        return serviceAddress.getHostName() + ":" + serviceAddress.getPort();
    }

    /**
     * 字符串解析为地址
     * @param address 服务接口地址路径
     * @return InetSocketAddress 类型的服务接口地址路径
     */
    private InetSocketAddress parseAddress(String address) {
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }
}

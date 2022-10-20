package com.bantanger.rpcfromzero.register;

import java.net.InetSocketAddress;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 服务注册接口类
 * @Date 2022/10/20 14:46
 */

// 两大基本功能，注册：保存服务与地址。 查询：根据服务名查找地址
public interface ServiceRegister {

    /**
     * 注册服务接口: 保存服务与地址
     * @param serviceName 接口名称
     * @param serviceAddress 接口地址
     */
    void register(String serviceName, InetSocketAddress serviceAddress);

    /**
     * 寻找服务接口: 根据服务名查找地址
     * @param serviceName 接口名称
     * @return 接口具体的服务地址
     */
    InetSocketAddress serviceDiscovery(String serviceName);

}

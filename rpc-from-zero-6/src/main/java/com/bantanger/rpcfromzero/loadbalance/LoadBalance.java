package com.bantanger.rpcfromzero.loadbalance;

import java.util.List;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 给服务器地址列表，根据不同的负载均衡策略选择一个
 * @Date 2022/10/20 16:22
 */
public interface LoadBalance {

    String balance(List<String> addressList);

}

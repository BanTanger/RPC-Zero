package com.bantanger.rpcfromzero.loadbalance.impl;

import com.bantanger.rpcfromzero.loadbalance.LoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 随机负载均衡策略
 * @Date 2022/10/20 16:23
 */

@Slf4j
public class RandomLoadBalance  implements LoadBalance {

    @Override
    public String balance(List<String> addressList) {
        Random random = new Random();
        int choose = random.nextInt(addressList.size());
        log.info("[随机负载均衡]选择了 {} 服务器", choose);
        return addressList.get(choose);
    }
}

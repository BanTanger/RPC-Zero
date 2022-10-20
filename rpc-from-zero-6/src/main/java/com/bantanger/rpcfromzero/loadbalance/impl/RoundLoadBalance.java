package com.bantanger.rpcfromzero.loadbalance.impl;

import com.bantanger.rpcfromzero.loadbalance.LoadBalance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 轮询负载均衡策略
 * @Date 2022/10/20 16:23
 */

@Slf4j
public class RoundLoadBalance implements LoadBalance {
    private int choose = -1;

    @Override
    public String balance(List<String> addressList) {
        choose ++;
        choose = choose % addressList.size();
        log.info("[轮询负载均衡] 选择了 {} 服务器", choose);
        return addressList.get(choose);
    }
}

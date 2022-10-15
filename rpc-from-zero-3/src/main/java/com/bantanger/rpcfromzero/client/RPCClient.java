package com.bantanger.rpcfromzero.client;

import com.bantanger.rpcfromzero.common.RPCRequest;
import com.bantanger.rpcfromzero.common.RPCResponse;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 客户端调用不同的方法
 * @Date 2022/10/9 18:57
 */

public interface RPCClient {

    RPCResponse sendRequest(RPCRequest request);

}

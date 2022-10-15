package com.bantanger.rpcfromzero.util;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description 服务端实际处理线程类，从服务端代码中抽离出来，简化服务端代码，单一职责原则
 * @Date 2022/10/11 22:18
 */

import com.bantanger.rpcfromzero.common.RPCRequest;
import com.bantanger.rpcfromzero.common.RPCResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 解析得到的request请求，执行服务方法，返回给客户端
 * 1. 从request得到interfaceName
 * 2. 根据interfaceName在serviceProvide Map中获取服务端的实现类
 * 3. 从request中得到方法名，参数， 利用反射执行服务中的方法
 * 4. 得到结果，封装成response，写入socket
 */

@AllArgsConstructor
public class WorkThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WorkThread.class);

    private Socket socket;
    private ServiceProvider serviceProvider;

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 读取客户端传来的request
            RPCRequest rpcRequest = (RPCRequest) ois.readObject();
            // 反射调用服务方法获得返回值
            RPCResponse response = getResponse(rpcRequest);
//            logger.info("服务端返回值 {}", response);
            // 写入到客户端
            oos.writeObject(response);
            oos.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("从IO中读取数据错误");
        }
    }

    private RPCResponse getResponse(RPCRequest request) {
        // 得到服务名
        String interfaceName = request.getInterfaceName();
        // 得到服务端对应服务的实现类
        Object service = serviceProvider.getService(interfaceName);
        // 反射调用方法
        Method method = null;
        try {
            method = service.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
            // 通过invoke反射技术调用方法
            Object invoke = method.invoke(service, request.getParams());
            return RPCResponse.success(invoke); // 服务端返回消息
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            logger.error("方法调用失败");
            return RPCResponse.fail();
        }
    }
}

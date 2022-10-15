package com.bantanger.nio_test.block;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/13 20:42
 */
public class Client {
    public static void main(String[] args) {
        try (SocketChannel sc = SocketChannel.open()) {
            // 建立连接
            sc.connect(new InetSocketAddress("localhost", 8888));
            System.out.println("waiting...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.bantanger.nio_test.nio_thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/15 21:47
 */
public class TestClient {
    public static void main(String[] args) throws IOException {
        SocketChannel ssc = SocketChannel.open();
        ssc.connect(new InetSocketAddress("localhost", 8080));
        ssc.write(Charset.defaultCharset().encode("1234567890abcdef"));
        System.in.read();
    }
}

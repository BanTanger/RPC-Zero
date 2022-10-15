package com.bantanger.nio_test.nio_write;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/15 17:48
 */
public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false); // 设置非阻塞
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT); // 关注连接事件
        ssc.bind(new InetSocketAddress(8080));
        while(true) {
            selector.select(); // 选择器轮询
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while(iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove(); // 将这个迭代器移除
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);

                    // 1. 向客户端发送大量的数据
                    StringBuffer sb = new StringBuffer();
                    for(int i = 0; i < 3000000; i ++) {
                        sb.append("a");
                    }
                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());
                    while(buffer.hasRemaining()) { // 实际上在这一步会陷入阻塞
                        // 2. 返回值代表实际写入的字节数
                        int write = sc.write(buffer);
                        System.out.println(write);
                    }
                }
            }
        }

    }
}

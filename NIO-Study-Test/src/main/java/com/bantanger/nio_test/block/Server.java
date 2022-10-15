package com.bantanger.nio_test.block;

import com.bantanger.nio_test.util.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/13 20:41
 */

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    // 阻塞模式
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 阻塞模式
        ssc.bind(new InetSocketAddress(8888));
        List<SocketChannel> channels = new ArrayList<>();
        while(true) {
            logger.info("before connecting...");
            SocketChannel sc = ssc.accept(); // 线程阻塞...
            logger.info("after connecting...{}", sc);

            channels.add(sc);
            for (SocketChannel channel : channels) {
                logger.info("before reading...");
                channel.read(buffer); // 往通道里写数据。线程阻塞...
                buffer.flip(); // 切换读模式
                ByteBufferUtil.debugRead(buffer);
                buffer.clear(); // 清空缓冲区
                logger.info("after read...{}", channel);
            }
        }
    }
}

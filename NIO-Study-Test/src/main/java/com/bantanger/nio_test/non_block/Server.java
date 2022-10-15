package com.bantanger.nio_test.non_block;

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
        ssc.configureBlocking(false); // 开启非阻塞模式
        ssc.bind(new InetSocketAddress(5599));
        List<SocketChannel> channels = new ArrayList<>();
        while(true) {
//            logger.info("before connecting...");
            SocketChannel sc = ssc.accept(); // 线程不阻塞 如果没有开启连接，sc返回null
            if(sc != null) {
                logger.info("after connecting...{}", sc);
                sc.configureBlocking(false);
                channels.add(sc);
            }
            for (SocketChannel channel : channels) {
//                logger.info("before read...");
                int read = channel.read(buffer);// 线程不阻塞 如果没有数据，read返回 0
                if(read > 0) {
                    buffer.flip(); // 切换读模式
                    ByteBufferUtil.debugRead(buffer);
                    buffer.clear(); // 清空缓冲区
                    logger.info("after read...{}", channel);
                }
            }
        }
    }
}

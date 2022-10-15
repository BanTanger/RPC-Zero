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
public class WriteServer2 {
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
                    SelectionKey sckey = sc.register(selector, SelectionKey.OP_READ);

                    // 1. 向客户端发送大量的数据
                    StringBuffer sb = new StringBuffer();
                    for(int i = 0; i < 70000000; i ++) {
                        sb.append("a");
                    }
                    ByteBuffer buffer = Charset.defaultCharset().encode(sb.toString());

                    // 2. 返回值代表实际写入的字节数
                    int write = sc.write(buffer);
                    System.out.println(write);

                    // 3. 判断是否有剩余内容
                    if(buffer.hasRemaining()) {
                        // 4. 关注可写事件
                        // 并且为了避免上一个事件不被覆盖
                        // 使用 | 或者 + 的方式关联两种类型的事件
                        sckey.interestOps(sckey.interestOps() + SelectionKey.OP_WRITE);
                        // 5. 关联上一个缓冲区附件，是未写完的附件
                        sckey.attach(buffer);
                    }
                } else if(key.isWritable()) {
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    SocketChannel sc = (SocketChannel) key.channel();
                    int write = sc.write(buffer);
                    System.out.println(write);
                    // 6. 如果buffer缓冲区为空，代表数据已经全部写入，取消这个附件避免打印打印0
                    if(!buffer.hasRemaining()) {
                        key.attach(null);
                        // 不需要关注可写事件
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                    }
                }
            }
        }

    }
}

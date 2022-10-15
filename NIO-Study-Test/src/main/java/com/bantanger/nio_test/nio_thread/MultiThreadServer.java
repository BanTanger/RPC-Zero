package com.bantanger.nio_test.nio_thread;

import com.bantanger.nio_test.util.ByteBufferUtil;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.bantanger.nio_test.util.ByteBufferUtil.debugAll;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/15 21:18
 */
public class MultiThreadServer {

    private static final Logger logger = LoggerFactory.getLogger(MultiThreadServer.class);

    public static void main(String[] args) {
        try (ServerSocketChannel ssc = ServerSocketChannel.open()){
            // 当前线程为Boss线程，只负责处理客户端的连接
            Thread.currentThread().setName("Boss");
            ssc.bind(new InetSocketAddress(8080));
            // 负责轮询Accept事件的Selector
            Selector boss = Selector.open();
            ssc.configureBlocking(false);
            ssc.register(boss, SelectionKey.OP_ACCEPT);
            // 创建固定的线程数 Worker
            Worker[] workers = new Worker[4];
            // 本来可以使用 Runtime.getRuntime().availableProcessors();
            // 但此方法在docker容器中会出现bug，这个问题只有到了JDK10才有效解决
            AtomicInteger robin = new AtomicInteger(0); // 线程计数要使用原子级别
            for(int i = 0; i < workers.length; i ++) {
                workers[i] = new Worker("worker-" + i);
            }
            while(true) {
                boss.select();
                Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
                while(iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();
                    if(key.isAcceptable()) {
                        // 建立连接
                        SocketChannel sc = ssc.accept();
                        logger.info("connect...{}", sc.getRemoteAddress());
                        sc.configureBlocking(false);
                        // 将sc注册到Worker的Selector中
                        logger.info("before register...{}", sc.getRemoteAddress());
                        workers[robin.getAndIncrement() % workers.length].register(sc);
//                        sc.register(worker.selector, SelectionKey.OP_READ); // 移步到worker-0线程里
                        logger.info("after register...{}", sc.getRemoteAddress());
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Worker implements Runnable {
        private Thread thread;
        private volatile Selector selector;
        private String name;
        // 同步消息队列，用于实现Boss线程和Worker线程之间的通讯
        private ConcurrentLinkedQueue<Runnable> queue;
        private volatile boolean started = false;

        public Worker(String name) {
            this.name = name;
        }

        public void register(SocketChannel sc) throws IOException {
            // 只启动一次
            if(!started) {
                thread = new Thread(this, name);
                selector = Selector.open();
                queue = new ConcurrentLinkedQueue<>();
                thread.start();
                started = true;
            }
            // 向同步队列里添加SocketChannel的注册事件
            // 在Worker线程中执行注册事件
            queue.add(() -> {
                try {
                    // boss线程里的SocketChannel通道
                    sc.register(selector, SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            // 唤醒被阻塞的Selector
            // select类似LockSupport中的park，wakeup的原理类似LockSupport中的unpark
            selector.wakeup();
        }

        @Override
        public void run() {
            while(true) {
                try {
                    selector.select(); // worker-0等待事件，线程阻塞
                    Runnable task = queue.poll();
                    if(task != null) {
                        task.run(); // 如果同步消息队列里存在线程，就启动
                    }
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while(iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();
                        // Worker只负责处理Read事件
                        if (key.isReadable()) {
                            // 简化操作，忽略粘包半包现象
                            SocketChannel sc = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            try {
                                sc.read(buffer);
                                logger.info("read...{}", sc.getRemoteAddress());
                            } catch (IOException e) {
                                key.cancel();
                                ((SocketChannel) key.channel()).socket().close();
                                key.channel().close();
                                e.printStackTrace();
                            }
                            buffer.flip();
                            debugAll(buffer);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

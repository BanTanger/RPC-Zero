import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/12 16:57
 */
public class CopyFileNIOTest {
    public static void fastCopy(String src, String dist) throws IOException {
        // 源文件输入字节流
        FileInputStream fin = new FileInputStream(src);
        // 输入字节流的文件通道
        FileChannel fcin = fin.getChannel();
        // 目的文件输出字节流
        FileOutputStream fout = new FileOutputStream(dist);
        // 输出字节流的文件通道
        FileChannel fcout = fout.getChannel();
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while(true) {
            // 从输入通道中读取数据到缓冲区
            int r = fcin.read(buffer);
            if(r == -1) break;
            // 切换读写，从读模式转换到写模式
            buffer.flip();
            // 把缓冲区里的内容写入到输出文件中
            fcout.write(buffer);
            buffer.clear(); // 清空缓冲区
        }
    }
    @Test
    public void test() throws IOException {
        String src = "D:\\test\\a.txt";
        String dist = "D:\\test\\b.txt";
        fastCopy(src, dist);
    }
}

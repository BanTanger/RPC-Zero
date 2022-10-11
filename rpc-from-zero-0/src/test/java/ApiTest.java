import com.bantanger.rpcfromzero.client.RPCClient;
import com.bantanger.rpcfromzero.dao.User;
import com.bantanger.rpcfromzero.mapper.UserService;
import com.bantanger.rpcfromzero.mapper.impl.UserServiceImpl;
import com.bantanger.rpcfromzero.server.RPCServer;
import org.junit.Before;
import org.junit.Test;
import sun.management.resources.agent;

import java.io.*;
import java.util.Random;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/9 19:01
 */
public class ApiTest {

    File dir = new File("D:\\四级");
    File file = new File("E:\\Java 学习项目集合\\RPC-Zero\\rpc-from-zero-0\\src");
    File file1 = new File("E:\\Java 学习项目集合\\RPC-From-Zero\\rpc-from-zero-1");
    @Test
    public void test() {
        System.out.println(new Random().nextInt());
    }

    @Test
    public void test1() {
//        listAllFiles(dir);
//        System.out.println(file.getName());
        System.out.println(file1.getName());
        tree(file1, 1);
    }

    public static void tree(File f, int level) {
        // 缩进量
        String preStr = "";
        for (int i = 0; i < level; i++) {
            if (i == level - 1) {
                preStr = preStr + "└─";
            } else {
                // 级别 - 代表这个目下下地子文件夹
                preStr = preStr + "    ";
            }
        }
        // 返回一个抽象路径名数组，这些路径名表示此抽象路径名所表示目录中地文件
        File[] childs = f.listFiles();
        for (int i = 0; i < childs.length; i++) {
            // 打印子文件的名字
            if(!childs[i].getName().equals("pom.xml")
                || !childs[i].getName().equals("target")
                || !childs[i].getName().equals("generated-sources")) {
                System.out.println(preStr + childs[i].getName());
                // 测试此抽象路径名表示地文件能否是一个目录
                if (childs[i].isDirectory()) {
                    // 假如子目录下还有子目录，递归子目录调用此方法
                    tree(childs[i], level + 1);
                }
            }
        }
    }

    /**
     * 功能：列出传参文件/目录下的所有文件名和信息（但不能显示文件内容）
     * @param dir
     */
    public static void listAllFiles(File dir) {
        if(dir == null || !dir.exists()) return ;
        if(dir.isFile()) {
            System.out.println(dir.getName());
            return ;
        }
        for (File file : dir.listFiles()) {
            listAllFiles(file);
        }
    }

    @Test
    public void test2() throws IOException {
        copyFile("D:\\test\\a.txt", "D:\\test\\b.txt");
    }

    /**
     * 拷贝文件
     * @param src 源文件名，绝对路径(必须是文件，不能是文件夹）
     * @param dist 目的文件名，绝对路径(必须是文件，不能是文件夹）
     * @throws IOException
     */
    public static void copyFile(String src, String dist) throws IOException {

        FileInputStream in = new FileInputStream(src);
        FileOutputStream out = new FileOutputStream(dist);
        // 字节流传输
        byte[] buffer = new byte[20 * 1024];

        // read() 最多能去读 buffer.length 个字节
        // 返回的是实际读取的个数
        // 返回 -1 时表示读取到 eof ，即文件尾
        while (in.read(buffer, 0, buffer.length) != - 1) {
            out.write(buffer);
        }

        in.close();
        out.close();
    }

    @Test
    public void test3() throws IOException {
        readFileContent("D:\\test\\b.txt");

    }

    /**
     * 实现逐行输出文本文件的内容
     * @param filePath 文件名（绝对路径）
     * @throws IOException
     */
    public static void readFileContent(String filePath) throws IOException {

        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }

        // 装饰器模式是的 BufferedReader 组合了一个 Reader 对象
        // 在调用 BufferedReader 的 clone() 方法会去调用 Reader 的 close() 方法
        // 所以只需要一个 close 即可
        bufferedReader.close();
    }


}

import org.junit.Test;

import java.io.File;

/**
 * @author bantanger 半糖
 * @version 1.0
 * @Description
 * @Date 2022/10/9 21:11
 */

/**
 * 打印项目所有文件的树形结构
 */
public class FileSystem {
    @Test
    public void test() {
        // 指定文件位置
        File f = new File(System.getProperty("E:\\Java 学习项目集合\\RPC-Zero\\rpc-from-zero-0\\src\\main\\java\\com\\bantanger\\rpcfromzero"));
        // 打印在这个文件下的文件夹;
        System.out.println(f.getName());
        // 方法!进入子文件夹中 并打印子文件名
        tree(f, 1);
    }

    private static void tree(File f, int level) {
        // 缩进量
        String preStr = "";
        for (int i = 0; i < level; i++) {
            if (i == level - 1) {
                preStr = preStr + "└─";
            } else {
                // 级别 - 代表这个目下下地子文件夹
                preStr = preStr + "|   ";
            }
        }
        // 返回一个抽象路径名数组，这些路径名表示此抽象路径名所表示目录中地文件
        File[] childs = f.listFiles();
        for (int i = 0; i < childs.length; i++) {
            // 打印子文件的名字
            System.out.println(preStr + childs[i].getName());
            // 测试此抽象路径名表示地文件能否是一个目录
            if (childs[i].isDirectory()) {
                // 假如子目录下还有子目录，递归子目录调用此方法
                tree(childs[i], level + 1);
            }
        }
    }
}

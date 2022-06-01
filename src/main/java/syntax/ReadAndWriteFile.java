package syntax;

import java.io.*;

/**
 * @author 阮启腾
 * @description   读写文件
 * @date 2022/5/11,20:25
 */
public class ReadAndWriteFile {
    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuilder sbf = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            int data;
            while ((data = reader.read()) != -1) {
                sbf.append((char) data);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException ignored) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) { }
            }
        }
        return sbf.toString();
    }

    public static void write(String path, String content) throws IOException {
        //将写入转化为流的形式
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        bw.write(content);
        //关闭流
        bw.close();
    }
}

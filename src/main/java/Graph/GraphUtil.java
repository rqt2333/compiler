package Graph;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/17,13:53
 */
public class GraphUtil {
    private String code = "digraph G {"+"rankdir=LR" + "\nnode [ fontcolor=blue shape=circle ]\n";
    private String sourcePath;
    private String targetPath;

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    //节点A到节点B画一条有向边
    public void link(String dotA, String dotB){
        String linkCode = dotA + " -> " + dotB + "\n";
        this.code += linkCode;
    }

    //节点A到节点B画一条有向边，边权写上label
    public void link(String dotA,String dotB,String label){
        String linkCode = dotA + " -> " + dotB + "[label=" + label + "]" + "\n";
        this.code += linkCode;
    }

    public void node(String dot,String label){
        String nodeCode = dot + "[label=" + label + "]" + "\n";
        this.code += nodeCode;
    }

    public void end(String dot,String color){
        String nodeCode = dot + "[shape=doublecircle color="+color+"]"+"\n";
        this.code += nodeCode;
    }

    public void start(String dot,String color){
        String nodeCode = dot + "[shape=circle color="+color+"]"+"\n";
        this.code += nodeCode;
    }



    //打开已经生成的DAG图片
    public static void openFile(String filePath) {
        try {
            File file = new File(filePath);
            Desktop.getDesktop().open(file);
        } catch (IOException | NullPointerException e) {
            System.err.println(e);
        }
    }

    //使用dot的命令 用dot指令文件 生成DAG图片
    public static void genGraph(String sourcePath,String targetPath) throws IOException, InterruptedException {
        Runtime run = Runtime.getRuntime();
        run.exec("dot "+sourcePath+" -T pdf -o "+targetPath);
        Thread.sleep(1000);
    }

    //整合上面两个方法的功能: 生成图片后自动打开
    public static void genAndOpenGraph(String sourcePath,String targetPath) throws InterruptedException, IOException {
        genGraph(sourcePath,targetPath);
        Thread.sleep(1000);
        openFile(targetPath);
    }

    //保存dot指令到文件  后续利用这个指令文件 就可以用dot命令生成图了
    public static void saveCodeToFile(String filePath, String content) {
        FileWriter fwriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fwriter = new FileWriter(filePath, false);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //一些setter和getter方法
    public String getCode() {
        return code + "}";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

}

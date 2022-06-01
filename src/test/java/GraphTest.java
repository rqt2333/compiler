import Graph.GraphUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/17,13:57
 */
public class GraphTest {
    @Test
    public void test(){
        GraphUtil graphUtil = new GraphUtil(); //new一个自己写的GraphUtil类
        graphUtil.setSourcePath("D:\\computer_study\\Java\\java_project\\compiler\\src\\main\\resources\\graph\\example.txt"); //设置dot指令的txt文件目录
        graphUtil.setTargetPath("D:\\computer_study\\Java\\java_project\\compiler\\src\\main\\resources\\graph\\example.jpg"); //设置要生成的图片的目录路径

        graphUtil.link("A","B"); //A结点向B结点 连一条有向边  实际上就是字符串拼接，dot指令加上 A->B 这个字符串
        graphUtil.link("B","C"); //B结点向C结点 连一条有向边 实际上就是字符串拼接，dot指令加上 B->C 这个字符串
        graphUtil.link("C","D"); //C结点向D结点 连一条有向边
        graphUtil.link("B","D","a"); //B结点向D结点 连一条有向边

        graphUtil.node("A","start"); //在A结点里面写文字
        GraphUtil.saveCodeToFile(graphUtil.getSourcePath(),graphUtil.getCode()); //保存dot指令到example.txt文件,graphUtil.getCode()获取了dot指令的内容
        try {
            GraphUtil.genAndOpenGraph(graphUtil.getSourcePath(),graphUtil.getTargetPath()); //生成图片 并自动打开图片文件
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}

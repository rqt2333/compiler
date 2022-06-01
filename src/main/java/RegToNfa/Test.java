package RegToNfa;

import Graph.GraphUtil;

import java.io.IOException;
import java.util.List;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/17,20:56
 */
public class Test {
    public static void main(String[] args) {
        GraphUtil graphUtil = new GraphUtil(); //new一个自己写的GraphUtil类
        graphUtil.setSourcePath("D:\\computer_study\\Java\\java_project\\compiler\\src\\main\\resources\\graph\\nfa.txt"); //设置dot指令的txt文件目录
        graphUtil.setTargetPath("D:\\computer_study\\Java\\java_project\\compiler\\src\\main\\resources\\graph\\nfa.jpg"); //设置要生成的图片的目录路径
        String expression = "(abcdefg)*hijk*|lmn*opq*|(r*s*t*)*uvw*|(xyz)*";
        String s = RegUtils.infixReversePostfix(expression);
        RegToNfa regToNfa = new RegToNfa();
        NFA nfa = regToNfa.regToNfa(s);
        List<NfaEdge> nfaEdges = nfa.getNfaEdgeArrayList();
        for (NfaEdge nfaEdge : nfaEdges) {
            String start = nfaEdge.getBegin().getStateName();
            String end = nfaEdge.getEnd().getStateName();
            String key = nfaEdge.getKey();
            graphUtil.link(start,end,key); //生成有向图
            System.out.println(nfaEdge);
        }
        graphUtil.end(nfa.getEndState().getStateName(), "red");
        graphUtil.start(nfa.getBeginState().getStateName(), "green");
        GraphUtil.saveCodeToFile(graphUtil.getSourcePath(),graphUtil.getCode()); //保存dot指令到example.txt文件,graphUtil.getCode()获取了dot指令的内容
        try {
            GraphUtil.genAndOpenGraph(graphUtil.getSourcePath(),graphUtil.getTargetPath()); //生成图片 并自动打开图片文件
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}

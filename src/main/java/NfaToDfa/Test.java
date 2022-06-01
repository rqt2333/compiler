package NfaToDfa;

import Graph.GraphUtil;
import RegToNfa.RegToNfa;
import RegToNfa.RegUtils;
import RegToNfa.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/17,20:57
 */
public class Test {
    public static void main(String[] args) {
        GraphUtil graphUtil = new GraphUtil(); //new一个自己写的GraphUtil类
        graphUtil.setSourcePath("D:\\computer_study\\Java\\java_project\\compiler\\src\\main\\resources\\graph\\dfa.txt"); //设置dot指令的txt文件目录
        graphUtil.setTargetPath("D:\\computer_study\\Java\\java_project\\compiler\\src\\main\\resources\\graph\\dfa.jpg"); //设置要生成的图片的目录路径
        String expression = "ab*";
        String s = RegUtils.infixReversePostfix(expression);
        RegToNfa regToNfa = new RegToNfa();
        NFA nfa = regToNfa.regToNfa(s);
        List<NfaEdge> nfaEdges = nfa.getNfaEdgeArrayList();

        String[] symbol = RegUtils.getSymbol(s);
        NfaToDfa dfaToDfa = new NfaToDfa(nfa.getBeginState().getStateName(), nfaEdges, symbol);
        List<Edge> edgeList = dfaToDfa.getEdgeList(nfa.getEndState());
        System.out.println("==========================");
        Set<String> finalState = dfaToDfa.getFinalState();
        for (Edge edge : edgeList) {
            String start = edge.getBegin();
            String end = edge.getEnd();
            for (String state : finalState) {
                if (state.equals(start)) {
                    graphUtil.end(start,"red");
                }
                if (state.equals(end)) {
                    graphUtil.end(end,"red");
                }
            }
            String key = edge.getKey();
            graphUtil.link(start,end,key); //生成有向图
            System.out.println(edge);
        }
        System.out.println("++++++++++++++++++++++++++");
        System.out.println("终态集合：" + finalState);

        GraphUtil.saveCodeToFile(graphUtil.getSourcePath(),graphUtil.getCode()); //保存dot指令到example.txt文件,graphUtil.getCode()获取了dot指令的内容
        try {
            GraphUtil.genAndOpenGraph(graphUtil.getSourcePath(),graphUtil.getTargetPath()); //生成图片 并自动打开图片文件
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}

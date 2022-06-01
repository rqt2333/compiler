package MinDfa;

import Graph.GraphUtil;
import NfaToDfa.NfaToDfa;
import RegToNfa.RegToNfa;
import RegToNfa.RegUtils;
import NfaToDfa.*;
import RegToNfa.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/21,16:14
 */
public class MinDfaGo {
    public void go(String expression){
        GraphUtil graphUtil = new GraphUtil(); //new一个自己写的GraphUtil类
        graphUtil.setSourcePath("D:\\computer_study\\Java\\java_project\\compiler\\src\\main\\resources\\graph\\mfa.txt"); //设置dot指令的txt文件目录
        graphUtil.setTargetPath("D:\\computer_study\\Java\\java_project\\compiler\\src\\main\\resources\\graph\\mfa.pdf"); //设置要生成的图片的目录路径
        String s = RegUtils.infixReversePostfix(expression);
        RegToNfa regToNfa = new RegToNfa();
        NFA nfa = regToNfa.regToNfa(s);
        List<NfaEdge> nfaEdges = nfa.getNfaEdgeArrayList();   //得到nfa的结果
        System.out.println("构造成的NFA如下：");
        for (NfaEdge nfaEdge : nfaEdges) {
            System.out.println(nfaEdge);
        }
        System.out.println("NFA的终态为："+nfa.getEndState().getStateName());
        System.out.println("=============================");

        String[] symbol = RegUtils.getSymbol(s);
        NfaToDfa nfaToDfa = new NfaToDfa(nfa.getBeginState().getStateName(), nfaEdges, symbol);
        List<Edge> newDfaEdgeList = nfaToDfa.getEdgeList(nfa.getEndState());   //得到重命名后dfa的结果
        System.out.println("重命名后构造成的DFA如下：");
        for (Edge edge : newDfaEdgeList) {
            System.out.println(edge);
        }
        System.out.println("终态集合为："+nfaToDfa.getFinalState());
        System.out.println("==============================");

        DFA dfa = new DFA();
        //设置dfa的状态集
        dfa.setStateSet(new ArrayList<>(nfaToDfa.getMap().values()));
        Set<String> finalState = nfaToDfa.getFinalState();
        List<Integer> list = new ArrayList<>();
        for (String str : finalState) {
            list.add(Integer.parseInt(str));
        }

        dfa.setEndStateSet(list);            //设置dfa的最终态集合
        dfa.setS0(nfaToDfa.getStartState());  //设置dfa的唯一初态

        char[] chars = new char[symbol.length];
        for (int i = 0; i < symbol.length; i++){
            chars[i] = symbol[i].charAt(0);
        }
        dfa.setLetters(chars);   //设置字母表
        int stateSize = nfaToDfa.getStateSize();
        String[][] f = new String[stateSize][stateSize];
        for(String[] arr : f){
            Arrays.fill(arr, "");
        }
        for(int i = 0; i < f.length; i++){
            f[i][i] = "ε";
        }
        for (Edge edge :newDfaEdgeList){
            f[Integer.parseInt(edge.getBegin().toString())][Integer.parseInt(edge.getEnd().toString())] += edge.getKey();
        }
        dfa.setFunction(f);    //设置转换函数

        MinDFA minDFA = new MinDFA(dfa, newDfaEdgeList);
        List<Edge> minDFAEdgeList = minDFA.getEdgeList(nfaToDfa.getFinalState());
        Set<String> endStateSet = minDFA.getEndStateSet();
        System.out.println("最小化DFA如下：");
        for (Edge edge : minDFAEdgeList) {
            String start = edge.getBegin();
            String end = edge.getEnd();
            for (String state : endStateSet) {
                if (state.equals(start)) {
                    graphUtil.end(state,"red");
                }
                if (state.equals(end)) {
                    graphUtil.end(end,"red");
                }
            }
            String key = edge.getKey();
            graphUtil.link(start,end,key); //生成有向图
            System.out.println(edge);

        }
        System.out.println("终态集合为：" + minDFA.getEndStateSet());
        GraphUtil.saveCodeToFile(graphUtil.getSourcePath(),graphUtil.getCode()); //保存dot指令到example.txt文件,graphUtil.getCode()获取了dot指令的内容
        try {
            GraphUtil.genAndOpenGraph(graphUtil.getSourcePath(),graphUtil.getTargetPath()); //生成图片 并自动打开图片文件
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }
}

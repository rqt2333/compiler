package NfaToDfa;

import Graph.GraphUtil;
import RegToNfa.NfaEdge;
import RegToNfa.RegToNfa;
import RegToNfa.RegUtils;
import RegToNfa.NFA;
import RegToNfa.State;

import java.io.IOException;
import java.util.*;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/16,14:04
 */
public class NfaToDfa {
    private final String startState;   //整个NFA图中的起始状态
    private final NfaUtils nfaUtils;   //操作nfa的工具实例
    private final String[] letters;    //正规式中的操作数
    private final DFA DFA = new DFA();   //DFA
    private final Map<String, Integer> map = new HashMap<>();   //保存状态及状态的新名字
    private final Set<String> finalStateSet = new LinkedHashSet<>();   //保存哪些状态是终态

    public Map<String, Integer> getMap() {
        return map;
    }

    public String getStartState() {
        return startState;
    }

    public Set<String> getFinalState() {
        return finalStateSet;
    }

    /**
     *
     * @param startState   起始状态
     * @param nfaEdgeList  NFA图中的所有弧
     * @param letters     正规式中的操作数
     */
    public NfaToDfa(String startState, List<NfaEdge> nfaEdgeList, String[] letters) {
        this.startState = startState;
        this.nfaUtils = new NfaUtils(nfaEdgeList);
        this.letters = letters;
    }


    /**
     * 构造新的状态集合
     */
    public void getNewState() {
        //存放每个状态的闭包
        Set<Set<String>> res = new HashSet<>();
        //获取起始点的ε闭包
        Set<String> startPointEpsilonClosure = nfaUtils.getEpsilonClosure(startState);
        res.add(startPointEpsilonClosure);
        getNewState(res, startPointEpsilonClosure);
    }

    /**
     *
     * @param res  存放每个状态的闭包的结果集
     * @param sourcePoint   状态集合（通过这个可以找到下一个状态集合）
     */
    public void getNewState(Set<Set<String>> res,Set<String> sourcePoint){
        for (String pathKey : letters) {
            Set<String> onePathEpsilonSet = new HashSet<>();   //定义某一路径上的 ε 闭包
            //找到当前状态集经过pathKey后的状态集合
            Set<String> move = nfaUtils.getMove(sourcePoint, pathKey);
            for (String s : move) {
                //获取当前状态的ε闭包
                Set<String> epsilonClosure = nfaUtils.getEpsilonClosure(s);
                //将当前可到达状态的ε闭包放进指定的集合中
                onePathEpsilonSet.addAll(epsilonClosure);
            }
            //往DFA中添加弧
            DFA.getDfaEdgeList().add(new DfaEdge(sourcePoint, pathKey, onePathEpsilonSet));
            //如果当前得到的闭包在之前没有出现过
            if (!res.contains(onePathEpsilonSet) && res.size() > 0) {
                res.add(onePathEpsilonSet);  //添加当前闭包
                getNewState(res, onePathEpsilonSet); //递归
            }
        }
    }

    /**
     *
     * @param endState  最终态
     * @return  返回最新命名的弧的集合
     */
    public List<Edge> getEdgeList(State endState) {
        Integer ID = 0;
        getNewState();  //构造状态集
        List<Edge> edgeList = new ArrayList<>();
        System.out.println("生成DFA过程中的状态集如下：");
        for (DfaEdge dfaEdge : DFA.getDfaEdgeList()) {
            String begin = dfaEdge.getBegin().toString();
            String end = dfaEdge.getEnd().toString();
            String key = dfaEdge.getKey().toString();
            if (end.length() == 2) {    //如果终态为空，则不添加
                continue;
            }
            if (map.get(begin) == null) {   //如果map中不包含该起点状态集
                map.put(begin, ID++);
                begin = String.valueOf(ID - 1);  //给该状态重命名
            } else {
                begin = String.valueOf(map.get(begin));
            }
            //如果起点状态集包含最终的状态，则标记这个起点也属于最终态
            if (dfaEdge.getBegin().contains(endState.getStateName())) {
                finalStateSet.add(begin);
            }
            if (map.get(end) == null) {   //如果map中不包含终点状态集
                map.put(end, ID++);
                end = String.valueOf(ID - 1);
            } else {
                end = String.valueOf(map.get(end));
            }
            //如果终点状态集包含最终的状态，则标记这个终点也属于最终态
            if (dfaEdge.getEnd().contains(endState.getStateName())) {
                finalStateSet.add(end);
            }
            edgeList.add(new Edge(begin, end, dfaEdge.getKey()));
            System.out.println(dfaEdge);
        }
        System.out.println("===============================");
        return edgeList;
    }
    public int getStateSize(){
        return map.keySet().size();
    }
}

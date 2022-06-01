package RegToNfa;

import Graph.GraphUtil;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

/**
 * @author 阮启腾
 * @description   //正规式到NFA的转化
 * @date 2022/4/15,11:16
 */
public class RegToNfa {

    /**
     *
     * @param postfixExpression   由中缀表达式转变而来的后缀表达式
     * @return  返回正规式构成的NFA
     */
    public NFA regToNfa(String postfixExpression) {
        State.ID = 0;
        int len = postfixExpression.length();
        char element;
        Stack<NFA> nfaStack = new Stack<>();
        NFA left,right,nfa;
        for (int i = 0; i < len; i++) {
            element = postfixExpression.charAt(i);
            switch (element) {
                case '|':
                    right = nfaStack.pop();
                    left = nfaStack.pop();
                    nfa = do_unit(left, right);
                    nfaStack.push(nfa);
                    break;
                case '*':
                    left = nfaStack.pop();
                    nfa = do_star(left);
                    nfaStack.push(nfa);
                    break;
                case '.':
                    right = nfaStack.pop();  //先弹出右边的nfa
                    left = nfaStack.pop();
                    nfa = do_join(left, right);
                    nfaStack.push(nfa);
                    break;
                default:  //如果是操作数
                    nfa = constructCell(element);
                    nfaStack.push(nfa);
                    break;
            }
        }
        nfa = nfaStack.pop();
        return nfa;
    }

    /**
     * 构造NFA单元
     * @param element  操作数
     * @return
     */
    public NFA constructCell(char element) {
        NFA nfa = new NFA();
        State beginState = new State();
        State endState = new State();
        NfaEdge nfaEdge = new NfaEdge(beginState, endState, String.valueOf(element));
        //将构造的边加入该NFA中
        nfa.getNfaEdgeArrayList().add(nfaEdge);
        nfa.setBeginState(nfa.getNfaEdgeArrayList().get(0).getBegin());
        nfa.setEndState(nfa.getNfaEdgeArrayList().get(0).getEnd());
        return nfa;
    }

    /**
     * 处理a*的情况
     * @param nfa
     * @return   返回新的nfa
     */
    public NFA do_star(NFA nfa){
        NFA newNfa = new NFA();
        nfaEdgeCopy(newNfa, nfa);
        State beginState = new State();
        State endState = new State();
        newNfa.getNfaEdgeArrayList().add(new NfaEdge(beginState, endState, "ε"));
        newNfa.getNfaEdgeArrayList().add(new NfaEdge(nfa.getEndState(), nfa.getBeginState(), "ε"));
        newNfa.getNfaEdgeArrayList().add(new NfaEdge(beginState, nfa.getBeginState(), "ε"));
        newNfa.getNfaEdgeArrayList().add(new NfaEdge(nfa.getEndState(), endState, "ε"));
        newNfa.setBeginState(beginState);
        newNfa.setEndState(endState);
        return newNfa;
    }

    /**
     * 处理ab连接的情况
     * @param left  左nfa
     * @param right 右nfa
     * @return   返回合并的nfa
     */
    public NFA do_join(NFA left, NFA right) {
        NFA nfa = new NFA();
        nfaEdgeCopy(nfa, left);
        nfaEdgeCopy(nfa, right);
        //中间产生一条空弧
        NfaEdge nfaEdge = new NfaEdge(left.getEndState(), right.getBeginState(), "ε");
        nfa.getNfaEdgeArrayList().add(nfaEdge);
        //将左nfa初态设置为现在nfa的初态,右nfa终态设置为nfa的终态
        nfa.setBeginState(left.getBeginState());
        nfa.setEndState(right.getEndState());
        return nfa;
    }

    /**
     *处理a|b的情况
     * @param left  左nfa
     * @param right 右nfa
     * @return   返回合并的nfa
     */
    public NFA do_unit(NFA left, NFA right){
        NFA nfa = new NFA();
        nfaEdgeCopy(nfa, left);
        nfaEdgeCopy(nfa, right);
        State beginState = new State();
        State endState = new State();
        nfa.getNfaEdgeArrayList().add(new NfaEdge(beginState, left.getBeginState(), "ε"));
        nfa.getNfaEdgeArrayList().add(new NfaEdge(beginState, right.getBeginState(), "ε"));
        nfa.getNfaEdgeArrayList().add(new NfaEdge(left.getEndState(), endState, "ε"));
        nfa.getNfaEdgeArrayList().add(new NfaEdge(right.getEndState(), endState, "ε"));
        nfa.setEndState(endState);
        nfa.setBeginState(beginState);
        return nfa;
    }

    /**
     *
     * @param Destination   将边复制到新的NFA
     * @param Source    子NFA
     */
    public void nfaEdgeCopy(NFA Destination, NFA Source) {
        Destination.getNfaEdgeArrayList().addAll(Source.getNfaEdgeArrayList());
    }



}

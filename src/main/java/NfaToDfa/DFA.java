package NfaToDfa;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/16,0:21
 */
public class DFA {
    private List<DfaEdge> dfaEdgeList = new ArrayList<DfaEdge>();   //DFA的弧集合
    private List<Integer> StateSet;   //状态集
    private char[] letters;    //字母表
    private String S0;    			// 唯一初态
    private List<Integer> EndStateSet;    //终态集
    private String[][] function;    //转换函数

    public String[][] getFunction() {
        return function;
    }

    public void setFunction(String[][] function) {
        this.function = function;
    }

    public List<DfaEdge> getDfaEdgeList() {
        return dfaEdgeList;
    }

    public void setDfaEdgeList(List<DfaEdge> dfaEdgeList) {
        this.dfaEdgeList = dfaEdgeList;
    }

    public List<Integer> getStateSet() {
        return StateSet;
    }

    public void setStateSet(List<Integer> stateSet) {
        StateSet = stateSet;
    }

    public char[] getLetters() {
        return letters;
    }

    public void setLetters(char[] letters) {
        this.letters = letters;
    }

    public String getS0() {
        return S0;
    }

    public void setS0(String s0) {
        S0 = s0;
    }

    public List<Integer> getEndStateSet() {
        return EndStateSet;
    }

    public void setEndStateSet(List<Integer> endStateSet) {
        EndStateSet = endStateSet;
    }
}

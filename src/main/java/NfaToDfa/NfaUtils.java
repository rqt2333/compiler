package NfaToDfa;

import RegToNfa.NfaEdge;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 阮启腾
 * @description  NFA工具类
 * @date 2022/4/16,14:06
 */
public class NfaUtils {

    private List<NfaEdge> nfaEdgeList;

    public NfaUtils(List<NfaEdge> nfaEdgeList) {
        this.nfaEdgeList = nfaEdgeList;
    }

    /**
     * 获取该状态的ε闭包
     * @param stateName   状态的名字
     * @return  返回该状态的ε闭包，存储在set中
     */
    public Set<String> getEpsilonClosure(String stateName) {
        Set<String> res = new HashSet<>();
        for (NfaEdge nfaEdge : nfaEdgeList) {
            //如果状态名称和弧上的字符都相同
            if (nfaEdge.getBegin().getStateName().equals(stateName) && nfaEdge.getKey().equals("ε")) {
                //把终端加进去
                res.add(nfaEdge.getEnd().getStateName());
            }
        }
        //当没有空弧时递归结束
        if (res.size() > 0) {
            Set<String> temSet = new HashSet<>();
            for (String next : res) {
                Set<String> epsilonClosure = getEpsilonClosure(next);
                temSet.addAll(epsilonClosure);
            }
            res.addAll(temSet);
        }
        res.add(stateName);   //将当前的状态加入ε闭包中，因为自身可以不经过任何字符就到达自身
        return res;
    }

    /**
     *
     * @param stateSet   状态集合
     * @param pathKey   该状态集合通过的弧上的字符
     * @return    返回当前状态集合经过该字符到达的状态集合
     */
    public Set<String> getMove(Set<String> stateSet, String pathKey) {
        Set<String> res = new HashSet<>();
        for (String setElem : stateSet) {
            for (NfaEdge nfaEdge : nfaEdgeList) {
                if (nfaEdge.getKey().equals(pathKey) && nfaEdge.getBegin().getStateName().equals(setElem)) {
                    //把符合条件的下一个状态放入集合中去
                    res.add(nfaEdge.getEnd().getStateName());
                }
            }
        }
        return res;
    }


}

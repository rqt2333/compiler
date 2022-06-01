package RegToNfa;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/15,10:56
 */
public class NFA {
    //该快NFA包含的边 （后面会合并中间产生的NFA形成最终的NFA）
    private List<NfaEdge> nfaEdgeArrayList = new ArrayList<>();
    private State beginState;   //开始的状态
    private State endState;    //结束的状态

    public List<NfaEdge> getNfaEdgeArrayList() {
        return nfaEdgeArrayList;
    }

    public void setNfaEdgeArrayList(List<NfaEdge> nfaEdgeArrayList) {
        this.nfaEdgeArrayList = nfaEdgeArrayList;
    }

    public State getBeginState() {
        return beginState;
    }

    public void setBeginState(State beginState) {
        this.beginState = beginState;
    }

    public State getEndState() {
        return endState;
    }

    public void setEndState(State endState) {
        this.endState = endState;
    }
}

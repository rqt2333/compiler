package RegToNfa;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/15,10:58
 */
public class NfaEdge {
    private State begin;  //边的起点
    private State end;   //边的终点
    private String key;   //边上的字符

    public NfaEdge(State begin, State end, String key) {
        this.begin = begin;
        this.end = end;
        this.key = key;
    }

    public State getBegin() {
        return begin;
    }
    public void setBegin(State begin) {
        this.begin = begin;
    }

    public State getEnd() {
        return end;
    }

    public void setEnd(State end) {
        this.end = end;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return begin+"->"+end+"[key=\"" + key + "\"];";
    }
}

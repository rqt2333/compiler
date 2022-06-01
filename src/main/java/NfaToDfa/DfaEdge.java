package NfaToDfa;

import java.util.Set;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/16,0:37
 */
public class DfaEdge {
    private final Set<String> begin;    //开始状态集
    private final Set<String> end;    //终结状态集
    private final String key;   //接收的字符


    public DfaEdge(Set<String> begin, String key, Set<String> end) {
        this.begin = begin;
        this.end = end;
        this.key = key;
    }

    public Set<String> getBegin() {
        return begin;
    }

    public Set<String> getEnd() {
        return end;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return begin.toString()+" -"+ key +"->  "+ end.toString();
    }
}

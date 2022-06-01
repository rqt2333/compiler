package NfaToDfa;

import RegToNfa.State;

/**
 * @author 阮启腾
 * @description  //nfa转dfa重命名后的弧
 * @date 2022/4/17,0:27
 */
public class Edge {
    private String begin;  //边的起点
    private String end;   //边的终点
    private String key;   //边上的字符

    public Edge(String begin, String end, String key) {
        this.begin = begin;
        this.end = end;
        this.key = key;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
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
        return begin + "-- " + key + "-->" + end;
    }
}

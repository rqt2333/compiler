package semantic;

import lexer.Word;

/**
 * @author 阮启腾
 * @description  四元式类
 * @date 2022/5/22,17:41
 */
public class Quaternary {
    private int id;     //编号
    private Word operator;    //操作数
    private Word arg1;        //第一个操作数
    private Word arg2;        //第二个操作数
    private Word result;      //中间结果
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Quaternary(Word operator, Word arg1, Word arg2, Word result) {
        this.operator = operator;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public Quaternary() {
    }

    public Quaternary(int id, Word operator, Word arg1, Word arg2, Word result) {
        this.id = id;
        this.operator = operator;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Word getOperator() {
        return operator;
    }

    public void setOperator(Word operator) {
        this.operator = operator;
    }

    public Word getArg1() {
        return arg1;
    }

    public void setArg1(Word arg1) {
        this.arg1 = arg1;
    }

    public Word getArg2() {
        return arg2;
    }

    public void setArg2(Word arg2) {
        this.arg2 = arg2;
    }

    public Word getResult() {
        return result;
    }

    public void setResult(Word result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return " (" + operator + ",  " + arg1 + ",  " + arg2 + ",  " + result + ")";
    }
}

package semantic;

import lexer.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author 阮启腾
 * @description   符号表
 * @date 2022/5/22,14:24
 */
public class SymbolTable {

    public List<Var> SynTable = new ArrayList<>(); //变量表
    public List<Var> Temp = new ArrayList<>(); //中间变量表
    public List<Fun> Func = new ArrayList<>(); //函数表
    public List<Cons> Const = new ArrayList<>(); //常量表
    public LiveStatus liveStatus;

    public final String funcName;     //函数名字
    public final String funcType;     //函数类型

    private int state = 0;     //状态记录
    private String sType;
    private String sName;
    private Word sWord;
    private int off = 0;
    private int offs = 0;
    private boolean isArr = false;
    private boolean isCons = false;
    private int len = 0;
    public List<Pair<String, String>> argsList= new ArrayList<>();

    public SymbolTable(String funcName, String funcType) {
        this.funcName = funcName;
        this.funcType = funcType;
        //一个符号表对应一个LiveStatus
        liveStatus = new LiveStatus();
    }

    public LiveStatus getLiveStatus() {
        return liveStatus;
    }

    public void setLiveStatus(LiveStatus liveStatus) {
        this.liveStatus = liveStatus;
    }

    public void setTable(Word word){
        state = changeState(word, state);//自动机状态变换

    }

    //遇到声明语句时转换状态
    int changeState(Word s, int state) {
        String word = s.getWord();
        switch (state) {
            case 0:
                //遇到以type开头的token时需要转换状态
                if (word.equals("int") || word.equals("char") || word.equals("String") || word.equals("float")) {
                    sType = word;
                    state = 2;     //状态变为2
                } else if (word.equals("const")){
                    isCons = true;
                    state = 1;
                } else {
                    isCons = false;
                    state = 0;
                }
                break;
            case 1:
                if (word.equals("int") || word.equals("char") || word.equals("String") || word.equals("float")) {
                    sType = word;
                    state = 2;
                } else
                    state = 0;
                break;
            case 2:
                sName = word;    //token的名字
                sWord = s;     //某个token
                state = 3;      //转到状态3
                break;
            case 3:
                if (word.equals(",")) {
                    isArr = false;    //标记不是数组
                    len = 1;
                    addSymbol();
                    state = 2;
                } else if (word.equals("[")) {
                    state = 4;
                    isArr = true;      //标记是数组
                    isCons = false;       //标记不是常数
                } else if (!(word.equals(";"))) {      //如果声明变量还没有结束
                    state = 3;
                }
                else {
                    isArr = false;
                    len = 1;
                    addSymbol();    //向变量表中添加该变量
                    isCons = false;
                    state = 0;     //状态再次变为0
                }
                break;
            case 4:
                len = Integer.parseInt(s.getWord());
                isCons = false;
                state = 5;
                break;
            case 5:
                if (word.equals("]")) {
                    isCons = false;
                    addSymbol();
                    state = 6;
                }
                break;
            case 6:
                if (word.equals(",")) {
                    state = 2;
                } else {
                    isCons = false;
                    state = 0;
                }
                break;
        }
        return state;
    }



    //添加符号
    void addSymbol() {
        if (isCons){      //如果是常量
            Cons c = new Cons();
            c.name = sName;
            c.type = sType;
            int size = 0;
            switch (sType) {
                case "int":
                    size = 4;
                    break;
                case "char":
                    size = 1;
                    break;
                case "float":
                    size = 8;
                    break;
                case "String":
                    size = 50;
                    break;
            }
            if (isArr) {
                c.tp = len;
                c.type = sType + "[]";
            }
            if (!SearchExist(sName)) {
                off = offs;
                c.offset = off;
                offs = off + size * len;//加入该变量后末尾地址等于原偏移地址加上该变量的长度
                c.scope = new ArrayList<>(Analysis.actionScope);
                Const.add(c);
            } else {
                Analysis.flag = false;
                Analysis.errorMsg.add("error: " + sWord.getWord() + " 重定义 position (" + sWord.getRow() + ", " + sWord.getCol() + ")");
                System.out.println("error: " + sWord.getWord() + " 重定义 position (" + sWord.getRow() + ", " + sWord.getCol() + ")");
            }
        }else {
            Var v = new Var();    //声明一个变量
            v.name = sName;
            v.type = sType;
            int size = 0;
            switch (sType) {
                case "int":
                    size = 4;
                    break;
                case "char":
                    size = 1;
                    break;
                case "float":
                    size = 8;
                    break;
                case "String":
                    size = 50;
                    break;
            }
            if (isArr) {
                v.tp = len;
                v.type = sType + "[]";
            }

            if (!SearchExist(sName)) {
                off = offs;
                v.offset = off;
                offs = off + size * len;//加入该变量后末尾地址等于原偏移地址加上该变量的长度
                v.scope = new ArrayList<>(Analysis.actionScope);
                SynTable.add(v);     //向变量表中添加该变量
            }
            else {     //如果之前早已存在该变量，那么就报错
                Analysis.flag = false;
                Analysis.errorMsg.add("error: " + sWord.getWord() + " 重定义 position (" + sWord.getRow() + ", " + sWord.getCol() + ")");
                System.out.println("error: " + sWord.getWord() + " 重定义 position (" + sWord.getRow() + ", " + sWord.getCol() + ")");
            }
        }
    }


    //查看某个变量之前是否已经定义过
    boolean SearchExist(String s) {
        int count;
        for (Var v : SynTable) {
            if (v.name.equals(s)) {
                count = 0;
                for (int i = 0; i < v.scope.size(); i++) {
                    if (v.scope.get(i).equals(Analysis.actionScope.get(i))) {
                        count++;
                    }
                }
                if (count == Analysis.actionScope.size() && Analysis.actionScope.size() == v.scope.size()) {
                    return true;
                }
            }
        }
        for (Cons c : Const) {
            if (c.name.equals(s)) {
                count = 0;
                for (int i = 0; i < c.scope.size(); i++) {
                    if (c.scope.get(i).equals(Analysis.actionScope.get(i))) {
                        count++;
                    }
                }
                if (count == Analysis.actionScope.size() && Analysis.actionScope.size() == c.scope.size()) {
                    return true;
                }
            }
        }
        return false;
    }



    //变量类
    public static class Var {
        public String name; // 变量名
        public List<Integer> scope = new ArrayList<>(); //作用域
        public String type; // 变量类型，int,char,float,string
        public String value; // 变量值
        public int tp; // 若数组则为数组单元个数
        public int offset; // 偏移量
        public boolean isTemp = false;    //是否是中间变量

//        @Override
//        public String toString(){
//            StringAlign align = new StringAlign(10, StringAlign.Alignment.CENTER);//调用构造方法，设置字符串对齐为居中对齐，最大长度为50
//            if (value == null)
//                value = "null";
//            return name + "\t" + align.format(type) + "\t" + align.format(value) + "\t" + align.format(offset) + "\t"+ align.format(tp) + "\t" + scope;
//        }
    }


    //常量类
    public static class Cons{
        public String name; // 常量名
        public List<Integer> scope = new ArrayList<>(); //作用域
        public String type; // 常量类型，int,char,float,string
        public String value; // 常量值
        public int tp; // 若数组则为数组单元个数
        public int offset; // 偏移量
//        @Override
//        public String toString(){
//            StringAlign align = new StringAlign(10, StringAlign.Alignment.CENTER);//调用构造方法，设置字符串对齐为居中对齐，最大长度为50
//            if (value == null)
//                value = "null";
//            return name + "\t" + align.format(type) + "\t" + align.format(value)+ "\t" + align.format(offset)  + "\t" + align.format(tp) + "\t" + scope;
//        }
    }

    //函数类
    public static class Fun {
        public String name; // 函数名
        public String type; // 返回值类型，int,char,float，void
        public List<Pair<String, String>> args = new ArrayList<>();

        @Override
        public String toString(){
            return name + "\t\t" + type + "\t\t\t" + args;
        }
    }

    public String getValue(Word word) {
        String value = null;
        for (Var v : SynTable) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionScope){
                stack.push(integer);
            }

            if (v.name.equals(word.getWord()) && isScope(v.scope, stack)) {
                value = v.value ;
                return value;
            }
        }
        for (Var v : SynTable) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionScope){
                stack.push(integer);
            }
            do {
                if (v.name.equals(word.getWord()) && isScope(v.scope, stack)) {
                    value = v.value ;
                    return value;
                }
                if (stack.size() > 0)
                    stack.pop();
            }while (stack.size() > 0);
        }

        for (Cons c : Const) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionScope){
                stack.push(integer);
            }

            if (c.name.equals(word.getWord()) && isScope(c.scope, stack)) {
                value = c.value ;
                return value;
            }
        }
        for (Cons c : Const) {
            Stack<Integer> stack = new Stack<>();
            for (Integer integer : Analysis.actionScope){
                stack.push(integer);
            }
            do {
                if (c.name.equals(word.getWord()) && isScope(c.scope, stack)) {
                    value = c.value ;
                    return value;
                }
                if (stack.size() > 0)
                    stack.pop();
            }while (stack.size() > 0);
        }
        return value;
    }


    private boolean isScope(List<Integer> scopeList, Stack<Integer> actionscope){
        if (scopeList.size() != actionscope.size()){
            return false;
        }
        for (int i = 0; i < scopeList.size(); i++){
            if (!scopeList.get(i).equals(actionscope.get(i))){
                return false;
            }
        }
        return true;
    }

    public List<List<Object>> printTable() {
        List<List<Object>> results = new ArrayList<>();
        System.out.println("变量表：");
        List<Object> synblList = new ArrayList<>();
        for (Var v : SynTable) {
            if (!v.isTemp) {
                synblList.add(v);
                System.out.printf("%10s", v.name);
                System.out.printf("%10s", v.type);
                System.out.printf("%10s", v.value);
                System.out.printf("%10s", v.offset);
                System.out.printf("%10s", v.tp);
                System.out.printf("\t\t\t%-10s", v.scope);
                System.out.println();
            }
        }

        System.out.println("常量表：");
        List<Object> constList = new ArrayList<>();
        for (Cons v : Const) {
            constList.add(v);
            System.out.printf("%10s", v.name);
            System.out.printf("%10s", v.type);
            System.out.printf("%10s", v.value);
            System.out.printf("%10s", v.offset);
            System.out.printf("%10s", v.tp);
            System.out.printf("\t\t\t%-10s", v.scope);
            System.out.println();
        }

        System.out.println("函数表：");
        List<Object> funclList = new ArrayList<>();
        for (Fun f : Func) {
            funclList.add(f);
            System.out.printf("%10s", f.name);
            System.out.printf("%10s", f.type);
            System.out.printf("\t\t\t%-10s", f.args);
            System.out.println();
        }
        results.add(synblList);
        results.add(constList);
        results.add(funclList);
        return results;
    }



}

package lexer;

import semantic.SymbolTable;
import syntax.AllGrammar;
import syntax.TreeNode;
//import syntax.MakeJson;

import java.util.*;

/**
 * @author 阮启腾
 * @description
 * @date 2022/5/10,16:09
 */
public class FinalAttribute {
    //关键字
    private static final String[] keyword = new String[]{"char", "int", "float", "break", "const", "return", "void", "continue", "do", "while", "if", "else", "for", "auto", "case", "default", "double", "enum", "extern", "goto", "long", "register", "short", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "volatile", "print"};

    //分界符
    private static final String[] delimiter ={"{", "}", ";", ","};

    //运算符
    private static final String[] operator ={"(", ")", "[", "]", "!", "*", "/", "%", "+", "-", "++", "--", "<", "<=", ">", ">=", "==", "!=", "&&", "||", "=", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", ">>=", "<<=", "&", "|", "~", "^", "<<", ">>", ".","#"};

    //语义标记
    private static final String[] sem = {"PUSH", "GEQA", "GEQS", "GEQM", "GEQD", "ASSI", "GREA", "LESS", "EQUA", "GREQ", "LEEQ", "NOEQ", "IF", "EL", "IEFIR", "IESEC", "WH", "DO", "WE", "PUSHNUM", "LEVELA", "LEVELS", "ADDFUN", "DOW", "CALLFUN", "ADDARG", "RE", "RET", "CALLFUNARG", "PUSHARG", "CALLS", "ARG", "PRINT", "INDE"};

    //所有的文法
    private static AllGrammar allGrammar;

    //带语义动作的文法
    private static AllGrammar allGrammarWithAction;

    public static AllGrammar getAllGrammarWithAction() {
        return allGrammarWithAction;
    }

    public static void setAllGrammarWithAction(AllGrammar allGrammarWithAction) {
        FinalAttribute.allGrammarWithAction = allGrammarWithAction;
    }

    //first集
    private static Map<String, Set<String>> firstMap = new LinkedHashMap<>();

    //follow集
    private static Map<String, Set<String>> followMap = new LinkedHashMap<>();

    //select集
    private static Map<List<List<String>>, Set<String>> selectMap = new LinkedHashMap<>();

    //预测分析表
    private static LinkedHashMap<String, LinkedHashMap<String, List<TreeNode>>> predictMap;

    public static LinkedHashMap<String, LinkedHashMap<String, List<String>>> getSemPredictMap() {
        return semPredictMap;
    }

    public static void setSemPredictMap(LinkedHashMap<String, LinkedHashMap<String, List<String>>> semPredictMap) {
        FinalAttribute.semPredictMap = semPredictMap;
    }

    //语义预测分析表
    private static LinkedHashMap<String, LinkedHashMap<String, List<String>>> semPredictMap;


    //符号表
    private static Map<String, SymbolTable> symbolTableMap = new LinkedHashMap<>();

    //所有的非终结符
    private static Set<String> allVn = new LinkedHashSet<>();

    //所有的终结符
    private static Set<String> allVt = new LinkedHashSet<>();

    //所有的非终结符
    public static Set<String> getAllVn() {
        return allVn;
    }

    public static void setAllVn(Set<String> allVn) {
        FinalAttribute.allVn = allVn;
    }

    public static Set<String> getAllVt() {
        return allVt;
    }

    public static void setAllVt(Set<String> allVt) {
        FinalAttribute.allVt = allVt;
    }

    public static LinkedHashMap<String, LinkedHashMap<String, List<TreeNode>>> getPredictMap() {
        return predictMap;
    }

    public static void setPredictMap(LinkedHashMap<String, LinkedHashMap<String, List<TreeNode>>> predictMap) {
        FinalAttribute.predictMap = predictMap;
    }

    public static Map<List<List<String>>, Set<String>> getSelectMap() {
        return selectMap;
    }

    public static void setSelectMap(Map<List<List<String>>, Set<String>> selectMap) {
        FinalAttribute.selectMap = selectMap;
    }

    public static Map<String, Set<String>> getFollowMap() {
        return followMap;
    }

    public static void setFollowMap(Map<String, Set<String>> followMap) {
        FinalAttribute.followMap = followMap;
    }

    public static Map<String, Set<String>> getFirstMap() {
        return firstMap;
    }

    public static void setFirstMap(Map<String, Set<String>> firstMap) {
        FinalAttribute.firstMap = firstMap;
    }

    public static AllGrammar getAllGrammar() {
        return allGrammar;
    }

    public static void setAllGrammar(AllGrammar allGrammar) {
        FinalAttribute.allGrammar = allGrammar;
    }

    private static final HashMap<String, Integer> tokenMap = new HashMap<>();

    private static final HashMap<Integer, String> stringMap = new HashMap<>();

    private static final HashMap<Integer, String> nameMap = new HashMap<>();

    public static Map<String, SymbolTable> getSymbolTableMap() {
        return symbolTableMap;
    }

    public static void setSymbolTableMap(Map<String, SymbolTable> symbolTableMap) {
        FinalAttribute.symbolTableMap = symbolTableMap;
    }

    //添加符号表
    public static void addSymbolTable(String func, SymbolTable symbolTable){
        symbolTableMap.put(func, symbolTable);
    }

    //获得某个符号表
    public static SymbolTable getSymbolTable(String func){
        return symbolTableMap.get(func);
    }







    //初始化关键字、分界符、运算符的token
    public static void init(){
        setToken(101, keyword);
        setToken(201, operator);
        setToken(301, delimiter);
        setString();
        setNameMap();
    }

    //设置关键字、分界符、运算符的token
    private static void setToken(int start, String[] strings){
        for(String s : strings){
            tokenMap.put(s, start++);
        }
    }

    //查找token
    public static int findToken(String word){
        if(tokenMap.get(word) == null)
            return 700;
        return tokenMap.get(word);
    }

    private static void setString(){
        int start = 101;
        stringMap.put(700, "id");
        stringMap.put(500, "char_const");
        stringMap.put(800, "float_const");
        stringMap.put(400, "int_const");
        stringMap.put(600, "string_const");
//        stringMap.put(700, "IDENTIFIER");
//        stringMap.put(800, "CONSTANT");
//        stringMap.put(400, "CONSTANT");
//        stringMap.put(600, "STRING_LITERAL");
//        stringMap.put(700, "x");
//        stringMap.put(500, "z");
//        stringMap.put(800, "y");
//        stringMap.put(400, "y");
//        stringMap.put(600, "y");
        for(String s : keyword){
            stringMap.put(start++, s);
        }
    }

    public static String findString(int token, String s){
        if(stringMap.get(token) == null)
            return s;
        else if (token == 800 && s.equals("0")){
            return stringMap.get(400);
        }
        return stringMap.get(token);
    }

    private static void setNameMap(){
        int start = 101;
        nameMap.put(700, "id");
        nameMap.put(500, "char");
        nameMap.put(800, "float");
        nameMap.put(400, "int");
        nameMap.put(600, "string");
        for(String s : keyword){
            nameMap.put(start++, s.toUpperCase());
        }
    }

    public static String findName(int token, String s){
        if(nameMap.get(token) == null)
            return s;
        return nameMap.get(token);
    }

    //获取分界符
    public static String[] getDelimiter() {
        return delimiter;
    }

    //获取运算符
    public static String[] getOperator() {
        return operator;
    }

    public static String[] getKeyword() {
        return keyword;
    }

    public static HashMap<String, Integer> getTokenMap() {
        return tokenMap;
    }


    public static String[] getSem() {
        return sem;
    }

    public static void clearSymbolTableMap() {
        symbolTableMap.clear();
    }
}

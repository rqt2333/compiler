package semantic;

import javafx.scene.control.TextArea;
import lexer.FinalAttribute;
import lexer.ReadAndWriteFile;
import lexer.Word;
import syntax.AllGrammar;
import syntax.GrammaticalHandle;
import syntax.Predict;

import java.util.*;

/**
 * @author 阮启腾
 * @description   总体分析类
 * @date 2022/5/22,21:36
 */
public class Analysis {
    public static int i;
    public static Boolean flag;
    public static List<String> errorMsg;
    public static List<Word> list;
    public static String nowFunc;
    public static int scope;
    public static Stack<Integer> actionScope;

    //初始化
    public static void init(List<Word> list){
        i = 0;
        flag = true;
        errorMsg = new ArrayList<>();
        Analysis.list = list;
        nowFunc = "global";
        actionScope = new Stack<>();
        scope = 0;
        actionScope.push(scope);

        //语义
        String content1 = ReadAndWriteFile.readFileContent("src/main/resources/grammar/MyGrammerWithAction.txt");
        GrammaticalHandle grammaticalHandle1 = new GrammaticalHandle(content1);
        Map<String, List<List<String>>> stringListMap1 = grammaticalHandle1.grammarHandle();
        System.out.println("语义文法如下：");
        for (String s : stringListMap1.keySet()) {
            System.out.println(s + "  ==>  " + stringListMap1.get(s));
        }
        System.out.println("========================");
        //注入语义分析文法
        FinalAttribute.setAllGrammarWithAction(new AllGrammar(stringListMap1));

        Predict predict = new Predict(FinalAttribute.getSelectMap(), FinalAttribute.getFollowMap(), FinalAttribute.getAllGrammar());
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> tableForSemantic = (LinkedHashMap<String, LinkedHashMap<String, List<String>>>) predict.predictTableForSyntaxOrSemantic().get(1);

        //注入语义分析表
        FinalAttribute.setSemPredictMap(tableForSemantic);
        System.out.println("语义分析表如下：");
        tableForSemantic.forEach((k,v)->{
            System.out.println(k);
            System.out.println(v);
            System.out.println("===============");
        });
    }

    public static void analyse(List<Word> list, Object... args){

        Object[] results = new Object[6];
        FinalAttribute.clearSymbolTableMap();   //清理符号表
        SymbolTable rootSymbolTable = new SymbolTable("global", "void");
        FinalAttribute.addSymbolTable(nowFunc, rootSymbolTable);
        LinkedHashMap<String, LinkedHashMap<String, List<String>>> semPredictMap = FinalAttribute.getSemPredictMap();
        Stack<String> stringStack = new Stack<>();
        //将#号和开始符号添加进去
        stringStack.push("#");
        stringStack.push((String) FinalAttribute.getAllGrammar().getGrammarMap().keySet().toArray()[0]);
        int IP = 0;
        Word a = list.get(IP);
        String X = stringStack.pop();

        //开始分析
        while (!X.equals("#")) {
            if (X.equals(a.getName())) {
                IP++;
                a = list.get(IP);
                //转换状态
                FinalAttribute.getSymbolTable(nowFunc).setTable(a);
            } else if (FinalAttribute.getAllVt().contains(X)) {

            } else if (FinalAttribute.getAllVn().contains(X)) {
                if (semPredictMap.get(X).get(a.getName()).size() == 0) {
                    X = stringStack.pop();
                    continue;
                } else if (semPredictMap.get(X).get(a.getName()).get(0).equals("synchronizingToken")) {
                    X = stringStack.pop();
                    continue;
                } else {
                    //获得产生式右部
                    List<String> stringList = semPredictMap.get(X).get(a.getName());
                    //寻找带有语义动作的文法右部
                    List<String> actionList = findAction(stringList, X);
                    List<String> copyAction = new ArrayList<>(actionList.subList(0, actionList.size()));
                    if (FinalAttribute.getAllVn().contains(X) && copyAction.size() == 0) {
                        copyAction.add("ε");
                    }
                    Collections.reverse(copyAction);
                    for (String s : copyAction) {
                        if (!s.equals("ε")) {
                            if (!s.equals("synchronizingToken"))
                                stringStack.push(s);    //将X替换的产生式压栈
                        }
                    }
                }
            }
            else {    //如果是遇到语义动作
                if (!X.equals("ε")){
                    SemanticAnalyse.call(X, IP, list.get(IP - 1), FinalAttribute.getSymbolTable(nowFunc), FinalAttribute.getSymbolTable(nowFunc).getLiveStatus());
                }
            }
            X = stringStack.pop();
        }

        //如果都没有出错
        if (flag){
            //创建程序结束时的四元式
            Quaternary q = new Quaternary();
            q.setOperator(new Word("sys"));
            q.setArg1(new Word("_"));
            q.setArg2(new Word("_"));
            q.setResult(new Word("_"));

            //得到main函数的符号表
            FinalAttribute.getSymbolTable("main").getLiveStatus().getQt().add(q);
            SymbolTable s = FinalAttribute.getSymbolTableMap().get("main");
            LiveStatus liveStatus = s.getLiveStatus();
            System.out.println("四元式如下：");
            if (args.length > 0) {
                TextArea textArea = (TextArea) args[0];
                textArea.appendText("四元式生成如下：\n");
            }
            for (int j = 0; j < liveStatus.getQt().size(); j++) {
                System.out.println(j + " " + liveStatus.getQt().get(j));
                if (args.length > 0) {
                    TextArea textArea = (TextArea) args[0];
                    textArea.appendText(j + " " + liveStatus.getQt().get(j) + "\n");
                }
            }
//            List<List<Object>> listList = s.printTable();
//            results[1] = listList.get(0);
//            results[2] = listList.get(1);
//            results[3] = listList.get(2);
//            SemanticAnalyse.printQuaternary(s.getLiveStatus());
//            results[0] =  FinalAttribute.getSymbolTable("main").getLiveStatus();
//
//            ToAsm asm = new ToAsm();
//            ToNasmCode asm = new ToNasmCode();
//            asm.cToAsm(FinalAttribute.getSymbolTable("main"), FinalAttribute.getSymbolTable("main").getLiveStatu());
//            try{
//                results[4] = asm.getResults();
//            }catch (Exception ignored){
//
//            }
        }





    }

    public static List<String> findAction(List<String> list, String vn) {
        List<String> find = new ArrayList<>();
        for (List<String> list1 : FinalAttribute.getAllGrammarWithAction().getGrammarMap().get(vn)) {
            if (equalsList(list, list1)) {
                find = list1;
                break;
            }
        }
        return find;
    }


    //判断两个产生式是否相等
    public static boolean equalsList(List<String> A, List<String> B) {
        List<String> copyA = new ArrayList<>(A.subList(0, A.size()));
        List<String> copyB = new ArrayList<>(B.subList(0, B.size()));
        copyA.removeAll(Arrays.asList(FinalAttribute.getSem()));
        copyB.removeAll((Arrays.asList(FinalAttribute.getSem())));
        int len = copyA.size();
        if (copyA.size() == copyB.size()) {
            for (int i = 0; i < len; i++) {
                if (!copyA.get(i).equals(copyB.get(i))) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }


}

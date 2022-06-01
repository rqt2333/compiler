package syntax;

import lexer.FinalAttribute;

import java.util.*;

/**
 * @author 阮启腾
 * @description First集
 * @date 2022/5/12,1:07
 */
public class FirstSet {
    //键是非终结符，值是终结符的集合
    public Map<String, Set<String>> firstSet;
    private final Map<String, List<List<String>>> grammars;

    public FirstSet(){
        //初始化文法
        this.grammars = FinalAttribute.getAllGrammar().getGrammarMap();
    }

    public Map<String, Set<String>> getFirstSet() {
        firstSet = new LinkedHashMap<>();
        grammars.forEach((k, v)->{
            Set<String> vnFirst = new LinkedHashSet<>();
            getFirst(k, vnFirst);    //求每个终结符的first集
            firstSet.put(k, vnFirst);
        });
        return firstSet;
    }

    public void setFirstSet(Map<String, Set<String>> firstSet) {
        this.firstSet = firstSet;
    }

    //获得终结符
    private Set<String> getFirst(String vn, Set<String> vnFirst) {
        if (!grammars.containsKey(vn)) {
            vnFirst.add(vn);
            return vnFirst;
        }
        int count = 0;
        boolean nullFlag = false;
        for (List<String> infer : grammars.get(vn)) {
            for (String singleInfer : infer) {
                Set<String> newVnFirst = new LinkedHashSet<>();
                Set<String> checkNull = getFirst(singleInfer, newVnFirst);
                vnFirst.addAll(checkNull);
                vnFirst.remove("ε");
                if (singleInfer.equals("ε")){  //可以推导出空串
                    nullFlag = true;
                }
                if (checkNull.contains("ε") && grammars.containsKey(singleInfer)){ //判断推导内容全为非终结符
                    count++;
                }
                else if (!checkNull.contains("ε")) {  //若当前非终结符不包含空串,则不进行下一个非终结符的分析
                    break;
                }
            }
        }
        if (count == grammars.get(vn).size() || nullFlag) { //推导内容全为非终结符且都推导出空串
            vnFirst.add("ε");
        }
        return vnFirst;
    }
}

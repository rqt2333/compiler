package syntax;

import lexer.FinalAttribute;

import java.util.*;

/**
 * @author 阮启腾
 * @description  select集：遇到终结符时，可以选用的产生式
 *       求解select集的目的是为了后面构造预测分析表提供数据
 *
 * @date 2022/5/12,9:59
 */
public class SelectSet {
    //键是产生式，值是终结符的集合
    //键的第一个<List<String>是产生式的左部
    //   第二个<List<String>是产生式的右部
    private final Map<List<List<String>>, Set<String>> selectSet = new LinkedHashMap<>();

    private final Map<String, List<List<String>>> grammars;

    public SelectSet(){
        this.grammars = FinalAttribute.getAllGrammar().getGrammarMap();
    }


    public Map<List<List<String>>, Set<String>> getSelectSet() {
        grammars.forEach((k, v)->{
            for (List<String> list : v){
                Set<String> vnSelect = new LinkedHashSet<>();
                getSelect(k, vnSelect, list);
                List<List<String>> listList = new ArrayList<>();
                listList.add(Collections.singletonList(k));
                listList.add(list);
                selectSet.put(listList, vnSelect);
            }
        });
        return selectSet;
    }


    private Set<String> getSelect(String vn, Set<String> vnSelect) {
        if (!grammars.containsKey(vn)) {
            vnSelect.add(vn);
            return vnSelect;
        }
        int count = 0;
        boolean nullFlag = false;
        for (List<String> infer : grammars.get(vn)) {
            for (String singleInfer : infer) {
                Set<String> newVnFirst = new LinkedHashSet<>();
                Set<String> checkNull = getSelect(singleInfer, newVnFirst);
                vnSelect.addAll(checkNull);
                vnSelect.remove("ε");
                if (singleInfer.equals("ε")){
                    nullFlag = true;
                }
                if (checkNull.contains("ε") && grammars.containsKey(singleInfer)){
                    count++;
                }
                else if (!checkNull.contains("ε")) {
                    break;
                }
            }
        }
        if (count == grammars.get(vn).size() || nullFlag) {
            vnSelect.add("ε");
        }
        return vnSelect;
    }

    /**
     * 对于产生式A—>α。集合select（A—>α）定义如下：
     *
     * 1. 若α不能推出ε，则select（A—>α） = first（α）。
     *
     * 2. 若α能推出ε，则select（A—>α）= first（α）∪ follow(A)。
     * @return
     */
    private void getSelect(String vn, Set<String> vnSelect, List<String> infer) {
        if (!grammars.containsKey(vn)) {
            vnSelect.add(vn);
            return;
        }
        int count = 0;
        boolean nullFlag = false;

        for (String singleInfer : infer) {
            Set<String> newVnFirst = new LinkedHashSet<>();
            Set<String> checkNull = getSelect(singleInfer, newVnFirst);
            vnSelect.addAll(checkNull);
            vnSelect.remove("ε");
            if (singleInfer.equals("ε")) {
                nullFlag = true;
            }
            if (checkNull.contains("ε") && grammars.containsKey(singleInfer)) {
                count++;
            } else if (!checkNull.contains("ε")) {
                break;
            }
        }
        if (count == grammars.get(vn).size() || nullFlag) {
            vnSelect.add("ε");
        }
    }
}

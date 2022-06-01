package syntax;

import java.util.List;
import java.util.Map;

/**
 * @author 阮启腾
 * @description  文法类
 * @date 2022/5/11,20:43
 */
public class AllGrammar {
    //这里的键是产生式左部，值是产生式右部
    private Map<String, List<List<String>>> grammarMap;

    public AllGrammar(Map<String, List<List<String>>> grammarMap) {
        this.grammarMap = grammarMap;
    }

    public Map<String, List<List<String>>> getGrammarMap() {
        return grammarMap;
    }

    public void setGrammarMap(Map<String, List<List<String>>> grammarMap) {
        this.grammarMap = grammarMap;
    }
}

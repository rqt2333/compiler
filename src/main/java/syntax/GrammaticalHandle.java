package syntax;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 阮启腾
 * @description   处理文法的类
 * @date 2022/5/11,18:37
 */
public class GrammaticalHandle {
    String myGrammar ;

    public GrammaticalHandle(String myGrammar) {
        this.myGrammar = myGrammar;
    }

    //处理语法
    public Map<String, List<List<String>>> grammarHandle() {
        Map<String, List<List<String>>> grammarMap = new LinkedHashMap<>();
//        String[] grammarList = myGrammar.split("\t;");
        String all = myGrammar.replaceAll("\n|\t", "");
        String[] grammarList = all.split(";;");
        for (String grammar : grammarList){
//            String[] split = grammar.split("\t:");
            String[] split = grammar.split(":");
            String g = replaceBlank(split[0]);
//            String[] replaceableList = split[1].split("\t\\|");
            String[] replaceableList = split[1].split("\\|");

//            String g =  replaceBlank(grammar.split("\t:")[0]);
//            String[] replaceableList = grammar.split("\t:")[1].split("\t\\|");
            List<List<String>> arrayList = new ArrayList<>();
            for (String word : replaceableList){
                List<String> stringArrayList = new ArrayList<>();
                for (String s : replaceBlank(word).split(" ")){
                    if (s.startsWith("'")){
                        s = s.substring(1, s.length() - 1);
                    }
                    stringArrayList.add(s);
                }
                stringArrayList.removeAll(Collections.singleton(""));
                arrayList.add(stringArrayList);
            }
            grammarMap.put(g, arrayList);
        }
        return grammarMap;
    }

    public String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\t|\r|\n|'");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}

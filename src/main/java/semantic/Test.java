package semantic;

import lexer.FinalAttribute;
import lexer.LexicalAnalyzer;
import lexer.ReadAndWriteFile;
import lexer.Word;
import syntax.AllGrammar;
import syntax.GrammaticalHandle;
import syntax.Predict;
import syntax.SyntaxAnalysis;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 阮启腾
 * @description
 * @date 2022/5/22,15:47
 */
public class Test {
    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer("src/main/resources/test.c");
        lexicalAnalyzer.runAnalyzer();
        List<Word> list = new LinkedList<>();
        List<Word> words = lexicalAnalyzer.getWords();
        for (Word word : words) {
            word.setName(FinalAttribute.findString(word.getTypenum(), word.getWord()));
            list.add(word);
        }
        System.out.println("================================");
        list.add(new Word("#", "end", -1,-1));
        list.get(list.size() - 1).setName("#");

        SyntaxAnalysis.init();
//      List<List<String>> analysis = SyntaxAnalysis.analysis(list);

        Analysis.init(list);
        Analysis.analyse(list);












    }
}

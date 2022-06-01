package ToTargetCode;

import lexer.FinalAttribute;
import lexer.LexicalAnalyzer;
import lexer.Word;
import semantic.Analysis;
import syntax.SyntaxAnalysis;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 阮启腾
 * @description
 * @date 2022/5/23,21:33
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

        Analysis.init(list);
        Analysis.analyse(list);
        ToNasmCode toNasmCode = new ToNasmCode();
        toNasmCode.cToAsm(FinalAttribute.getSymbolTable("main"), FinalAttribute.getSymbolTable("main").getLiveStatus());

    }
}

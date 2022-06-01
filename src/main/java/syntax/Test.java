package syntax;
import lexer.FinalAttribute;
import lexer.LexicalAnalyzer;
import lexer.Word;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 阮启腾
 * @description
 * @date 2022/5/11,18:49
 */
public class Test {
    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer("src/main/resources/test.c");
        lexicalAnalyzer.runAnalyzer();
        List<Word> list = new LinkedList<>();
        List<Word> words = lexicalAnalyzer.getWords();
        for (Word word : words) {
            word.setName(FinalAttribute.findString(word.getTypenum(), word.getWord()));
            System.out.println(word);
            list.add(word);
        }
        System.out.println("================================");
        list.add(new Word("#", "end", -1,-1));
//        for (Word word : list) {
//            System.out.println(word);
//        }
        list.get(list.size() - 1).setName("#");
        SyntaxAnalysis.init();
        SyntaxAnalysis.analysis(list);
        SyntaxAnalysis.drawTree();
    }
}

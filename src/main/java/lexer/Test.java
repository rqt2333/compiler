package lexer;

import java.util.List;

/**
 * @author 阮启腾
 * @description
 * @date 2022/5/10,16:08
 */
public class Test {
    public static void main(String[] args) {
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer("src/main/resources/第二次测试 - 副本.txt");
        lexicalAnalyzer.runAnalyzer();
        List<Word> words = lexicalAnalyzer.getWords();
        List<Word> errorMsgList = lexicalAnalyzer.getErrorMsgList();
        for (Word word : words) {
            System.out.println(word + " " + word.getTypenum() + " " + word.getType());
        }
        for (Word word : errorMsgList) {
            System.out.println(word + " " + word.getType() + " " + word.getRow() + " " + word.getCol());
        }

//        List<Word> list = new LinkedList<>();
//        for (Word word : lexicalAnalyzer.getWords()) {
//            word.setName(FinalAttribute.findString(word.getTypenum(), word.getWord()));
//            list.add(word);
//        }
//        for (Word word : list) {
//            System.out.println(word);
//        }

    }
}

package lexer;

/**
 * @author 阮启腾
 * @description
 * @date 2022/3/19,17:21
 */
public class Data {
    private String word;
    private String wordType;
    private String token;

    public Data(String word, String wordType, String token) {
        this.word = word;
        this.wordType = wordType;
        this.token = token;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

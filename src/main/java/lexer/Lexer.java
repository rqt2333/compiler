package lexer;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;


/**
 * @author 阮启腾
 * @description
 * @date 2022/3/12,18:06
 */
public class Lexer {   //词法分析器
    public HashMap<String,String> keyMap = new HashMap<>();
    public HashMap< String,String> limiterMap=new HashMap<>();
    public HashMap<String,String> operatorMap =new HashMap<>();
    public HashMap<String,String> wordTypeMap =new HashMap<>();
    public int row = 1;
    public String text = "";   //存放文本文件


    //返回Date
    public Data createDate(String str, String wordType, String token) {
        return new Data(str, wordType, token);
    }

    public Lexer() throws IOException {
        //关键字集合
//        HashMap<Integer, String> keyMap = new HashMap<>();
        keyMap.put( "char","101");
        keyMap.put("int","102");
        keyMap.put("float","103");
        keyMap.put("break","104");
        keyMap.put("const","105");
        keyMap.put("return","106");
        keyMap.put("void","107");
        keyMap.put("continue","108");
        keyMap.put("do","109");
        keyMap.put("while","110");
        keyMap.put("if","111");
        keyMap.put("else","112");
        keyMap.put("for","113");

        //界符集合
//        HashMap<Integer, String> limiterMap = new HashMap<>();
        limiterMap.put("{","301");
        limiterMap.put("}","302");
        limiterMap.put(";","303");
        limiterMap.put(",","304");
        limiterMap.put("#", "305");

        //运算符集合
//        HashMap<Integer, String> operatorMap = new HashMap<>();
        operatorMap.put("(","201");
        operatorMap.put(")","202");
        operatorMap.put("[","203");
        operatorMap.put("]","204");
        operatorMap.put("!","205");
        operatorMap.put("*","206");
        operatorMap.put("/","207");
        operatorMap.put("%","208");
        operatorMap.put("+","209");
        operatorMap.put("-","210");
        operatorMap.put("<","211");
        operatorMap.put("<=","212");
        operatorMap.put(">","213");
        operatorMap.put(">=","214");
        operatorMap.put("==","215");
        operatorMap.put("&&","216");
        operatorMap.put("||","216");
        operatorMap.put("=","217");
        operatorMap.put("!=","218");
        operatorMap.put(".","219");
        operatorMap.put("++", "220");
        operatorMap.put("--", "221");
        operatorMap.put("+=", "222");
        operatorMap.put("+-", "223");
        operatorMap.put(">>", "224");
        operatorMap.put("<<", "225");
        operatorMap.put("%=", "226");
        operatorMap.put("*=", "227");
        operatorMap.put("/=", "228");
        operatorMap.put("&", "229");
        operatorMap.put("|", "230");
        operatorMap.put("-=", "231");


        //单词类别集合
        wordTypeMap.put("整数","400");
        wordTypeMap.put("字符","500");
        wordTypeMap.put("字符串","600");
        wordTypeMap.put("标识符","700");
        wordTypeMap.put("实数","800");

    }

    //读取文本文件
    public String readText(String fileName) throws IOException {
        //从文件中读取字符
//        FileReader fileReader = new FileReader(fileName);
        FileInputStream fileInputStream = new FileInputStream(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String s = "";
        while ((s = bufferedReader.readLine()) != null) {
            text += s;
            text += "\n";
        }
        bufferedReader.close();
        inputStreamReader.close();
        return text;
    }

    //处理第一个字符是字母或下划线的情况
    public int  handleFirstAlpha(int i, String s, ObservableList<Data> list){
        char c = text.charAt(++i);
        String str = s;
        while (Character.isLetter(c) || Character.isDigit(c) || c == '_') {
            //如果接下来的字符是字母、数字或是_
            str+=c;
            c = text.charAt(++i);
        }
        if (str.length() == 1) {  //如果只有一个，说明是字符常数
            String type = "字符";
            Data data = createDate(str, "字符常数", wordTypeMap.get(type));
            list.add(data);
//            textArea.appendText(str + "：字符常数" + "  token:" + wordTypeMap.get(type));
//            textArea.appendText("\n");
//            System.out.println(str + "：字符常数" + "  token:" + wordTypeMap.get(type));
            return i;
        }
        if (keyMap.containsKey(str)) {    //如果是关键字
            Data data = createDate(str, "关键字", keyMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":关键字" + "  token:" +keyMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":关键字" + "  token:" +keyMap.get(str));
            return i;
        } else {
            String type = "标识符";
            Data data = createDate(str, "普通标识符", wordTypeMap.get(type));
            list.add(data);
//            textArea.appendText(str + ":普通标识符" + "  token:" + wordTypeMap.get(type));
//            textArea.appendText("\n");
//            System.out.println(str + ":普通标识符" + "  token:" + wordTypeMap.get(type));
            return i;
        }
    }

    //处理第一个字符是数字的情况
    public int handleFirstNumber(int i, String s,ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        while (Character.isDigit(c)) {
            str+=c;
            c = text.charAt(++i);
        }
        if (text.charAt(i) == ' ' || text.charAt(i) == '\t' || text.charAt(i) == '\n' || text.charAt(i) == '\r'
                || text.charAt(i) == ';' || text.charAt(i) == ',' || text.charAt(i) == ']'||text.charAt(i)==')') {
            //如果到了末尾或是遇到']'
            String type = "整数";
            Data data = createDate(str, "整数", wordTypeMap.get(type));
            list.add(data);
//            textArea.appendText(str + "：整数" + "  token:" + wordTypeMap.get(type));
//            textArea.appendText("\n");
//            System.out.println(str + "：整数" + "  token:" + wordTypeMap.get(type));
            return i;
        } else if (c == 'E' || c == 'e') {
            str+=c;
            c = text.charAt(++i);
            if (c == '+' || c == '-' || Character.isDigit(c)) {
                str += c;
                c = text.charAt(++i);
                while (Character.isDigit(c)) {
                    str += c;
                    c = text.charAt(++i);
                }
                String type = "实数";
                Data data = createDate(str, "实数（科学计数法）", wordTypeMap.get(type));
                list.add(data);
                return i;
            } else {
                System.out.println("第" + row+ "行：" + "错误的标识符:" + str);
                return i;
            }
        } else if (c == '.') {
            str += c;
            c = text.charAt(++i);
            if (Character.isDigit(c)) {
                str += c;
                c = text.charAt(++i);
                while (Character.isDigit(c)) {     //直到不是数字
                    str += c;
                    c = text.charAt(++i);
                }
                if (c == 'E' || c == 'e') {
                    str += c;
                    c = text.charAt(++i);
                    if (c == '+' || c == '-' || Character.isDigit(c)) {
                        str += c;
                        c = text.charAt(++i);
                        while (Character.isDigit(c)) {
                            str += c;
                            c = text.charAt(++i);
                        }
                        String type = "实数";
                        Data data = createDate(str, "实数（科学计数法）", wordTypeMap.get(type));
                        list.add(data);
                        return i;
                    } else {
                        System.out.println("第" + row + "行：" + "错误的标识符:" + str);
                        return i;
                    }
                } else if (c != '\n') {
                    str += c;
                    c = text.charAt(++i);
                    while (c != '\n') {
                        str += c;
                        c = text.charAt(++i);
                    }
                    System.out.println("第" + row + "行：" + "浮点数错误:" + str);
                    return i;
                } else {
                    String type = "实数";
                    Data data = createDate(str, "实数", wordTypeMap.get(type));
                    list.add(data);
                    return i;
                }
            } else {
                if (c != '\n') {
                    str += c;
                    c = text.charAt(++i);
                    while (c != '\n') {
                        str+=c;
                        c = text.charAt(++i);
                    }
                }
                System.out.println("第" + row + "行：" + "浮点数错误:" + str);
                return i;
            }
        } else {
            str+=c;
            System.out.println("第" + row + "行：" + "错误的标识符:" + str);
            return ++i;
        }
    }

    //扫描整个文件
    public void scannerAll(ObservableList<Data> list){
        int i=0;
        char c;
        while (i < text.length()) {
            c = text.charAt(i);
            if (c==' '||c=='\t'){   //如果c为空格或者tab,那么扫描下一个字符
                i++;
            } else if (c == '\n' || c == '\r') {  //c为回车或者换行
                row++;
                i++;
            } else if (c == '#') {
                String str = "";
                str+=c;
                Data data = createDate(str, "界符", limiterMap.get(c));
                list.add(data);
//                textArea.appendText(c + ":界符" + "  token:" + limiterMap.get(c));
                i++;
            } else {
                i = scannerPart(i,list);
            }
        }
    }

    //扫描单词
    public int scannerPart(int i,ObservableList<Data> list){
        char c = text.charAt(i);
        String string = "";
        if (Character.isLetter(c)||c=='_') {   //如果第一个字符是字母或下划线
            string+=c;
            return handleFirstAlpha(i, string,list);
        } else if (Character.isDigit(c)) {    //如果第一个字符是数字
            if (c == '0') {
                if (text.charAt(i+1) == 'x' || text.charAt(i+1) == 'X') {
                    string+=c;
                    c = text.charAt(++i);
                    string+=c;
                    return handHex(i, string, list);    //处理十六进制
                } else {
                    string+=c;
                    return handleOctal(i, string, list);   //处理八进制
                }
            }
            string+=c;
            return handleFirstNumber(i, string,list);
        }else {   //第一个字符既不是数字也不是字母或下划线
            string+=c;
            switch (c) {
                case '\n':
                case '\t':
                case '\r':
                case ' ':
                    return ++i;      //一些空格的情况
                case '(':
                case ')':
                case '[':
                case ']':
                case '.':
                    Data data = createDate(string, "运算符", operatorMap.get(string));
                    list.add(data);
//                    textArea.appendText(string + ":运算符" + "  token:" + operatorMap.get(string));
//                    textArea.appendText("\n");
//                    System.out.println(string + ":运算符" + "  token:" + operatorMap.get(string));
                    return ++i;
                case '{':
                case '}':
                case ';':
                case ',':
                    Data data1 = createDate(string, "界符", limiterMap.get(string));
                    list.add(data1);
//                    textArea.appendText(string + "界符" + "  token:" + limiterMap.get(string));
//                    textArea.appendText("\n");
//                    System.out.println(string + "界符" + "  token:" + limiterMap.get(string));
                    return ++i;
                case '+':    //处理加法运算符
                    return handlePlus(i, string,list);
                case '-':    //处理减法运算符
                    return handleMinus(i, string,list);
                case '!':
                case '=':    //处理感叹号和等于号
                    return handleEqualAndExclamatory(i, string,list);
                case '>':    //处理大于号
                    return handleMore(i,string,list);
                case '<':    //处理小于号
                    return handLess(i,string,list);
                case '%':    //处理求余号
                    return handleMod(i,string,list);
                case '\'':   //处理单引号
                    return handleSingleQuotes(i,string,list);
                case '\"':   //处理双引号
                    return handleDoubleQuotes(i,string,list);
                case '*':    //处理乘号
                    return handleMultiply(i,string,list);
                case '/':    //处理除号
                    return handleDivision(i,string,list);
                case '&':
                    return handleAnd(i, string, list);
                case '|':
                    return handOr(i, string, list);
                default:
                    c = text.charAt(++i);
                    while (c != '\n') {
                        string+=c;
                        c = text.charAt(++i);
                    }
                    string+=c;
                    System.out.println("第"+row+"行:"+"非法字符:" + string);
                    return ++i;
            }
        }
    }

    //处理十六进制
    private int handHex(int i, String s, ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        if (Character.isDigit(c) || (c<'f'&&c>='a')||(c<'F'&&c>='A')) {
            str += c;
            c = text.charAt(++i);
            while (Character.isDigit(c) || (c<'f'&&c>='a')) {
                str += c;
                c = text.charAt(++i);
            }
            if (text.charAt(i) == ' ' || text.charAt(i) == '\t' || text.charAt(i) == '\n' || text.charAt(i) == '\r'
                    || text.charAt(i) == ';' || text.charAt(i) == ',' || text.charAt(i) == ']' || text.charAt(i) == ')') {
                //如果到了末尾或是遇到']'
                String type = "整数";
                Data data = createDate(str, "十六进制整数", wordTypeMap.get(str));
                list.add(data);
                return i;
            } else {
                str += c;
                c = text.charAt(++i);
                while (c != '\n') {
                    str += c;
                    c = text.charAt(++i);
                }
                System.out.println("第" + row + "行：" + "错误的十六进制:" + str);
                return i;
            }
        } else {
            str += c;
            c = text.charAt(++i);
            while (c != '\n') {
                str += c;
                c = text.charAt(++i);
            }
            System.out.println("第" + row + "行：" + "错误的字符:" + str);
            return i;
        }
    }

    //处理八进制
    private int handleOctal(int i, String s, ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        if (c <= '7' && c >= '0') {
            str += c;
            c = text.charAt(++i);
            while (c <= '7' && c >= '0') {
                str += c;
                c = text.charAt(++i);
            }
            if (text.charAt(i) == ' ' || text.charAt(i) == '\t' || text.charAt(i) == '\n' || text.charAt(i) == '\r'
                    || text.charAt(i) == ';' || text.charAt(i) == ',' || text.charAt(i) == ']' || text.charAt(i) == ')') {
                //如果到了末尾或是遇到']'
                String type = "整数";
                Data data = createDate(str, "八进制整数", wordTypeMap.get(str));
                list.add(data);
                return i;
            }
        } else if (str.length() == 1 && !Character.isDigit(c)) {
            String type = "整数";
            Data data = createDate(str, "整数", wordTypeMap.get(str));
            list.add(data);
            return i;
        } else {
            str += c;
            c = text.charAt(++i);
            while (c != '\n') {
                str += c;
                c = text.charAt(++i);
            }
            System.out.println("第" + row + "行：" + "错误的八进制:" + str);
            return i;
        }
        return ++i;
    }

    private int handOr(int i, String s, ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        if (c == '|') {
            str += c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
            return ++i;
        } else {
            Data data = createDate(str, "运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
            return i;
        }
    }

    //处理&号
    private int handleAnd(int i, String s, ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        if (c == '&') {
            str += c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
            return ++i;
        } else {
            Data data = createDate(str, "运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
            return i;
        }
    }

    //处理除号
    public int handleDivision(int i, String s,ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        if (c == '=') {    //处理 /= 的情况
            str+=c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");

//            System.out.println(str + ":双目运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        } else if (c == '/') {    //处理单行注释
            str+=c;
            c = text.charAt(++i);
            while (c != '\n' && c != '\r' && c != '\0') {
                str += c;
                c = text.charAt(++i);
            }
            Data data = createDate(str, "单行注释", "1");
            list.add(data);
//            textArea.appendText(str + ":单行注释");
//            textArea.appendText("\n");
//            System.out.println(str + ":单行注释");
            return i;
        } else if (c == '*') {      //处理多行注释
            str+=c;
            c = text.charAt(++i);
            while (true) {
                while (c != '*') {
                    str += c;
                    c = text.charAt(++i);
                }
                str+=c;
                c = text.charAt(++i);
                if (c == '/') {
                    break;
                } else if (c == '\n') {
                    row++;
                }
            }
            str+=c;
            Data data = createDate(str, "多行注释", "2");
            list.add(data);
            return ++i;
        } else {
            Data data = createDate(str, "运算符", operatorMap.get(str));
            list.add(data);
            return i;
        }
    }

    //处理乘号的情况
    public int handleMultiply(int i, String s,ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        if (c == '=') {
            str += c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":双目运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        } else {
            Data data = createDate(str, "运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":运算符" + "  token:" + operatorMap.get(str));
            return i;
        }
    }

    //处理双引号
    public int handleDoubleQuotes(int i, String s,ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        while (c != '\"') {
            if (c == '\n' || c == '\r') {
                row++;
            }
            str+=c;
            if (c == '\n') {
                System.out.println("第" + row + "行:" + "双引号发生错误:" + str);
                return ++i;
            } else {
                c = text.charAt(++i);
            }
        }
        str+=c;
        String type = "字符串";
        Data data = createDate(str, "字符串", wordTypeMap.get(type));
        list.add(data);
        return ++i;
    }

    //处理单引号
    public int handleSingleQuotes(int i, String s,ObservableList<Data> list) {
        char c = text.charAt(++i);  //双引号中间的字符
        String str = s;
        str+=c;
        c = text.charAt(++i);
        if (c == '\'') {
            String type = "字符";
            Data data = createDate(str, "单字符", wordTypeMap.get(type));
            list.add(data);
            return ++i;
        } else {
            System.out.println("第" + row + "行:" + "单引号发生错误:" + str);
            return i;
        }
//        while (c != '\'') {
//            if (c == '\n' || c == '\r') {
//                row++;
//            } else if (c == '\0') {    //如果遇到了结束符，说明单字符错误
//                System.out.println("单字符出现错误");
//                return i;
//            }
//            str+=c;
//            c = text.charAt(++i);
//        }
//        str+=c;
//        if (str.length() == 3) {
//            String type = "字符";
//            Data data = createDate(str, "单字符", wordTypeMap.get(type));
//            list.add(data);
//            return ++i;
//        }else {
//            System.out.println("第" + row + "行:" + "单引号发生错误" + str);
//            return ++i;
//        }
    }

    //处理 % 号
    public int handleMod(int i, String s,ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        if (c == '=') {
            str+=c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":双目运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        }else {
            Data data = createDate(str, "单字符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":运算符" + "  token:" + operatorMap.get(str));
            return i;
        }
    }

    //处理小于号
    public int handLess(int i, String s,ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        if (c == '=') {
            str += c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":双目运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        } else if (c == '<') {
            str += c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":双目运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        } else {
            Data data = createDate(str, "运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":运算符" + "  token:" + operatorMap.get(str));
            return i;
        }
    }

    //处理大于号
    public int handleMore(int i, String s,ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        if (c == '=') {
            str += c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":双目运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        } else if (c == '>') {
            str += c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":双目运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        } else {
            Data data = createDate(str, "运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":运算符" + "  token:" + operatorMap.get(str));
            return i;
        }
    }

    //处理等于号和感叹号
    public int handleEqualAndExclamatory(int i, String s,ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        if (c == '=') {
            str += c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":双目运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        } else {
            Data data = createDate(str, "运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":运算符" + "  token:" + operatorMap.get(str));
            return i;
        }
    }

    //处理减法运算符
    public int handleMinus(int i, String s,ObservableList<Data> list) {
        char c = text.charAt(++i);
        String str = s;
        if (c == '-') {
            str+=c;
            Data data = createDate(str, "自减运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":自减运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":自减运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        } else if (c == '=') {
            str += c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":双目运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        } else {
            Data data = createDate(str, "运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":运算符" + "  token:" + operatorMap.get(str));
            return i;
        }
    }

    //处理加法运算符
    public int handlePlus(int i,String s,ObservableList<Data> list){
        char c = text.charAt(++i);
        String str = s;
        if (c == '+') {
            str+=c;
            Data data = createDate(str, "自增运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":自增运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":自增运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        } else if (c == '=') {
            str += c;
            Data data = createDate(str, "双目运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":双目运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":双目运算符" + "  token:" + operatorMap.get(str));
            return ++i;
        } else {
            Data data = createDate(str, "运算符", operatorMap.get(str));
            list.add(data);
//            textArea.appendText(str + ":运算符" + "  token:" + operatorMap.get(str));
//            textArea.appendText("\n");
//            System.out.println(str + ":运算符" + "  token:" + operatorMap.get(str));
            return i;
        }
    }






}

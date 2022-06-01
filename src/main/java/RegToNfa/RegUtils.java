package RegToNfa;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * @author 阮启腾
 * @description  处理正规式的工具类
 * @date 2022/4/15,11:22
 */
public class RegUtils {


    /**
     *将中缀表达式变为后缀表达式
     * @param regExpression   正规式中缀表达式
     * @return    正规式后缀表达式
     */
    public static String infixReversePostfix(String regExpression) {
        String expression = addConnectionCharacter(regExpression);
        //存储后缀表达式
        StringBuilder postfix = new StringBuilder();
        //存储操作符的栈
        Stack<String> operatorStack = new Stack<>();
        //分割操作数和操作符
        StringTokenizer stringTokenizer = new StringTokenizer(expression, "()|.*", true);
        while (stringTokenizer.hasMoreTokens()) {
            //取出字符前后空格
            String str = stringTokenizer.nextToken().trim();
            switch (str) {
                /**
                 *
                 * 符号优先集："*" > "." > "|"
                 */
                case "|":
                    //优先级比"*"和"."小，所有弹出栈顶元素，直到遇到的元素大于等于本身
                    while (!operatorStack.isEmpty() && (operatorStack.peek().equals("*") || operatorStack.peek().equals("."))) {
                        postfix.append(operatorStack.pop());
                    }
                    operatorStack.push(str);
                    break;
                case ".":
                    while (!operatorStack.isEmpty() && operatorStack.peek().equals(".")) {
                        postfix.append(operatorStack.pop());
                    }
                    operatorStack.push(str);
                    break;
                case "*":
                    postfix.append(str);    //"*"优先级最高，直接添加
                    break;
                case "(":
                    operatorStack.push(str);   //遇到"("，可直接压栈
                    break;
                case ")":
                    while (!"(".equals(operatorStack.peek())) {
                        //当遇到")"时，将栈中的元素弹出并输出，直到遇到"("为止
                        postfix.append(operatorStack.pop());
                    }
                    operatorStack.pop();    //弹出"("括号
                    break;

                default:    //读到操作数，直接添加
                    postfix.append(str);
                    break;
            }
        }

        //将栈中剩余的符号加到后缀表达式上
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop());
        }
        return postfix.toString();
    }

    /**
     * 将正规式中省略的连接符加上去。即ab---->a.b
     *                            ab*----->a.b*
     *                            (a|b)*c------>(a|b)*.c等
     *
     * @return  添加连接符之后的表达式
     */
    public static String addConnectionCharacter(String regExpression){
        StringBuilder builder = new StringBuilder();
        //将正规式中所有的空格变为空
        char[] chars = regExpression.replaceAll(" ", "").toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (i == 0) {
                builder.append(chars[i]);   //第一个字符就直接添加
            } else if (chars[i] == '|' || chars[i] == '*' || chars[i] == ')') {
                builder.append(chars[i]);  //遇上“ | *  )” 则直接添加
            } else if (chars[i - 1] == '(' || chars[i - 1] == '|') {
                builder.append(chars[i]);  //前面一个字符是“ (  |” 则添加当前字符
            } else {
                builder.append('.').append(chars[i]);  //添加"."运算符
            }
        }
        return builder.toString();
    }

    /**
     * 获取字符串中的操作数且不重复  如："ab.*|()abc" ----->"a","b","c"
     * @param s   字符串
     * @return  返回字符串数组
     */
    public static String[] getSymbol(String s) {
        String[] strings = {"(", ")", "|", ".", "*"};
        for (String str : strings) {
            s = s.replace(str, "");   //将s中的符号都去除
        }
        char[] chars = s.toCharArray();
        Set<String> characterSet = new LinkedHashSet<>();
        for (char c : chars) {
            characterSet.add(String.valueOf(c));
        }
        String[] str = new String[characterSet.size()];
        for (int i = 0; i < characterSet.size(); i++) {
            str[i] = (String) characterSet.toArray()[i];
        }
        return str;
    }

}

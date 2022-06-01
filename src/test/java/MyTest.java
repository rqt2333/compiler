import RegToNfa.RegUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/15,12:29
 */
public class MyTest {
    @Test
    public void testAddConnectionCharacter(){
        String s = "(ab)*ab";
        String s1 = RegUtils.addConnectionCharacter(s);
        System.out.println(s1);
    }
    @Test
    public void test(){
        String s = "(a.b)*a.b";
        StringTokenizer tokenizer = new StringTokenizer(s, "()|.*", true);
        while (tokenizer.hasMoreTokens()) {
            String s1 = tokenizer.nextToken();
            System.out.println(s1);
        }
    }

    @Test
    public void testInfixReversePostfix() {
        String s = "(ab)*|c";
        String s1 = RegUtils.infixReversePostfix(s);
        System.out.println(s1);
    }

    @Test
    public void testGetSymbol(){
        String s = "ab.*|()abc";
        String[] symbol = RegUtils.getSymbol(s);
        for (String s1 : symbol) {
            System.out.println(s1);
        }
    }

    @Test
    public void test1(){
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("1");
        objects.add("2");
        objects.add("3");
        ArrayList<Object> objects1 = new ArrayList<>(objects.subList(0, objects.size()));
        System.out.println(objects1);
    }
}

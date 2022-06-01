package syntax;

import java.util.Stack;

/**
 * @author 阮启腾
 * @description
 * @date 2022/5/15,0:34
 */
public class TreeNodeStack<T> extends Stack<T> {

    @Override
    public String toString(){

        StringBuilder result = new StringBuilder();
        this.forEach( s -> result.append(" ").append(((TreeNode)s).getName()));
        return result.toString();
    }

}

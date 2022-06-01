package syntax;

import java.util.List;

/**
 * @author 阮启腾
 * @description  节点类，用于语法分析定义父子节点,为了画图方便
 * @date 2022/5/14,16:19
 */
public class TreeNode {
    //节点名称
    private String name;
    //子节点链表
    private List<TreeNode> children;

    private int nodeNumber ;

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public TreeNode(String name, List<TreeNode> children) {
        this.name = name;
        this.children = children;
    }

    public TreeNode(String name, int nodeNumber) {
        this.name = name;
        this.nodeNumber = nodeNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    @Override
    public String toString() {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("name", name);
//            jsonObject.put("children", children);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return name + "->" + children;
        return name ;
    }

    //    @Override
//    public String toString(){
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("name", name);
//            jsonObject.put("children", children);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        //去掉转义字符
//        return StringEscapeUtils.unescapeJava(jsonObject.toString());
//    }


}

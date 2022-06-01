package syntax;

import Graph.GraphUtil;
import javafx.scene.control.TextArea;
import lexer.FinalAttribute;
import lexer.Word;

import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author 阮启腾
 * @description 语法分析类
 * @date 2022/5/14,23:27
 */
public class SyntaxAnalysis {
    private static ArrayList<TreeNode> arrayList = new ArrayList<>();
    private static String treeNodeContent = "digraph G {" +  "\nnode [ fontcolor=blue ]\n";
    private static String T = "";

    public static void init() {
        //读取文法
//        String content = ReadAndWriteFile.readFileContent("src/main/resources/grammar/C语言官方文法.txt");
//        String content = ReadAndWriteFile.readFileContent("src/main/resources/grammar/TestGrammar.txt");
//        String content = ReadAndWriteFile.readFileContent("src/main/resources/grammar/MyGrammar.txt");
        String content = ReadAndWriteFile.readFileContent("src/main/resources/grammar/MyGrammar1.txt");
        Map<String, List<List<String>>> grammarHandleList = new GrammaticalHandle(content).grammarHandle();
        System.out.println("文法如下：");
        for (String s : grammarHandleList.keySet()) {
            System.out.println(s + "  ==>  " + grammarHandleList.get(s));
        }
        System.out.println("========================");
        System.out.println();

        //注入文法
        FinalAttribute.setAllGrammar(new AllGrammar(grammarHandleList));

        firstAndFollowAndSelect();

        makePredict();


    }

    private static void firstAndFollowAndSelect() {
        //注入first集
        FirstSet firstSet = new FirstSet();
        Map<String, Set<String>> firstSetFirstSet = firstSet.getFirstSet();
        FinalAttribute.setFirstMap(firstSetFirstSet);
        System.out.println("first集如下:");
        firstSetFirstSet.forEach((k, v) -> {
            System.out.println(k + "  ==>  " + v);
        });
        System.out.println("========================");
        System.out.println();

        //注入follow集
        FollowSet followSet = new FollowSet();
        Map<String, Set<String>> followSetFollowSet = followSet.getFollowSet();
        FinalAttribute.setFollowMap(followSetFollowSet);
        System.out.println("follow集如下:");
        followSetFollowSet.forEach((k, v) -> {
            System.out.println(k + "  ==>  " + v);
        });
        System.out.println("========================");
        System.out.println();

        //注入select集
        SelectSet selectSet = new SelectSet();
        Map<List<List<String>>, Set<String>> selectSetSelectSet = selectSet.getSelectSet();
        FinalAttribute.setSelectMap(selectSetSelectSet);
        System.out.println("select集如下:");
        selectSetSelectSet.forEach((k, v) -> {
            System.out.println(k + "  ==>  " + v);
        });
        System.out.println("========================");
        System.out.println();

        //注入非终结符和终结符
        Map<String, List<List<String>>> grammarMap = FinalAttribute.getAllGrammar().getGrammarMap();
        grammarMap.forEach((k, v) -> {
            FinalAttribute.getAllVn().add(k);
            for (List<String> stringList : v) {
                FinalAttribute.getAllVt().addAll(stringList);
            }
        });
        FinalAttribute.getAllVt().removeAll(FinalAttribute.getAllVn());
        FinalAttribute.getAllVt().remove("ε");

    }

    private static void makePredict() {
        Predict predict = new Predict(FinalAttribute.getSelectMap(), FinalAttribute.getFollowMap(), FinalAttribute.getAllGrammar());
        List<Object> objects = predict.predictTableForSyntaxOrSemantic();
        LinkedHashMap<String, LinkedHashMap<String, List<TreeNode>>> predictTableForSyntax = (LinkedHashMap<String, LinkedHashMap<String, List<TreeNode>>>)objects.get(0);


        //注入预测分析表
        FinalAttribute.setPredictMap(predictTableForSyntax);
        System.out.println("预测分析表如下");
        predictTableForSyntax.forEach((k, v) -> {
            v.forEach((q, p) -> {
                System.out.println("非终结符：" + k + " 遇到终结符： " + q + " 时， 选择产生式:" + k + " >>> " + p);
            });
            System.out.println();
        });
        System.out.println("===================================");
        System.out.println();

    }

    //    语法分析
    public static void analysis(List<Word> words, Object... args) {
        int COUNT = 0;
        LinkedHashMap<String, LinkedHashMap<String, List<TreeNode>>> predictMap = FinalAttribute.getPredictMap();
        TreeNodeStack<TreeNode> TreeNodeStack = new TreeNodeStack<>();
        TreeNodeStack<TreeNode> drawTreeNode = new TreeNodeStack<>();

        int IP = 0;  //输入指针
        int count = 0;
        boolean errorFlag = true;   //出错标记
        Word word = words.get(IP);
        //将#号入栈
        TreeNodeStack.push(new TreeNode("#", new ArrayList<>()));
        drawTreeNode.push(new TreeNode("#", COUNT++));

        //将开始符号入栈
        TreeNodeStack.push(new TreeNode((String) FinalAttribute.getAllGrammar().getGrammarMap().keySet().toArray()[0], new ArrayList<>()));
        drawTreeNode.push(new TreeNode((String) FinalAttribute.getAllGrammar().getGrammarMap().keySet().toArray()[0], COUNT++));

        System.out.println("栈中的情况：" + TreeNodeStack);
        System.out.println("剩余的符号串：" + words.subList(IP, words.size()));
        if (args.length > 0) {
            TextArea textArea = (TextArea) args[1];
            textArea.appendText("栈中的情况：" + TreeNodeStack + "\n");
            textArea.appendText("剩余的符号串：" + words.subList(IP, words.size()) + "\n");
        }
        //弹出栈顶元素
        TreeNode X = TreeNodeStack.pop();
        TreeNode pop = drawTreeNode.pop();
        treeNodeContent = treeNodeContent + pop.getNodeNumber() + "[label=" + pop.getName() + "]\n";
        //按照书p112的算法
        while (!X.getName().equals("#")) {
            if (X.getName().equals(word.getName())) {
                IP++;
                word = words.get(IP);
            } else if (FinalAttribute.getAllVt().contains(X.getName())) {
                System.out.println("error: " + " position : (" + words.get(IP - 1).getRow() + "," + words.get(IP - 1).getCol() + ") 缺少 '" + X.getName() + "'\n");
                System.out.println("=================================");
                if (args.length > 0) {
                    TextArea textArea1 = (TextArea) args[0];
                    textArea1.appendText("error: " + " position : (" + words.get(IP - 1).getRow() + "," + words.get(IP - 1).getCol() + ") 缺少 '" + X.getName() + "'\n");
                }
                count++;
                errorFlag = false;
            } else if (FinalAttribute.getAllVn().contains(X.getName())) {
                //如果某个格子为空，则表示出错
                if (predictMap.get(X.getName()).get(word.getName()).size() == 0) {
                    System.out.println("遇到同步语法单元'synchronizingToken',从栈顶弹出非终结符：" + X.getName() + ",并进行下一步的分析");
                    if (args.length > 0) {
                        TextArea textArea = (TextArea) args[1];
                        textArea.appendText("遇到同步语法单元'synchronizingToken',从栈顶弹出非终结符：" + X.getName() + ",并进行下一步的分析" + "\n");
                        TextArea textArea1 = (TextArea) args[0];
                        textArea1.appendText("error: " + " position : (" + words.get(IP - 1).getRow() + "," + words.get(IP - 1).getCol() + ") 缺少 '" + X.getName() + "'\n");
                    }
                    count++;
                    errorFlag = false;
                    X = TreeNodeStack.pop();
                    continue;
                } else if (predictMap.get(X.getName()).get(word.getName()).get(0).getName().equals("synchronizingToken")) {
                    //遇到同步语法单元
                    System.out.println("遇到同步语法单元'synchronizingToken',从栈顶弹出非终结符：" + X.getName() + ",并进行下一步的分析");
                    if (args.length > 0) {
                        TextArea textArea = (TextArea) args[1];
                        textArea.appendText("遇到同步语法单元'synchronizingToken',从栈顶弹出非终结符：" + X.getName() + ",并进行下一步的分析" + "\n");
                        TextArea textArea1 = (TextArea) args[0];
                        textArea1.appendText("error: " + " position : (" + words.get(IP - 1).getRow() + "," + words.get(IP - 1).getCol() + ") 缺少 '" + X.getName() + "'\n");
                    }
                    count++;
                    errorFlag = false;
                    X = TreeNodeStack.pop();
                    continue;
                } else {
                    List<TreeNode> funRight = predictMap.get(X.getName()).get(word.getName());
                    List<TreeNode> makeJsons = new ArrayList<>(funRight.subList(0, funRight.size()));
                    if (FinalAttribute.getAllVn().contains(X.getName()) && makeJsons.size() == 0) {
                        funRight.add(new TreeNode("ε", new ArrayList<>()));
                    }
                    //将产生式反转
                    Collections.reverse(makeJsons);
                    for (TreeNode json : makeJsons) {
                        if (!json.getName().equals("ε")) {
                            if (!json.getName().equals("synchronizingToken")) {
                                TreeNodeStack.push(json);
                                json.setNodeNumber(COUNT++);
                                drawTreeNode.push(json);
                                if (isSepecial(json)) {
                                    treeNodeContent = treeNodeContent + json.getNodeNumber() + "[label=" + "\"" + json + "\"" + "]\n";

                                } else {
                                    treeNodeContent = treeNodeContent + json.getNodeNumber() + "[label=" + json + "]\n";

                                }

                                treeNodeContent = treeNodeContent + pop.getNodeNumber() + "->" + json.getNodeNumber() + "\n";

                            }
                        }
                    }
                }
            }
            if (predictMap.containsKey(X.getName())) {
                List<TreeNode> makeJsons = predictMap.get(X.getName()).get(word.getName());
                ArrayList<TreeNode> makeJsons1 = new ArrayList<>(makeJsons);
                Collections.reverse(makeJsons1);
                System.out.println("栈中的情况：" + TreeNodeStack + "，   选择的产生式：" + X.getName() + "===>" + makeJsons1);
                if (args.length > 0) {
                    TextArea textArea = (TextArea) args[1];
                    textArea.appendText("栈中的情况：" + TreeNodeStack + "，   选择的产生式：" + X.getName() + "===>" + makeJsons1 + "\n");
                }
            } else {
                if (args.length > 0) {
                    TextArea textArea = (TextArea) args[1];
                    textArea.appendText("弹出终结符：" + X.getName() + "\n");
                }
                System.out.println("弹出终结符：" + X.getName());
            }
            if (args.length > 0) {
                TextArea textArea = (TextArea) args[1];
//                textArea.appendText("栈中的情况："+TreeNodeStack+"\n");
                textArea.appendText("剩余符号串：" + words.subList(IP, words.size()) + "\n");
            }
//            System.out.println("栈中的情况："+TreeNodeStack);
            System.out.println("剩余符号串：" + words.subList(IP, words.size()));
            //弹出栈顶符号
            X = TreeNodeStack.pop();
            pop = drawTreeNode.pop();
            System.out.println("弹出栈顶元素：" + X);
        }
        if (words.size() - 1 != IP || !errorFlag) {
            if (args.length > 0) {
                TextArea textArea = (TextArea) args[1];
                textArea.appendText("该语句不符合该文法\n");
            }
            System.out.println("该语句不符合该文法");
        } else {
            if (args.length > 0) {
                TextArea textArea = (TextArea) args[1];
                textArea.appendText("该语句符合该文法\n");
            }
            System.out.println("该语句符合该文法");
        }
        if (count == 0) {
            if (args.length > 0) {
                TextArea textArea1 = (TextArea) args[0];
                textArea1.appendText("没有出现错误\n");
            }
        }
        treeNodeContent = treeNodeContent  + "}";

    }

    public static List<TreeNode> remove(TreeNode json) {
        List<TreeNode> children = json.getChildren();
        if (json.getChildren().size() != 0) {
            for (TreeNode makeJson : children) {
                List<TreeNode> remove = remove(makeJson);
                arrayList.addAll(remove);
            }
        }
        ArrayList<TreeNode> list = new ArrayList<>();
        list.add(json);
        List<TreeNode> nodeList = new ArrayList<>(list);
        System.out.println(json + "->" + json.getChildren());
        return nodeList;
    }

    public static void drawTree() {
        GraphUtil graphUtil = new GraphUtil(); //new一个自己写的GraphUtil类
        graphUtil.setSourcePath("D:\\computer_study\\Java\\java_project\\compiler\\src\\main\\resources\\graph\\grammarTree.txt"); //设置dot指令的txt文件目录
        graphUtil.setTargetPath("D:\\computer_study\\Java\\java_project\\compiler\\src\\main\\resources\\graph\\grammarTree.pdf"); //设置要生成的图片的目录路径
        GraphUtil.saveCodeToFile(graphUtil.getSourcePath(), treeNodeContent);
        try {
            GraphUtil.genAndOpenGraph(graphUtil.getSourcePath(), graphUtil.getTargetPath()); //生成图片 并自动打开图片文件
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isSepecial(TreeNode treeNode) {
        String[] delimiter = FinalAttribute.getDelimiter();
        String[] operator = FinalAttribute.getOperator();
        for (String s : delimiter) {
            if (treeNode.getName().equals(s)) {
                return true;
            }
        }
        for (String s : operator) {
            if (treeNode.getName().equals(s)) {
                return true;
            }
        }
        return false;
    }
}

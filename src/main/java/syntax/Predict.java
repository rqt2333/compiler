package syntax;

import java.util.*;

/**
 * @author 阮启腾
 * @description   预测类，用于构造预测分析表
 * @date 2022/5/13,17:20
 */
public class Predict {
    public Map<List<List<String>>, Set<String>> selectSet;
    public     Map<String, Set<String>> followSet;
    public AllGrammar allGrammar;

    public Predict(Map<List<List<String>>, Set<String>> selectSet, Map<String, Set<String>> followSet, AllGrammar allGrammar) {
        this.selectSet = selectSet;
        this.followSet = followSet;
        this.allGrammar = allGrammar;
    }

    public List<Object> predictTableForSyntaxOrSemantic() {

        List<Object> objects = new ArrayList<>();

        //预测分析表
        /**
         *   外层map键是非终结符，即产生式的左部，值是一个内层的map
         *   内层map中的键是终结符，值是产生式的右部的集合，
         *   这张表中存储了每个非终结符和每个终结符所对应的产生式，可以想象为一张二维表，
         *   每个非终结符为一行，每个终结符为一列，两者相交的格子就储存所选用的产生式
         */
        LinkedHashMap<String, LinkedHashMap<String, List<TreeNode>>> predictLinkedHashMapForSyntax = new LinkedHashMap<>();

        LinkedHashMap<String, LinkedHashMap<String, List<String>>> predictLinkedHashMapForSemantic = new LinkedHashMap<>();


        //所有的非终结符
        Set<String> allVn = new LinkedHashSet<>();
        //所有的终结符
        Set<String> allVt = new LinkedHashSet<>();

        allGrammar.getGrammarMap().forEach((k, v) -> {
            allVn.add(k);
            for (List<String> stringList : v) {
                allVt.addAll(stringList);
            }
        });
        allVt.removeAll(allVn);
        allVt.remove("ε");   //取出终结符中的ε
        allVt.add("#");    //向终结符中加入#号

        LinkedHashMap<String, List<TreeNode>> makeJsonLinkedHashMap;

        //创建预测分析二维表
        for (String vn : allVn) {
            if (predictLinkedHashMapForSyntax.get(vn) == null) {
                makeJsonLinkedHashMap = new LinkedHashMap<>();
            } else {
                makeJsonLinkedHashMap = predictLinkedHashMapForSyntax.get(vn);
            }
            for (String vt : allVt) {
                //将所有的终结符和对应的所有json节点加进来，此时右部未知
                makeJsonLinkedHashMap.put(vt, new LinkedList<>());
            }
            predictLinkedHashMapForSyntax.put(vn, makeJsonLinkedHashMap);
        }

        //构造语义分析二维
        LinkedHashMap<String, List<String>> stringLinkedHashMap;
        for (String vn : allVn) {
            if (predictLinkedHashMapForSemantic.get(vn) == null) {
                stringLinkedHashMap = new LinkedHashMap<>();
            } else {
                stringLinkedHashMap = predictLinkedHashMapForSemantic.get(vn);
            }
            for (String vt : allVt) {
                stringLinkedHashMap.put(vt, new LinkedList<>());
            }
            predictLinkedHashMapForSemantic.put(vn, stringLinkedHashMap);
        }

        //向预测分析表中填入产生式
        selectSet.forEach((k, v) -> {
            //对于所有的终结符
            for (String s : v) {
                if (!s.equals("ε")) {
                    //得到产生式左部，即非终结符
                    String notTerminal = k.get(0).get(0);
                    //获得非终结符和终结符相交的产生式右部集合
                    List<TreeNode> makeJsons = predictLinkedHashMapForSyntax.get(notTerminal).get(s);
                    //若这个集合为空
                    if (makeJsons.size() == 0) {
                        for (String right : k.get(1)) {
                            makeJsons.add(new TreeNode(right, new ArrayList<>()));
                            predictLinkedHashMapForSemantic.get(k.get(0).get(0)).get(s).add(right);
                        }
                    }
                } else {
                    String notTerminal = k.get(0).get(0);
                    //得到follow集中的终结符
                    Set<String> set = followSet.get(notTerminal);
                    for (String str : set) {
                        List<TreeNode> makeJsons = predictLinkedHashMapForSyntax.get(notTerminal).get(str);
                        if (makeJsons.size() == 0) {
                            makeJsons.add(new TreeNode("ε", new ArrayList<>()));
                            predictLinkedHashMapForSemantic.get(k.get(0).get(0)).get(str).add("ε");
                        }
                    }
                }
            }
        });

        //将同步词法单元加入预测分析表中
        selectSet.forEach((k, v) -> {
            for (String s : v) {
                if (!s.equals("ε")) {
                    for (String terminal : followSet.get(k.get(0).get(0))) {
                        if (predictLinkedHashMapForSyntax.get(k.get(0).get(0)).get(terminal).size() == 0) {
                            predictLinkedHashMapForSyntax.get(k.get(0).get(0)).get(terminal).add(new TreeNode("synchronizingToken", new ArrayList<>()));
                            predictLinkedHashMapForSemantic.get(k.get(0).get(0)).get(terminal).add("synchronizingToken");
                        }
                    }
                }
            }
        });

//        System.out.println("语义分析表如下：");
//        predictLinkedHashMapForSemantic.forEach((k, v) -> {
//            System.out.println(k);
//            System.out.println(v);
//            System.out.println("===============");
//        });

        objects.add(predictLinkedHashMapForSyntax);
        objects.add(predictLinkedHashMapForSemantic);
        return objects;
    }


//    public LinkedHashMap<String, LinkedHashMap<String, List<String>>> predictTableForSemantic() {
//
//        LinkedHashMap<String, LinkedHashMap<String, List<TreeNode>>> predictLinkedHashMapForSyntax = new LinkedHashMap<>();
//
//        LinkedHashMap<String, LinkedHashMap<String, List<String>>> predictLinkedHashMapForSemantic = new LinkedHashMap<>();
//
//
//        //所有的非终结符
//        Set<String> allVn = new LinkedHashSet<>();
//        //所有的终结符
//        Set<String> allVt = new LinkedHashSet<>();
//
//        allGrammar.getGrammarMap().forEach((k, v) -> {
//            allVn.add(k);
//            for (List<String> stringList : v) {
//                allVt.addAll(stringList);
//            }
//        });
//        allVt.removeAll(allVn);
//        allVt.remove("ε");   //取出终结符中的ε
//        allVt.add("#");    //向终结符中加入#号
//
//        LinkedHashMap<String, List<TreeNode>> makeJsonLinkedHashMap;
//
//        //创建预测分析二维表
//        for (String vn : allVn) {
//            if (predictLinkedHashMapForSyntax.get(vn) == null) {
//                makeJsonLinkedHashMap = new LinkedHashMap<>();
//            } else {
//                makeJsonLinkedHashMap = predictLinkedHashMapForSyntax.get(vn);
//            }
//            for (String vt : allVt) {
//                //将所有的终结符和对应的所有json节点加进来，此时右部未知
//                makeJsonLinkedHashMap.put(vt, new LinkedList<>());
//            }
//            predictLinkedHashMapForSyntax.put(vn, makeJsonLinkedHashMap);
//        }
//
//        //构造语义分析二维
//        LinkedHashMap<String, List<String>> stringLinkedHashMap;
//        for (String vn : allVn) {
//            if (predictLinkedHashMapForSemantic.get(vn) == null) {
//                stringLinkedHashMap = new LinkedHashMap<>();
//            } else {
//                stringLinkedHashMap = predictLinkedHashMapForSemantic.get(vn);
//            }
//            for (String vt : allVt) {
//                stringLinkedHashMap.put(vt, new LinkedList<>());
//            }
//            predictLinkedHashMapForSemantic.put(vn, stringLinkedHashMap);
//        }
//
//        //向预测分析表中填入产生式
//        selectSet.forEach((k, v) -> {
//            //对于所有的终结符
//            for (String s : v) {
//                if (!s.equals("ε")) {
//                    //得到产生式左部，即非终结符
//                    String notTerminal = k.get(0).get(0);
//                    //获得非终结符和终结符相交的产生式右部集合
//                    List<TreeNode> makeJsons = predictLinkedHashMapForSyntax.get(notTerminal).get(s);
//                    //若这个集合为空
//                    if (makeJsons.size() == 0) {
//                        for (String right : k.get(1)) {
//                            predictLinkedHashMapForSemantic.get(k.get(0).get(0)).get(s).add(right);
//
//                        }
//                    }
//                } else {
//                    String notTerminal = k.get(0).get(0);
//                    //得到follow集中的终结符
//                    Set<String> set = followSet.get(notTerminal);
//                    for (String str : set) {
//                        List<TreeNode> makeJsons = predictLinkedHashMapForSyntax.get(notTerminal).get(str);
//                        if (makeJsons.size() == 0) {
//                            predictLinkedHashMapForSemantic.get(k.get(0).get(0)).get(str).add("ε");
//
//
//                        }
//                    }
//                }
//            }
//        });
//
//        //将同步词法单元加入预测分析表中
//        selectSet.forEach((k, v) -> {
//            for (String s : v) {
//                if (!s.equals("ε")) {
//                    for (String terminal : followSet.get(k.get(0).get(0))) {
//                        if (predictLinkedHashMapForSyntax.get(k.get(0).get(0)).get(terminal).size() == 0) {
//                            predictLinkedHashMapForSemantic.get(k.get(0).get(0)).get(terminal).add("synchronizingToken");
//
//                        }
//                    }
//                }
//            }
//        });
//        return predictLinkedHashMapForSemantic;
//    }


    
    




















}

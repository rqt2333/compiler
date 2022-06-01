package MinDfa;

import NfaToDfa.*;
import RegToNfa.*;

import java.util.*;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/17,15:14
 */
public class MinDFA {
    private int cnt = 0;		// 维护Group的唯一ID
    private final DFA dfa;
    private final List<Edge> edgeList;    //新构成的DFA的弧集合
    private Set<String> endStateSet = new LinkedHashSet<>();   //保存最小化DFA的终态

    public MinDFA(DFA dfa, List<Edge> edgeList) {
        this.dfa = dfa;
        this.edgeList = edgeList;
    }

    public Set<String> getEndStateSet() {
        return endStateSet;
    }

    public void setEndStateSet(Set<String> endStateSet) {
        this.endStateSet = endStateSet;
    }

    public Set<Group> getMinDFA(){
        List<Integer> stateSet = dfa.getStateSet();   //获得状态集合
        List<Integer> endStateSet = dfa.getEndStateSet();   //获得终态集合
        char[] letters = dfa.getLetters();    //获得字母表
        String[][] function = dfa.getFunction();
        stateSet.removeAll(endStateSet);   //   全部状态集-终态集=非终态集
        Group notFinalStateGroup = new Group(cnt++, new LinkedHashSet<>(stateSet));   //非终态集分组
        Group finalStateGroup = new Group(cnt++, new LinkedHashSet<>(endStateSet));   //终态集分组
        Set<Group> finalGroupSet = new LinkedHashSet<>();             // 最终分组
        Set<Group> curGroupSet;               // 此时的分组

        finalGroupSet.add(notFinalStateGroup);
        finalGroupSet.add(finalStateGroup);
        for (char letter : letters) {                                 // 对于每个字母
            curGroupSet = finalGroupSet;                            // 【最终分组】不断沦为【此时分组】
            finalGroupSet = separate(curGroupSet, letter, function);       // 【此时分组】又分裂成新的【最终分组】
        }                                                           // 所有字母都用了一次后，成为名副其实的【最终分组】

        return finalGroupSet;
    }

    /**
     *
     * @param state   当前状态
     * @param letter   字符
     * @param function  转换函数
     * @return   返回当前状态接收一个字符后的状态，如果没有，则返回-1
     */
    public int move(int state, char letter, String[][] function) {
        for (int nexState = 0; nexState < function.length; nexState++) {
            char[] chars = function[state][nexState].toCharArray();
            for (char c : chars) {
                if (c == letter) {
                    return nexState;
                }
            }
        }
        return -1;
    }

    /**
     *
     * @param state  当前状态
     * @param letter   字符
     * @param function  转换函数
     * @param groupSet  分组集合
     * @return   返回某状态(state)经过字母(letter)一次转化(move)后，所属于的当前分组(group)
     */
    public Group belong(int state, char letter, String[][] function, Set<Group> groupSet) {
        int move = move(state, letter, function);
        for (Group group : groupSet) {
            if (group.getStateSet().contains(move)) {
                return group;
            }
        }
        return null;
    }

    /**
     *
     * @param groupSet  分组合集
     * @param letter  字符
     * @param function 转换函数
     * @return
     */
    public Set<Group> separate(Set<Group> groupSet, char letter, String[][] function) {
        //最终的分组
        Set<Group> finalGroupSet = new LinkedHashSet<>();
        Queue<Group> queue = new LinkedList<>(groupSet);

        while (!queue.isEmpty()) {   //当队列不为空时
            Group oldGroup = queue.poll();   //弹出队头元素
            Map<Group, List<Integer>> map = new LinkedHashMap<>();  //根据指向的组，对状态Integer进行分类
            //根据状态遇到输入进行分组
            for (Integer state : oldGroup.getStateSet()) {
                Group belongGroup = belong(state, letter, function, groupSet);
                if (!map.containsKey(belongGroup)) {
                    map.put(belongGroup, new ArrayList<>());
                }
                map.get(belongGroup).add(state);
            }
            if (map.size() == 1) {   //如果这些状态映射到了一个状态集(Group)中，则为最终分组
                finalGroupSet.add(oldGroup);
            } else {
                groupSet.remove(oldGroup); // 如果这些状态映射到了多个状态集(Group)中，则删除原先分组，创建多个新分组，并将新分组入队
                for(List<Integer> list : map.values()){
                    Group newGroup = new Group(cnt++, new LinkedHashSet<>(list));
                    groupSet.add(newGroup);
                    queue.add(newGroup);
                }
            }
        }
        return finalGroupSet;   //返回最终分组
    }

    public List<Edge> getEdgeList(Set<String> oldEndStateSet) {
        Set<Group> set = getMinDFA();
        Set<Edge> newMinDfaEdgeList = new LinkedHashSet<>();
        Map<String,Object> stringMap = new HashMap();
        Edge edge;
        System.out.println("状态集合分组如下：");
        for (Group group : set) {
            System.out.println(group);
            for (Integer state : group.getStateSet()) {
                for (Edge newDfaEdge : edgeList) {
//                    if (newDfaEdge.getBegin().equals(String.valueOf(state))){
//                        Edge edge = new Edge(String.valueOf(group.getGroupID()), find(set, newDfaEdge.getEnd()), newDfaEdge.getKey());
//                        newMinDfaEdgeList.add(edge);
//                    }
//                    if (newDfaEdge.getBegin().equals(String.valueOf(state))) {
//                        String finalID = find(set, newDfaEdge.getEnd());  //重命名后的弧的终端
//                        Edge edge = new Edge(String.valueOf(group.getGroupID()), finalID, newDfaEdge.getKey());
//                        newMinDfaEdgeList.add(edge);
//                    }
                    if (newDfaEdge.getBegin().equals(String.valueOf(state))){
                        String groupID = String.valueOf(group.getGroupID());
                        String finalID = find(set, newDfaEdge.getEnd());
                        String key = newDfaEdge.getKey();
                        String s = groupID + key + finalID;
                        if (!stringMap.containsKey(s)) {
                            stringMap.put(s, null);
                            edge = new Edge(groupID, finalID, key);
                            newMinDfaEdgeList.add(edge);
                        }
                    }
                }
                for (String endState : oldEndStateSet) {
                    if (endState.equals(String.valueOf(state))) {
                        endStateSet.add(String.valueOf(group.getGroupID()));
                    }
                }
            }
        }
        System.out.println("============================");
        return new ArrayList<>(newMinDfaEdgeList);
    }

    public String find(Set<Group> set, String s){
        for (Group group : set){
            if (group.getStateSet().contains(Integer.valueOf(s))){
                return String.valueOf(group.getGroupID());
            }
        }
        return null;
    }


}

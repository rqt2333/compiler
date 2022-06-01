package MinDfa;

import java.util.Set;

/**
 * @author 阮启腾
 * @description   //实现NFA最小化的分组
 * @date 2022/4/17,17:00
 */
public class Group {
    private int groupID;   //该组的唯一ID
    private Set<Integer> stateSet;    //该组包含的状态集

    public Group(int groupID, Set<Integer> stateSet) {
        this.groupID = groupID;
        this.stateSet = stateSet;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public Set<Integer> getStateSet() {
        return stateSet;
    }

    public void setStateSet(Set<Integer> stateSet) {
        this.stateSet = stateSet;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupID=" + groupID +
                ", stateSet=" + stateSet +
                '}';
    }
}

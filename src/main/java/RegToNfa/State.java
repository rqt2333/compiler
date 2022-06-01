package RegToNfa;

/**
 * @author 阮启腾
 * @description
 * @date 2022/4/15,11:09
 */
public class State {
    //状态
    private String stateName;
    public static int ID = 0;

    public State() {
        //每新增一个状态就加一
        this.stateName = String.valueOf(ID++);
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public static int getID() {
        return ID;
    }

    public static void setID(int ID) {
        State.ID = ID;
    }

    @Override
    public String toString() {
        return stateName;
    }
}

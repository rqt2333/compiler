package lexer;

/**
 * @author 阮启腾
 * @description
 * @date 2022/5/10,16:18
 */
public class MyProject {
    private static final long serialVersionUID = 5160728458299623666L;
    /**
     * 工程名称
     */
    private String name;
    /**
     * 工程路径（在本电脑的路径）
     */
    private String path;
    private boolean showTreeView = false;

    public MyProject(String name, String path){
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getShowTreeView() {
        return showTreeView;
    }

    public void setShowTreeView(boolean showTreeView) {
        this.showTreeView = showTreeView;
    }
}

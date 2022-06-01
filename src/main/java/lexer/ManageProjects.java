package lexer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 阮启腾
 * @description
 * @date 2022/5/10,16:19
 */
public class ManageProjects {
    public static Map<String,MyProject> ProjectsMap = new LinkedHashMap<>(); //保存所有工程

    public static void setProjectsMap(LinkedHashMap<String,MyProject> projectsMap) {
        ProjectsMap = projectsMap;
    }

    public static Map<String,MyProject> getProjectsMap() {
        return ProjectsMap;
    }

    public static void delProject(MyProject myProject){
        ProjectsMap.remove(myProject.getPath()+myProject.getName());
    }

}

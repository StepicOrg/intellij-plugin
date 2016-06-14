package main.actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import main.stepicConnector.NewProjectService;

import java.util.HashSet;
import java.util.Set;

public class Utils {
    public static void refreshFiles(Project project){
        NewProjectService projectService = NewProjectService.getInstance(project);

        LocalFileSystem lfs = LocalFileSystem.getInstance();
        Set<String> set = projectService.getMapPathInfo().keySet();
        Set<String> removed = new HashSet<>();
        set.forEach(x -> {
            if (lfs.findFileByPath(x) == null)
                removed.add(x);
        });

        projectService.removeAll(removed);
        set.removeAll(removed);
    }
}

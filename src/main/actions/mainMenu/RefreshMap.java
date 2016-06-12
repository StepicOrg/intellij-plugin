package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import main.stepicConnector.StepicProjectService;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Petr on 03.06.2016.
 */
public class RefreshMap extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        StepicProjectService projectService = StepicProjectService.getInstance(project);

        LocalFileSystem lfs = LocalFileSystem.getInstance();
        Set<String> set = projectService.getMapPathStep().keySet();
        Set<String> removed = new HashSet<>();
        set.forEach(x -> {
            if (lfs.findFileByPath(x) == null)
                removed.add(x);
        });

        projectService.removeAll(removed);
//        set.removeAll(removed);

    }
}

package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import main.Utils;

/**
 * Created by Petr on 03.06.2016.
 */
public class RefreshMap extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        Utils.refreshFiles(project);
    }
}

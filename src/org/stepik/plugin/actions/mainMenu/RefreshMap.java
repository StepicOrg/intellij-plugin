package org.stepik.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.stepik.plugin.utils.Utils;

public class RefreshMap extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        Utils.refreshFiles(project);
    }
}

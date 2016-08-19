package org.stepik.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.stepik.plugin.stepikConnector.StepikConnector;

public class WhoAmI extends MainMenuAction {
    private static final Logger LOG = Logger.getInstance(AnAction.class);

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getData(PlatformDataKeys.PROJECT);
        String login = StepikConnector.getLogin(project);
        String name = null;
        name = StepikConnector.getUserName(project);
        Messages.showMessageDialog(project, "Hello, " + name + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());

    }
}

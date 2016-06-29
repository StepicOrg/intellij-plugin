package org.stepic.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.stepic.plugin.stepicConnector.StepicConnector;

public class WhoAmI extends MainMenuAction {
    private static final Logger LOG = Logger.getInstance(AnAction.class);

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getData(PlatformDataKeys.PROJECT);
        String login = StepicConnector.getLogin(project);
        String name = null;
        name = StepicConnector.getUserName(project);
        Messages.showMessageDialog(project, "Hello, " + name + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());

    }
}

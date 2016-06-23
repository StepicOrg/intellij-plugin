package org.stepic.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.stepic.plugin.stepicConnector.StepicConnector;

public class SignIn extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        String login =
                Messages.showInputDialog(project, "Please, input your E-mail", "Sing in", Messages.getQuestionIcon());
        String password =
                Messages.showPasswordDialog(project, "Please, input your Password", "Sing in", Messages.getQuestionIcon());

        StepicConnector.setLoginAndPassword(login, password, project);
        StepicConnector.initToken(e.getProject());
        String name = null;
        name = StepicConnector.getUserName(project);

        Messages.showMessageDialog(project, "Hello, " + name + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
    }
}

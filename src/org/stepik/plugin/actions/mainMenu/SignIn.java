package org.stepik.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.stepik.plugin.stepikConnector.StepikConnector;

public class SignIn extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        String login =
                Messages.showInputDialog(project, "Please, input your E-mail", "Sing in", Messages.getQuestionIcon());
        String password =
                Messages.showPasswordDialog(project, "Please, input your Password", "Sing in", Messages.getQuestionIcon());

        StepikConnector.setLoginAndPassword(login, password, project);
        StepikConnector.initToken(e.getProject());
        String name = StepikConnector.getUserName(project);

        Messages.showMessageDialog(project, "Hello, " + name + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
    }
}

package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.stepicConnector.StepicConnector;

public class SingIn extends MainMenuAction {

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
        try {
            name = StepicConnector.getUserName(project);
        } catch (UnirestException e1) {
            e1.printStackTrace();
        }

        Messages.showMessageDialog(project, "Hello, " + name + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
    }
}

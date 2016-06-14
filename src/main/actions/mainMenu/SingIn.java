package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import main.stepicConnector.StepicApplicationService;
import main.stepicConnector.StepicConnector;

/**
 * Created by Petr on 01.04.2016.
 */
public class SingIn extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        StepicApplicationService.getInstance().setLogin(
                Messages.showInputDialog(project, "Please, input your E-mail", "Sing in", Messages.getQuestionIcon()));
        StepicApplicationService.getInstance().setPassword(
                Messages.showPasswordDialog(project, "Please, input your Password", "Sing in", Messages.getQuestionIcon()));

        StepicConnector.initToken();
        String name = StepicConnector.getUserName();

        Messages.showMessageDialog(project, "Hello, " + name + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
    }
}

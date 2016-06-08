package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import main.stepicConnector.StepicConnector;
import main.stepicConnector.WorkerService;

/**
 * Created by Petr on 01.04.2016.
 */
public class SingIn extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        WorkerService.getInstance().setLogin(
                Messages.showInputDialog(project, "Please, input your username", "Sing in", Messages.getQuestionIcon()));
        WorkerService.getInstance().setPassword(
                Messages.showPasswordDialog(project, "Please, input your password", "Sing in", Messages.getQuestionIcon()));

        StepicConnector.initToken();
        String name = StepicConnector.getUserName();

        Messages.showMessageDialog(project, "Hello, " + name + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
    }
}

package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import main.stepicConnector.Commands;
import main.stepicConnector.WorkerService;

/**
 * Created by Petr on 22.03.2016.
 */
public class WhoAmI extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (WorkerService.getInstance().getClientId() == null) {
            Messages.showMessageDialog(project, "Sorry, you don't authorize. Please sing in", "Information", Messages.getInformationIcon());
        } else {
            Commands.initToken();
            String name = Commands.getUserName();
            Messages.showMessageDialog(project, "Hello, " + name + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
        }
    }
}

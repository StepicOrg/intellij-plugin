package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import main.stepicConnector.WorkerService;

/**
 * Created by Petr on 02.04.2016.
 */
public class ClientInfo extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Messages.showMessageDialog(project,
                "client_id = " + WorkerService.getInstance().getClientId()
                + "\nusername = " + WorkerService.getInstance().getUsername()
                , "Information", Messages.getInformationIcon());
    }
}

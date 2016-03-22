package actions;

import StepicConnector.WorkerService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * Created by Petr on 22.03.2016.
 */
public class WhoAmI extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        WorkerService ws = WorkerService.getInstance();
        if (ws.getClientId() == null) {
            ws.setClientId(Messages.showInputDialog(project, "Please,", "input clientId", Messages.getQuestionIcon()));
            ws.setClientSecret(Messages.showInputDialog(project, "Please,", "input clientSecret", Messages.getQuestionIcon()));
        }

        Messages.showMessageDialog(project, "Hello, " + ws.getClientId() + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());

    }
}

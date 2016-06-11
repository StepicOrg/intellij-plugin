package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import main.stepicConnector.ApplicationService;
import main.stepicConnector.StepicConnector;

/**
 * Created by Petr on 22.03.2016.
 */
public class WhoAmI extends MainMenuAction {
    private static final Logger LOG = Logger.getInstance(AnAction.class);

    @Override
    public void actionPerformed(AnActionEvent e) {

        LOG.warn("action");
        Project project = e.getData(PlatformDataKeys.PROJECT);
        if (ApplicationService.getInstance().getPassword() == null) {
            Messages.showMessageDialog(project, "Sorry, you don't authorize. Please sing in", "Information", Messages.getInformationIcon());
        } else {
//            StepicConnector.initToken();
            String name = StepicConnector.getUserName();
            Messages.showMessageDialog(project, "Hello, " + name + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
        }
    }
}

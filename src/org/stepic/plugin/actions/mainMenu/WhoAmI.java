package org.stepic.plugin.actions.mainMenu;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.stepic.plugin.stepicConnector.StepicConnector;

public class WhoAmI extends MainMenuAction {
    private static final Logger LOG = Logger.getInstance(AnAction.class);

    @Override
    public void actionPerformed(AnActionEvent e) {

        Project project = e.getData(PlatformDataKeys.PROJECT);
        String login  = StepicConnector.getLogin(project);
        String name = null;
        try {
            name = StepicConnector.getUserName(project);
            Messages.showMessageDialog(project, "Hello, " + name + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
        } catch (UnirestException e1) {
            Notification notification = new Notification("Who am i", "Stepic authorization error", "Please check your internet configuration.", NotificationType.ERROR);
            notification.notify(project);
        }

    }
}

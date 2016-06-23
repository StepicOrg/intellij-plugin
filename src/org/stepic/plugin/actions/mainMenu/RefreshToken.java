package org.stepic.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.stepic.plugin.stepicConnector.StepicConnector;

public class RefreshToken extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            StepicConnector.initToken(e.getProject());
        } catch (UnirestException e1) {
            StepicConnector.initConnectionError(e.getProject());
        }
    }
}

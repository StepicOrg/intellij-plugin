package org.stepic.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.stepic.plugin.stepicConnector.StepicConnector;

public class RefreshToken extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        StepicConnector.initToken(e.getProject());
    }
}

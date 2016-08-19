package org.stepik.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.stepik.plugin.stepikConnector.StepikConnector;

public class RefreshToken extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        StepikConnector.initToken(e.getProject());
    }
}

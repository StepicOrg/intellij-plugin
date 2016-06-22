package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import main.stepicConnector.StepicConnector;

public class RefreshToken extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        StepicConnector.initToken(e.getProject());
    }
}

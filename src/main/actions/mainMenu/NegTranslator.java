package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import main.stepicConnector.StepicProjectService;

/**
 * Created by Petr on 03.06.2016.
 */
public class NegTranslator extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        StepicProjectService ws = StepicProjectService.getInstance(e.getProject());
        ws.setTranslator(!ws.isTranslate());
        Messages.showMessageDialog(e.getProject(), Boolean.toString(ws.isTranslate()), "Information", Messages.getInformationIcon());
    }
}

package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import main.stepicConnector.NewProjectService;

/**
 * Created by Petr on 03.06.2016.
 */
public class NegTranslator extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        NewProjectService ws = NewProjectService.getInstance(e.getProject());

        ws.setTranslate(!ws.isTranslate());
        Messages.showMessageDialog(e.getProject(), Boolean.toString(ws.isTranslate()), "Information", Messages.getInformationIcon());
    }
}

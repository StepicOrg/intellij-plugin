package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import main.stepicConnector.WorkerService;

/**
 * Created by Petr on 03.06.2016.
 */
public class NegTranslator extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        WorkerService ws = WorkerService.getInstance();
        ws.setTranslator(!ws.isTranslate());
        Messages.showMessageDialog(e.getProject(), Boolean.toString(ws.isTranslate()), "Information", Messages.getInformationIcon());
    }
}

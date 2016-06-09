package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * Created by Petr on 09.06.2016.
 */
public class SomeAction extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Object navigatable = anActionEvent.getData(CommonDataKeys.NAVIGATABLE);
        if (navigatable != null) {
            Messages.showDialog(navigatable.toString(), "Selected Element: ", new String[]{"OK"}, -1, null);
        }
    }

    public void update(AnActionEvent anActionEvent) {
        final Project project = anActionEvent.getData(CommonDataKeys.PROJECT);
        if (project == null)
            return;
        Object navigatable = anActionEvent.getData(CommonDataKeys.NAVIGATABLE);
        anActionEvent.getPresentation().setEnabled(navigatable != null);
    }
}

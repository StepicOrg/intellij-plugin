package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import main.courseFormat.StepInfo;
import main.stepicConnector.NewProjectService;

import java.util.Map;

/**
 * Created by Petr on 21.05.2016.
 */
public class PrintMapInfo extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();

        NewProjectService ws = NewProjectService.getInstance(e.getProject());
        final String[] text = {""};

        Map<String, StepInfo> map = ws.getMapPathInfo();
        if (map == null) {
            Messages.showMessageDialog(project, "path--step is null", "Information", Messages.getInformationIcon());
        } else {
            map.keySet().forEach((x) -> text[0] += x + "\n");
            Messages.showMessageDialog(project, text[0], "Information", Messages.getInformationIcon());
        }
    }
}

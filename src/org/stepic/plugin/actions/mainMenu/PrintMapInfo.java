package org.stepic.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.stepic.plugin.modules.StepInfo;
import org.stepic.plugin.storages.CourseDefinitionStorage;

import java.util.Map;

public class PrintMapInfo extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();

        CourseDefinitionStorage ws = CourseDefinitionStorage.getInstance(e.getProject());
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

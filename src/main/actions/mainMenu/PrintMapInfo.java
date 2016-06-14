package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import main.edu.stepic.StepInfo;
import main.stepicConnector.NewProjectService;

import java.util.Map;

/**
 * Created by Petr on 21.05.2016.
 */
public class PrintMapInfo extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getProject();

        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);

//        StepicApplicationService ws = StepicApplicationService.getInstance();
        NewProjectService ws = NewProjectService.getInstance(e.getProject());
//        StepicProjectService ws = StepicProjectService.getInstance();
        final String[] text = {""};

        Map<String,StepInfo> map = ws.getMapPathInfo();
        if (map == null){
            Messages.showMessageDialog(project, "path--step is null", "Information", Messages.getInformationIcon());
        } else {
            map.keySet().forEach((x) -> text[0] += x + "\n");
            Messages.showMessageDialog(project, text[0], "Information", Messages.getInformationIcon());
        }


    }
}

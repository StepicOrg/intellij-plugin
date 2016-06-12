package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import main.stepicConnector.ProjectService;

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

//        ApplicationService ws = ApplicationService.getInstance();
        ProjectService ws = ProjectService.getInstance(e.getProject());
//        ProjectService ws = ProjectService.getInstance();
        final String[] text = {""};

        Map map = ws.getMapPathStep();
        if (map == null){
            Messages.showMessageDialog(project, "path--step is null", "Information", Messages.getInformationIcon());
        } else {
            map.keySet().forEach((x) -> text[0] += x + "\n");
            Messages.showMessageDialog(project, text[0], "Information", Messages.getInformationIcon());
        }


        text[0] = "";
        map = ws.getPathAttId();
        if (map == null){
            Messages.showMessageDialog(project, "Path--Id is null", "Information", Messages.getInformationIcon());
        } else {
            map.keySet().forEach((x) -> text[0] += x + "\n");
            Messages.showMessageDialog(project, text[0], "Information", Messages.getInformationIcon());
        }
    }
}

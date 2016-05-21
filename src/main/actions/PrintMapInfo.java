package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import main.stepicConnector.WorkerService;

/**
 * Created by Petr on 21.05.2016.
 */
public class PrintMapInfo extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getProject();

        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);

        WorkerService ws = WorkerService.getInstance();
        final String[] text = {""};

        ws.getMapPathStep().keySet().forEach((x) -> text[0] += x+"\n");
//        Messages.showMessageDialog(project, vf.getPath(), "Information", Messages.getInformationIcon());
        Messages.showMessageDialog(project, text[0], "Information", Messages.getInformationIcon());
    }
}

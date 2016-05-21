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
public class GetIdStep extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        WorkerService ws = WorkerService.getInstance();
        String stepId = ws.getStepId(vf.getPath());
        Messages.showMessageDialog(project, stepId, "Information", Messages.getInformationIcon());
    }
}

package main.actions.popupMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import main.stepicConnector.StepicProjectService;

/**
 * Created by Petr on 21.05.2016.
 */
public class GetIdStep extends PopupMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

//        StepicApplicationService ws = StepicApplicationService.getInstance();
        StepicProjectService ws = StepicProjectService.getInstance(project);
//        StepicProjectService ws = StepicProjectService.getInstance();
        String stepId = ws.getStepId(vf.getPath());
        Messages.showMessageDialog(project, stepId, "Information", Messages.getInformationIcon());
    }

}

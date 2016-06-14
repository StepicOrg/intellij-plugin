package main.actions.popupMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import main.edu.stepic.Submission;
import main.stepicConnector.NewProjectService;
import main.stepicConnector.StepicConnector;

import java.util.List;

/**
 * Created by Petr on 21.05.2016.
 */
public class GetStepStatus extends PopupMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        NewProjectService projectService = NewProjectService.getInstance(project);
        String stepId = projectService.getStepID(vf.getPath());

        String ans = "";
        int size = StepicConnector.getStatusTask(stepId, Pair.pair("status", "correct")).size();
        if (size > 0) {
            ans = "Step was solved";
        } else {
            ans = "Step wasn't solved";
        }

        String attemptId = projectService.getAttemptID(vf.getPath());
        if (!attemptId.equals("")) {
            List<Submission> list = StepicConnector.getStatusTask(stepId, Pair.pair("attempt", attemptId));
            if (!list.isEmpty()) {
                Submission max = list.get(0);
                for (Submission node : list) {
                    if (max.getTime().compareTo(node.getTime()) < 0){
                        max = node;
                    }
                }
                ans += "\nlast submission is " + max.getStatus();
            } else {
                ans += "\nlast submission from IDEA is unknown";
            }
        } else {
            ans += "\nlast submission from IDEA is unknown";
        }

        Messages.showMessageDialog(project, ans, "Information", Messages.getInformationIcon());
    }
}

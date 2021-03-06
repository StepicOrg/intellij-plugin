package org.stepik.plugin.actions.popupMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import org.stepik.plugin.stepikConnector.StepikConnector;
import org.stepik.plugin.storages.CourseDefinitionStorage;

public class GetStepStatus extends PopupMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        String path = vf.getPath();
        CourseDefinitionStorage projectService = CourseDefinitionStorage.getInstance(project);
        String stepId = projectService.getStepID(path);

        String ans;


        if (wasItSolved(stepId, project)) {
            ans = "Step was solved\n";
        } else {
            ans = "Step wasn't solved\n";
        }

        ans += "last submission from IDEA is ";
        String subID = projectService.getSubmissionID(path);
        if (subID.isEmpty()) {
            ans += "unknown";
        } else {
            ans += StepikConnector.getStatus(subID, project).get(0).getStatus();
        }


        Messages.showMessageDialog(project, ans, "Information", Messages.getInformationIcon());
    }

    private boolean wasItSolved(String stepID, Project project) {
        int size = StepikConnector.getStepStatus(stepID, Pair.pair("status", "correct"), project).size();
        return size > 0;
    }
}

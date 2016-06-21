package main.actions.popupMenu;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.stepicConnector.NewProjectService;
import main.stepicConnector.StepicConnector;
import main.stepicConnector.StudentService;

/**
 * Created by Petr on 21.05.2016.
 */
public class GetStepStatus extends PopupMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        String path = vf.getPath();
        NewProjectService projectService = NewProjectService.getInstance(project);
        StudentService studentService = StudentService.getInstance(project);
        String stepId = projectService.getStepID(path);

        String ans = "";


        Notification notification = null;
        try {
            if (wasItSolved(stepId, studentService.getToken())) {
                ans = "Step was solved\n";
            } else {
                ans = "Step wasn't solved\n";
            }

            String subID = projectService.getSubmissionID(path);
            if (subID.isEmpty()) {
                ans += "last submission from IDEA is unknown";
            } else {
                ans += StepicConnector.getStatus(subID, studentService.getToken()).get(0).getStatus();
            }

        } catch (UnirestException e1) {
            notification =
                    new Notification("Step.status", "Error", "Get Status error", NotificationType.ERROR);
            notification.notify(project);
            return;
        }

        Messages.showMessageDialog(project, ans, "Information", Messages.getInformationIcon());
    }

    private boolean wasItSolved(String stepID, String token) throws UnirestException {
        int size = StepicConnector.getStatusTask(stepID, Pair.pair("status", "correct"), token).size();
        if (size > 0) {
            return true;
        } else {
            return false;
        }
    }
}

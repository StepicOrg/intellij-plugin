package main.actions.popupMenu;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.courseFormat.Submission;
import main.stepicConnector.NewProjectService;
import main.stepicConnector.StepicConnector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Petr on 19.05.2016.
 */
public class SendFile extends PopupMenuAction {
    private static String success = "the Step successfully sent";
    private static String error = "the Step is not sent";

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        NewProjectService projectService = NewProjectService.getInstance(project);
        String stepId = projectService.getStepID(vf.getPath());

        String text = renameMainClass(vf);
        String path = vf.getPath();

        String attemptId;
        String submissionId;
        try {
            attemptId = StepicConnector.getAttemptId(stepId);
            submissionId = StepicConnector.sendFile(text, attemptId);

            projectService.setAttemptID(path, attemptId);
            projectService.setSubmissionID(path, submissionId);
            Messages.showMessageDialog(project, success, "Information", Messages.getInformationIcon());
        } catch (UnirestException e1) {
            Messages.showMessageDialog(project, error, "Error", Messages.getErrorIcon());
            return;
        }

        String filename = projectService.getFilename(path);
        final Application application = ApplicationManager.getApplication();
        final String finalSubmissionId = submissionId;
        application.executeOnPooledThread(
                new Runnable() {
                    @Override
                    public void run() {

                        String ans = "evaluation";
                        final int TIMER = 2;
                        int count = 0;
                        Notification notification = null;
                        List<Submission> list = null;
                        while (ans.equals("evaluation") && count < 100) {
                            try {
                                Thread.sleep(TIMER * 1000);          //1000 milliseconds is one second.
                                list = StepicConnector.getStatus(finalSubmissionId);
                                if (list != null)
                                    ans = list.get(0).getStatus();
                                count += TIMER;
                            } catch (InterruptedException | UnirestException | NullPointerException e1) {
                                notification = new Notification("Step.sending", "Error", "Get Status error", NotificationType.ERROR);
                                notification.notify(project);
                                return;
                            }
                        }

                        NotificationType notificationType;
                        if (ans.equals("correct")) {
                            notificationType = NotificationType.INFORMATION;
                        } else {
                            notificationType = NotificationType.WARNING;
                        }
                        notification = new Notification("Step.sending", "Step status", filename + " is " + ans, notificationType);
                        notification.notify(project);
                    }
                }

        );
    }

    private String renameMainClass(VirtualFile vf) {
        Document doc = FileDocumentManager.getInstance().getDocument(vf);
        String[] lines = doc.getText().split("\n");

        Set<Integer> skipLine = new HashSet<>();
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("package")) skipLine.add(i);
            if (lines[i].contains("class Step")) {
                lines[i] = "class Main {";
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            if (skipLine.contains(i)) continue;
            sb.append(lines[i] + "\n");
        }
        return sb.toString();
    }

    @Override
    public void setDefaultIcon(boolean isDefaultIconSet) {
        super.setDefaultIcon(isDefaultIconSet);
    }

}

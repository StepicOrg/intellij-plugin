package org.stepic.plugin.actions.popupMenu;

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
import org.stepic.plugin.modules.Submission;
import org.stepic.plugin.stepicConnector.StepicConnector;
import org.stepic.plugin.storages.CourseDefinitionStorage;
import org.stepic.plugin.utils.MyLogger;
import org.stepic.plugin.utils.NotificationUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SendFile extends PopupMenuAction {
    private static String success = "the Step successfully sent";
    private static String error = "the Step is not sent";

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        VirtualFile vf = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        CourseDefinitionStorage projectService = CourseDefinitionStorage.getInstance(project);
        String stepId = projectService.getStepID(vf.getPath());

        String text = renameMainClass(vf);
        String path = vf.getPath();

        String attemptId = StepicConnector.getAttemptId(stepId, project);
        String submissionId = StepicConnector.sendFile(text, attemptId, project);

        MyLogger.getInstance().getLOG().debug("attId = " + attemptId);
        MyLogger.getInstance().getLOG().debug("subId = " + submissionId);

        projectService.setAttemptID(path, attemptId);
        projectService.setSubmissionID(path, submissionId);
        Messages.showMessageDialog(project, success, "Information", Messages.getInformationIcon());

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
                        String b = "";
                        while (ans.equals("evaluation") && count < 100) {
                            try {
                                Thread.sleep(TIMER * 1000);          //1000 milliseconds is one second.
                                list = StepicConnector.getStatus(finalSubmissionId, project);
                                if (list != null) {
                                    ans = list.get(0).getStatus();
                                    b = list.get(0).getHint();
                                }
                                count += TIMER;
                            } catch (InterruptedException | NullPointerException e) {
                                notification = new Notification("Step.sending", "Error", "Get Status error", NotificationType.ERROR);
                                NotificationUtils.showNotification(notification, project);
                                return;
                            }
                        }

                        NotificationType notificationType;

                        if (ans.equals("correct")) {
                            notificationType = NotificationType.INFORMATION;
                            b = "Success!";
                        } else {
                            notificationType = NotificationType.WARNING;
                            b = b.split("\\.")[0];
                        }
                        notification = new Notification("Step.sending", filename + " is " + ans, b, notificationType);
                        NotificationUtils.showNotification(notification, project);
                    }
                }

        );
    }

    private String renameMainClass(VirtualFile vf) {
        Document doc = FileDocumentManager.getInstance().getDocument(vf);
        String[] lines = doc.getText().split("\n");

        Set<Integer> skipLine = new HashSet<>();
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("// Sent from IntelliJ IDEA")) skipLine.add(i);
            if (lines[i].contains("package")) skipLine.add(i);
            if (lines[i].contains("class Step")) {
                lines[i] = "class Main {";
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("// Sent from IntelliJ IDEA");
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

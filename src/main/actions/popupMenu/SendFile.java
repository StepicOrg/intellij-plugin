package main.actions.popupMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.stepicConnector.ProjectService;
import main.stepicConnector.StepicConnector;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Petr on 19.05.2016.
 */
public class SendFile extends PopupMenuAction {
    private static String success = "the Step successfully sent";
    private static String error = "the Step is not sent";

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        ProjectService projectService = ProjectService.getInstance(project);
        String stepId = projectService.getStepId(vf.getPath());

        String text = renameMainClass(vf);

        String attemptId = StepicConnector.getAttemptId(stepId);
        projectService.setAttemptId(vf.getPath(), attemptId);

        try {
            String submissionId = StepicConnector.sendFile(text, attemptId);
            Messages.showMessageDialog(project, success, "Information", Messages.getInformationIcon());
        } catch (UnirestException e1) {
            Messages.showMessageDialog(project, error, "Error", Messages.getErrorIcon());
        }
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

package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import main.stepicConnector.StepicConnector;
import main.stepicConnector.WS2;
import main.stepicConnector.WorkerService;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Petr on 19.05.2016.
 */
public class SendFile extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        StepicConnector.initToken();
        WorkerService ws = WorkerService.getInstance();
        String stepId = ws.getStepId(vf.getPath());

        String text = renameMainClass(vf);
        Messages.showMessageDialog(project, text, "Information", Messages.getInformationIcon());

        String attemptId = StepicConnector.getAttemptId(stepId);

        WS2 ws2 = WS2.getInstance();
        ws2.setAttemptId(stepId, attemptId);

        String submissionId = StepicConnector.sendFile(text, attemptId);
//        ws.set
//        String status = StepicConnector.getStatusTask(submissionId);
//        Messages.showMessageDialog(project, status, "Information", Messages.getInformationIcon());

    }

    private String renameMainClass(VirtualFile vf) {
        Document doc = FileDocumentManager.getInstance().getDocument(vf);
        String[] lines = doc.getText().split("\n");

//        int packLine = 0;
        Set<Integer> skipLine = new HashSet<>();
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("package")) skipLine.add(i);
            if (lines[i].contains("class Step")) {
                lines[i] = "class Main {";
//                startLine = i;
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

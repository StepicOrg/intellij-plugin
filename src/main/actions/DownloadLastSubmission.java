package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import main.stepicConnector.StepicConnector;
import main.stepicConnector.SubmissionsNode;
import main.stepicConnector.WS3;

import java.util.List;

/**
 * Created by Petr on 03.06.2016.
 */
public class DownloadLastSubmission extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        Document doc = FileDocumentManager.getInstance().getDocument(vf);


        List<SubmissionsNode> submissions =
                StepicConnector.getSubmissions(WS3.getInstance(project).getStepId(vf.getPath())).submissions;

        String code = submissions.get(submissions.size() -1 ).getCode();

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                doc.setText(code);
            }
        });

//        doc.setText(code);

    }
}

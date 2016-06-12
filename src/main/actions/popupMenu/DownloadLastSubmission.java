package main.actions.popupMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import main.edu.stepic.SubmissionsNode;
import main.stepicConnector.StepicConnector;
import main.stepicConnector.StepicProjectService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Petr on 03.06.2016.
 */
public class DownloadLastSubmission extends PopupMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        Document doc = FileDocumentManager.getInstance().getDocument(vf);


        List<SubmissionsNode> submissions =
                StepicConnector.getSubmissions(StepicProjectService.getInstance(project).getStepId(vf.getPath()));

        String code = submissions.get(submissions.size() - 1).getCode();

        String stepName = vf.getName().split("\\.")[0];
        String pack = StepicProjectService.getInstance(project).getPackage(vf.getPath());
//        MyLogger.getInstance().getLOG().warn("StepName =" + stepName);

        String code2 = renameFromMainToStep(code, stepName, pack);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                doc.setText(code2);
            }
        });

//        doc.setText(code);

    }

    private String renameFromMainToStep(String code, String stepName, String ppackage) {
        String[] lines = code.split("\n");

        Set<Integer> skipLine = new HashSet<>();
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("package")) skipLine.add(i);
            if (lines[i].contains("class Main")) {
                lines[i] = "class " + stepName + " {";
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("package " + ppackage + ";\n\n");
        for (int i = 0; i < lines.length; i++) {
            if (skipLine.contains(i)) continue;
            sb.append(lines[i] + "\n");
        }
        return sb.toString();
    }

}

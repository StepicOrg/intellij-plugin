package org.stepic.plugin.actions.popupMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.stepic.plugin.modules.Submission;
import org.stepic.plugin.stepicConnector.StepicConnector;
import org.stepic.plugin.storages.CourseDefinitionStorage;
import org.stepic.plugin.utils.NotificationTemplates;
import org.stepic.plugin.utils.NotificationUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DownloadLastSubmission extends PopupMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        CourseDefinitionStorage projectService = CourseDefinitionStorage.getInstance(project);
        String stepName = vf.getName().split("\\.")[0];

        List<Submission> submissions =
                StepicConnector.getSubmissions(projectService.getStepID(vf.getPath()), project);

        if (submissions.isEmpty()) {
            NotificationUtils.showNotification(NotificationTemplates.DOWNLOAD_WARNING, project);
        } else {
            String code = submissions.get(submissions.size() - 1).getCode();
            String pack = CourseDefinitionStorage.getInstance(project).getPackageName(vf.getPath());
            Document doc = FileDocumentManager.getInstance().getDocument(vf);
            String code2 = renameFromMainToStep(code, stepName, pack);
            CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                @Override
                public void run() {
                    ApplicationManager.getApplication().runWriteAction(new Runnable() {
                        @Override
                        public void run() {
                            doc.setText(code2);
                        }
                    });
                }
            }, "Download last submission", "Download last submission");
        }
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

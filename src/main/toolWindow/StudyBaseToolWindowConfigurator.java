package main.toolWindow;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import main.stepicConnector.NewProjectService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collections;
import java.util.Map;

public abstract class StudyBaseToolWindowConfigurator implements StudyToolWindowConfigurator {
    @NotNull
    @Override
    public DefaultActionGroup getActionGroup(Project project) {
        final DefaultActionGroup group = new DefaultActionGroup();
//    group.add(new StudyPreviousStudyTaskAction());
//    group.add(new StudyNextStudyTaskAction());
//    group.add(new StudyRefreshTaskFileAction());
//    group.add(new StudyShowHintAction());
//
//    group.add(new StudyRunAction());
//    group.add(new StudyEditInputAction());
        return group;
    }

    @NotNull
    @Override
    public Map<String, JPanel> getAdditionalPanels(Project project) {
        return Collections.emptyMap();
    }

    @NotNull
    @Override
    public FileEditorManagerListener getFileEditorManagerListener(@NotNull Project project, @NotNull StudyToolWindow toolWindow) {

        return new FileEditorManagerListener() {

            private static final String EMPTY_TASK_TEXT = "Please, open any task to see task description";

            @Override
            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                setTaskText(file);
            }

            @Override
            public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                toolWindow.setTaskText(EMPTY_TASK_TEXT);
            }

            @Override
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                VirtualFile file = event.getNewFile();
                if (file != null) {
                    setTaskText(file);
                }
            }

            private void setTaskText(final VirtualFile virtualFile) {
                String text = NewProjectService.getInstance(project).getText(virtualFile.getPath());
                toolWindow.setTaskText(text);
            }
        };
    }
}

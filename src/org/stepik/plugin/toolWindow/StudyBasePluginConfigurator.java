package org.stepik.plugin.toolWindow;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.stepik.plugin.storages.CourseDefinitionStorage;

import javax.swing.*;
import java.util.Collections;
import java.util.Map;

public abstract class StudyBasePluginConfigurator implements StudyPluginConfigurator {
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

            private final CourseDefinitionStorage cs = CourseDefinitionStorage.getInstance(project);
            private static final String EMPTY_STEP_TEXT = "Please, open any Step to see Step description";

            @Override
            public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                if (cs.contains(file.getPath())) {
                    setStepText(file);
                }
            }

            @Override
            public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
                toolWindow.setStepText(EMPTY_STEP_TEXT);
            }

            @Override
            public void selectionChanged(@NotNull FileEditorManagerEvent event) {
                VirtualFile file = event.getNewFile();
                if (file != null && cs.contains(file.getPath())) {
                    setStepText(file);
                }
            }

            private void setStepText(final VirtualFile virtualFile) {
                String text = CourseDefinitionStorage.getInstance(project).getText(virtualFile.getPath());
                toolWindow.setStepText(text);
            }
        };
    }
}

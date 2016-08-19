package org.stepik.plugin.components;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import javafx.application.Platform;
import org.jetbrains.annotations.NotNull;
import org.stepik.plugin.stepikConnector.StepikConnector;
import org.stepik.plugin.storages.CourseDefinitionStorage;
import org.stepik.plugin.toolWindow.StudyToolWindowFactory;
import org.stepik.plugin.toolWindow.StudyUtils;
import org.stepik.plugin.utils.MyLogger;

public class MyProjectComponent implements ProjectComponent {
    private Project project;

    public MyProjectComponent(Project project) {
        this.project = project;
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "MyProjectComponent";
    }

    @Override
    public void projectOpened() {
        MyLogger.getInstance().getLOG().debug("projectOpened");

        if (StudyUtils.hasJavaFx()) {
            Platform.setImplicitExit(false);
        }

        if (project.getName().equals(CourseDefinitionStorage.getInstance(project).getProjectName())) {
            StepikConnector.initToken(project);
            registerStudyToolWindow();
        }
    }

    private void registerStudyToolWindow() {
        MyLogger.getInstance().getLOG().debug("registerStudyToolWindow");
        final ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        registerToolWindows(toolWindowManager);
        final ToolWindow studyToolWindow = toolWindowManager.getToolWindow(StudyToolWindowFactory.STUDY_TOOL_WINDOW);
        if (studyToolWindow != null) {
            StudyUtils.initToolWindows(project);
        }
    }

    private void registerToolWindows(@NotNull final ToolWindowManager toolWindowManager) {
        MyLogger.getInstance().getLOG().debug("registerToolWindows");
        final ToolWindow toolWindow = toolWindowManager.getToolWindow(StudyToolWindowFactory.STUDY_TOOL_WINDOW);
        if (toolWindow == null) {
            toolWindowManager.registerToolWindow(StudyToolWindowFactory.STUDY_TOOL_WINDOW, true, ToolWindowAnchor.RIGHT, project, true);
        }
    }

    @Override
    public void projectClosed() {
    }
}

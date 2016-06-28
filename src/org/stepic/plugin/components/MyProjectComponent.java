package org.stepic.plugin.components;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;
import org.stepic.plugin.stepicConnector.StepicConnector;
import org.stepic.plugin.storages.CourseDefinitionStorage;
import org.stepic.plugin.toolWindow.StudyToolWindowFactory;
import org.stepic.plugin.toolWindow.StudyUtils;
import org.stepic.plugin.utils.MyLogger;

public class MyProjectComponent implements ProjectComponent {
    Project project;

    public MyProjectComponent(Project project) {
        this.project = project;
    }

    @Override
    public void initComponent() {
//        StepicConnector.setSSLProperty(project);
//        if (project.getName().equals(CourseDefinitionStorage.getInstance(project).getProjectName())) {
//            StepicConnector.initToken(project);
//        }
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
        MyLogger.getInstance().getLOG().warn("projectOpened");
        if (project.getName().equals(CourseDefinitionStorage.getInstance(project).getProjectName())) {
            StepicConnector.initToken(project);
            registerStudyToolWindow();
        }
    }

    public void registerStudyToolWindow() {
//        if (course != null && "PyCharm".equals(course.getCourseType())) {
        MyLogger.getInstance().getLOG().warn("registerStudyToolWindow");
            final ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
            registerToolWindows(toolWindowManager);
            final ToolWindow studyToolWindow = toolWindowManager.getToolWindow(StudyToolWindowFactory.STUDY_TOOL_WINDOW);
            if (studyToolWindow != null) {
                studyToolWindow.show(null);
                StudyUtils.initToolWindows(project);
            }
//        }
    }

    private void registerToolWindows(@NotNull final ToolWindowManager toolWindowManager) {
        MyLogger.getInstance().getLOG().warn("registerToolWindows");
        final ToolWindow toolWindow = toolWindowManager.getToolWindow(StudyToolWindowFactory.STUDY_TOOL_WINDOW);
        if (toolWindow == null) {
            toolWindowManager.registerToolWindow(StudyToolWindowFactory.STUDY_TOOL_WINDOW, true, ToolWindowAnchor.RIGHT, project, true);
        }
    }

    @Override
    public void projectClosed() {
    }
}

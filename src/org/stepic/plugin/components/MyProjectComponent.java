package org.stepic.plugin.components;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.stepic.plugin.stepicConnector.StepicConnector;
import org.stepic.plugin.storages.CourseDefinitionStorage;

public class MyProjectComponent implements ProjectComponent {
    Project project;

    public MyProjectComponent(Project project) {
        this.project = project;
    }

    @Override
    public void initComponent() {
        if (project.getName().equals(CourseDefinitionStorage.getInstance(project).getProjectName())) {
            StepicConnector.initToken(project);
        }
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
        if (project.getName().equals(CourseDefinitionStorage.getInstance(project).getProjectName())) {
            StepicConnector.initToken(project);
        }
    }

    @Override
    public void projectClosed() {
    }
}

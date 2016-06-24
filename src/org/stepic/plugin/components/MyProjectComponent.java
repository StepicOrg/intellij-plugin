package org.stepic.plugin.components;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.stepic.plugin.stepicConnector.StepicConnector;
import org.stepic.plugin.storages.ActionVisibleProperties;
import org.stepic.plugin.storages.CourseDefinitionStorage;

public class MyProjectComponent implements ProjectComponent {
    Project project;

    public MyProjectComponent(Project project) {
        this.project = project;
    }

    @Override
    public void initComponent() {
        ActionVisibleProperties.Wrapper prop = ActionVisibleProperties.getInstance(project);
        prop.setEnabled(false);
        prop.setVisible(false);
    }

    @Override
    public void disposeComponent() {
//        ActionVisibleProperties prop = ActionVisibleProperties.getInstance();
        ActionVisibleProperties.Wrapper prop = ActionVisibleProperties.getInstance(project);
        prop.setEnabled(false);
        prop.setVisible(false);
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "MyProjectComponent";
    }

    @Override
    public void projectOpened() {
        ActionVisibleProperties.Wrapper prop = ActionVisibleProperties.getInstance(project);

        if (project.getName().equals(CourseDefinitionStorage.getInstance(project).getProjectName())) {
            prop.setEnabled(true);
            prop.setVisible(true);
            StepicConnector.initToken(project);
        }
    }

    @Override
    public void projectClosed() {
        ActionVisibleProperties.Wrapper prop = ActionVisibleProperties.getInstance(project);
        prop.setEnabled(false);
        prop.setVisible(false);
    }
}

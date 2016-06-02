package main.components;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import main.stepicConnector.StepicConnector;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Petr on 26.05.2016.
 */
public class MyProjectComponent implements ProjectComponent {
    public MyProjectComponent(Project project) {
    }

    @Override
    public void initComponent() {
        StepicConnector.initToken();
        // TODO: insert component initialization logic here
    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "MyProjectComponent";
    }

    @Override
    public void projectOpened() {

        // called when project is opened
    }

    @Override
    public void projectClosed() {
        // called when project is being closed
    }
}

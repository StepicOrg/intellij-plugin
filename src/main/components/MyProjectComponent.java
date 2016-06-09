package main.components;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import main.actions.ActionVisibleProperties;
import main.stepicConnector.StepicConnector;
import main.stepicConnector.WorkerService;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Petr on 26.05.2016.
 */
public class MyProjectComponent implements ProjectComponent {
    Project project;

    public MyProjectComponent(Project project) {
        this.project = project;
    }

    @Override
    public void initComponent() {
//        ActionVisibleProperties prop = ActionVisibleProperties.getInstance();
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
//        MyLogger.getInstance().getLOG().warn("name = " + project.getName());
        //        ActionVisibleProperties prop = ActionVisibleProperties.getInstance();
        ActionVisibleProperties.Wrapper prop = ActionVisibleProperties.getInstance(project);

        if (project.getName().equals(WorkerService.getInstance().getProjectName())) {
//            ActionVisibleProperties prop = ActionVisibleProperties.getInstance();
            prop.setEnabled(true);
            prop.setVisible(true);
            StepicConnector.initToken();
        }
        // called when project is opened
    }

    @Override
    public void projectClosed() {
        //        ActionVisibleProperties prop = ActionVisibleProperties.getInstance();
        ActionVisibleProperties.Wrapper prop = ActionVisibleProperties.getInstance(project);
        prop.setEnabled(false);
        prop.setVisible(false);
    }
}

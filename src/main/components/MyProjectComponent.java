package main.components;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.actions.ActionVisibleProperties;
import main.stepicConnector.NewProjectService;
import main.stepicConnector.StepicConnector;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

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

        if (project.getName().equals(NewProjectService.getInstance(project).getProjectName())) {
            prop.setEnabled(true);
            prop.setVisible(true);
            try {
                StepicConnector.initToken();
            } catch (UnirestException e) {
                e.printStackTrace();
            } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void projectClosed() {
        ActionVisibleProperties.Wrapper prop = ActionVisibleProperties.getInstance(project);
        prop.setEnabled(false);
        prop.setVisible(false);
    }
}

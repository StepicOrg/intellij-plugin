package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.stepicConnector.StepicApplicationService;
import main.stepicConnector.StepicConnector;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SingIn extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        StepicApplicationService.getInstance().setLogin(
                Messages.showInputDialog(project, "Please, input your E-mail", "Sing in", Messages.getQuestionIcon()));
        StepicApplicationService.getInstance().setPassword(
                Messages.showPasswordDialog(project, "Please, input your Password", "Sing in", Messages.getQuestionIcon()));

        try {
            StepicConnector.initToken();
        } catch (UnirestException ex) {
            ex.printStackTrace();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException ex) {
            ex.printStackTrace();
        }
        String name = StepicConnector.getUserName();

        Messages.showMessageDialog(project, "Hello, " + name + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
    }
}

package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.stepicConnector.StepicConnector;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class RefreshToken extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        try {
            StepicConnector.initToken();
        } catch (UnirestException ex) {
            ex.printStackTrace();
        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException | IOException ex) {
            ex.printStackTrace();
        }
    }
}

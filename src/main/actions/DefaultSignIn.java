package main.actions;

import javafx.util.Pair;
import stepicConnector.Commands;
import stepicConnector.WorkerService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.json.JSONException;
import stepicConnector.WorkWithProperties;

import java.io.IOException;

/**
 * Created by Petr on 22.03.2016.
 */
public class DefaultSignIn extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final String client_id;
        final String client_secret;
        String token = null;

        Pair<String, String> pair = WorkWithProperties.getProperties();
        client_id = pair.getKey();
        client_secret = pair.getValue();


        WorkerService ws = WorkerService.getInstance();
        ws.setClientId(client_id);
        ws.setClientSecret(client_secret);

        try {
            ws.setToken(Commands.getToken(ws.getClientId(), ws.getClientSecret()));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}

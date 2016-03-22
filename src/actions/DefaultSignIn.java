package actions;

import stepicConnector.Commands;
import stepicConnector.WorkerService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Petr on 22.03.2016.
 */
public class DefaultSignIn extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        WorkerService ws = WorkerService.getInstance();
        ws.setClientId("XeDsIRtCBYdvEnShTtMw6quDO3aVlEgnvy27Tgeg");
        ws.setClientSecret( "661DX7ctAyraWeJNQhbVrMgeSpZb40Omzee4eTETRwFI7BzsfKOId" +
                "HFHRcQtIEL6wOnncqw4ztNvwjtoe1KX2JkC7TYbF1zPPxRezt0D9fZLd1FaAgf2NdK1VJoMiuPp");

        try {
            ws.setToken(Commands.getToken(ws.getClientId(),ws.getClientSecret()));
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}

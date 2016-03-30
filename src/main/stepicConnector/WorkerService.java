package main.stepicConnector;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;


// don't work (((


//@Storage(id="WorkerService", file = "/WorkerService.xml")
//@State(name = "WorkerService", storages = @Storage("WorkerService.xml"))
@State(name = "WorkerService", storages = @Storage(id="WorkerService", file = "/WorkerService.xml"))
public class WorkerService implements PersistentStateComponent<WorkerService> {
//    private static WorkerService ourInstance = new WorkerService();
    public String clientId;
    public String clientSecret;
    public String token;

    public static WorkerService getInstance() {
//        return ServiceManager.getService(main.stepicConnector.WorkerService.class);
        return ServiceManager.getService(WorkerService.class);
    }

    public WorkerService() {
    }

    public WorkerService getState() {
        return this;
    }

    public void loadState(WorkerService state) {
        XmlSerializerUtil.copyBean(state, this);
    }




//    setters and getters -------------------------------

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

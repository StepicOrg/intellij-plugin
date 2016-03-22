package StepicConnector;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.util.xmlb.XmlSerializerUtil;


// don't work (((


@Storage(id="WorkerService", file = StoragePathMacros.APP_CONFIG + "/WorkerService.xml")
//@State(name = "WorkerService", storages = @Storage("course_service.xml"))
public class WorkerService implements PersistentStateComponent<WorkerService> {
    private static WorkerService ourInstance = new WorkerService();
    public String clientId;
    public String clientSecret;
    public String token;

    public static WorkerService getInstance() {
        return ourInstance;
    }

    private WorkerService() {
    }

    @Override
    public WorkerService getState() {
//        return ourInstance;
        System.out.println("get state ----------");
        return this;
    }

    @Override
    public void loadState(WorkerService state) {
//        XmlSerializerUtil.copyBean(state, ourInstance);
        System.out.println("Load state ----------");
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

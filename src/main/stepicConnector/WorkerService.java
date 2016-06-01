package main.stepicConnector;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

// TODO: 27.05.2016 Project level

//@State(name = "WorkerService", storages = @Storage("WorkerService.xml"))
@State(name = "WorkerService", storages = @Storage(id = "WorkerService", file = "/WorkerService.xml"))
public class WorkerService implements PersistentStateComponent<WorkerService> {
    private final String clientId = "hUCWcq3hZHCmz0DKrDtwOWITLcYutzot7p4n59vU";
    private String courseLink;
    private String clientSecret;
    private String token;
    private String refresh_token;

//    private Map<String, String> mapPathStep;

    // TODO: 14.05.2016 remove username and password
    private String username;
    private String password;

    public static WorkerService getInstance() {
        return ServiceManager.getService(WorkerService.class);
    }

    public WorkerService() {
//        mapPathStep = new HashMap<>();
    }

    public WorkerService getState() {
        return this;
    }

    public void loadState(WorkerService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

//    public void addPathStep(String path, String stepId) {
//        if (mapPathStep == null)
//            mapPathStep = new HashMap<>();
//        mapPathStep.put(path, stepId);
//    }

//    public String getStepId(String path) {
//        if (mapPathStep == null)
//            mapPathStep =new HashMap<>();
//        return mapPathStep.getOrDefault(path, "");
//    }

//    public void setAttemptId(String path, String stepId, String attemptId) {
//        mapPathStep.put(path, stepId);
//    }


//    setters and getters -------------------------------

    public String getClientId() {
        return clientId;
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

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCourseLink() {
        return courseLink;
    }

    public void setCourseLink(String courseLink) {
        this.courseLink = courseLink;
    }

//    public Map<String, String> getMapPathStep() {
//        return mapPathStep;
//    }

//    public void setMapPathStep(Map<String, String> mapPathStep) {
//        this.mapPathStep = mapPathStep;
//    }

}

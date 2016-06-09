package main.stepicConnector;

import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.passwordSafe.PasswordSafeException;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;

// TODO: 27.05.2016 Project level

//@State(name = "WorkerService", storages = @Storage("WorkerService.xml"))
@State(name = "WorkerService", storages = @Storage(id = "WorkerService", file = "/WorkerService.xml"))
public class WorkerService implements PersistentStateComponent<WorkerService> {
    private final String clientId = "hUCWcq3hZHCmz0DKrDtwOWITLcYutzot7p4n59vU";
    private ModifiableRootModel rootModel;
    private String courseID;
    private String clientSecret;
    private String token;
    private String refresh_token;
    private String projectName;

    @Transient
    private static final Logger LOG = Logger.getInstance(WorkerService.class);


    // TODO: 14.05.2016 remove login and password
    private String login;
    //    private String password;
    private boolean translate;

    public static WorkerService getInstance() {
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

    private String getPasswordKey() {
        return "STEPIC_SETTINGS_PASSWORD_KEY: " + getLogin();
    }


//    setters and getters -------------------------------

    @NotNull
    public String getPassword() {
        final String login = getLogin();
        if (StringUtil.isEmptyOrSpaces(login)) return "";

        String password;
        try {
            password = PasswordSafe.getInstance().getPassword(null, WorkerService.class, getPasswordKey());
        } catch (PasswordSafeException e) {
            LOG.info("Couldn't get password for key [" + getPasswordKey() + "]", e);
            password = "";
        }

        return StringUtil.notNullize(password);
    }

    public void setPassword(@NotNull String password) {
        try {
            PasswordSafe.getInstance().storePassword(null, WorkerService.class, getPasswordKey(), password);
        } catch (PasswordSafeException e) {
            LOG.info("Couldn't set password for key [" + getPasswordKey() + "]", e);
        }
    }

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

//    public String getPassword() {
//        return password;
//    }

//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public ModifiableRootModel getRootModel() {
        return rootModel;
    }

    public void setRootModel(ModifiableRootModel rootModel) {
        this.rootModel = rootModel;
    }

    public void setTranslator(boolean translator) {
        this.translate = translator;
    }

    public boolean isTranslate() {
        return translate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}

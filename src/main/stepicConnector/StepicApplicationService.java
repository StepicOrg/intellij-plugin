package main.stepicConnector;

import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.passwordSafe.PasswordSafeException;
import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.jetbrains.annotations.NotNull;

import java.util.Date;


@State(name = "StepicApplicationService", storages = @Storage(id = "StepicApplicationService", file = StoragePathMacros.APP_CONFIG + "/StepicApplicationService.xml"))
public class StepicApplicationService implements PersistentStateComponent<StepicApplicationService> {
    private final String clientId = "hUCWcq3hZHCmz0DKrDtwOWITLcYutzot7p4n59vU";
    private String clientSecret;
    private String token;
    private String refresh_token;
    private String login;
    private long tokenTimeCreate;

    @Transient
    private static final Logger LOG = Logger.getInstance(StepicApplicationService.class);

    public static StepicApplicationService getInstance() {
        return ServiceManager.getService(StepicApplicationService.class);
    }

    public StepicApplicationService() {
    }

    public StepicApplicationService getState() {
        return this;
    }

    public void loadState(StepicApplicationService state) {
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
            password = PasswordSafe.getInstance().getPassword(null, StepicApplicationService.class, getPasswordKey());
        } catch (PasswordSafeException e) {
            LOG.info("Couldn't get password for key [" + getPasswordKey() + "]", e);
            password = "";
        }

        return StringUtil.notNullize(password);
    }

    public void setPassword(@NotNull String password) {
        try {
            PasswordSafe.getInstance().storePassword(null, StepicApplicationService.class, getPasswordKey(), password);
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
        if (timePassedLessThen(tokenTimeCreate, new Date().getTime(), 9*60*60)) {
            return token;
        } else {
//            StepicConnector.initToken();
            try {
                StepicConnector.setTokenGRP();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
        return token;
    }

    private boolean timePassedLessThen(long base, long current, long sec) {
//        long delta = d1.getTime() - d0.getTime();
        long delta = current - base;
        return delta - sec * 1000L < 0L;
    }

    public void setToken(String token) {
        this.token = token;
        Date date = new Date();
        tokenTimeCreate = date.getTime();
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

    public void setTokenTimeCreate(long tokenTimeCreate) {
        this.tokenTimeCreate = tokenTimeCreate;
    }

    public long getTokenTimeCreate() {
        return tokenTimeCreate;
    }

    //    public String getPassword() {
//        return password;
//    }

//    public void setPassword(String password) {
//        this.password = password;
//    }




}

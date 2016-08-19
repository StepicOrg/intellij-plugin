package org.stepik.plugin.storages;

import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.passwordSafe.PasswordSafeException;
import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;


@State(name = "StepikApplicationStorage", storages = @Storage(id = "StepikApplicationStorage", file = StoragePathMacros.APP_CONFIG + "/StepikApplicationStorage.xml"))
public class StepikApplicationStorage implements PersistentStateComponent<StepikApplicationStorage> {
    private final String clientId = "hUCWcq3hZHCmz0DKrDtwOWITLcYutzot7p4n59vU";
    private String login;
    private long tokenTimeCreate;

    @Transient
    private static final Logger LOG = Logger.getInstance(StepikApplicationStorage.class);

    public static StepikApplicationStorage getInstance() {
        return ServiceManager.getService(StepikApplicationStorage.class);
    }

    public StepikApplicationStorage() {
    }

    public StepikApplicationStorage getState() {
        return this;
    }

    public void loadState(StepikApplicationStorage state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    private String getPasswordKey() {
        return "STEPIK_SETTINGS_PASSWORD_KEY: " + getLogin();
    }


//    setters and getters -------------------------------

    @NotNull
    public String getPassword() {
        final String login = getLogin();
        if (StringUtil.isEmptyOrSpaces(login)) return "";

        String password;
        try {
            password = PasswordSafe.getInstance().getPassword(null, StepikApplicationStorage.class, getPasswordKey());
        } catch (PasswordSafeException e) {
            LOG.info("Couldn't get password for key [" + getPasswordKey() + "]", e);
            password = "";
        }

        return StringUtil.notNullize(password);
    }

    public void setPassword(@NotNull String password) {
        try {
            PasswordSafe.getInstance().storePassword(null, StepikApplicationStorage.class, getPasswordKey(), password);
        } catch (PasswordSafeException e) {
            LOG.info("Couldn't set password for key [" + getPasswordKey() + "]", e);
        }
    }

    public String getClientId() {
        return clientId;
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
}

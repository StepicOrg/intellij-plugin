package org.stepic.plugin.storages;

import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.passwordSafe.PasswordSafeException;
import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import org.jetbrains.annotations.NotNull;

import java.util.Date;


@State(name = "StudentStorage", storages = @Storage(id = "StudentStorage", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/StudentStorage.xml"))
public class StudentStorage implements PersistentStateComponent<StudentStorage> {
    private String token;
    private String refresh_token;
    private String login;
    private long tokenTimeCreate;

    @Transient
    private static final Logger LOG = Logger.getInstance(StudentStorage.class);

    public static StudentStorage getInstance(Project project) {
        return ServiceManager.getService(project, StudentStorage.class);
    }

    public StudentStorage() {
    }

    public StudentStorage getState() {
        return this;
    }

    public void loadState(StudentStorage state) {
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
            password = PasswordSafe.getInstance().getPassword(null, StudentStorage.class, getPasswordKey());
        } catch (PasswordSafeException e) {
            LOG.info("Couldn't get password for key [" + getPasswordKey() + "]", e);
            password = "";
        }

        return StringUtil.notNullize(password);
    }

    private void setPassword(@NotNull String password) {
        try {
            PasswordSafe.getInstance().storePassword(null, StudentStorage.class, getPasswordKey(), password);
        } catch (PasswordSafeException e) {
            LOG.info("Couldn't set password for key [" + getPasswordKey() + "]", e);
        }
    }

    public String getToken() {
        return token;
    }

    private boolean timePassedLessThen(long base, long current, long sec) {
//        long delta = d1.getTime() - d0.getTime();
        long delta = current - base;
        return delta - sec * 1000L < 0L;
    }

    public void setToken(String token) {
        tokenTimeCreate = new Date().getTime();
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

    public void setLoginAndPassword(String login, String password) {
        setLogin(login);
        setPassword(password);
    }

    public void setTokenTimeCreate(long tokenTimeCreate) {
        this.tokenTimeCreate = tokenTimeCreate;
    }

    public long getTokenTimeCreate() {
        return tokenTimeCreate;
    }
}

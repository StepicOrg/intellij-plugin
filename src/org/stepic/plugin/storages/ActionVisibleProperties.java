package org.stepic.plugin.storages;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(name = "ActionVisibleProperties", storages = @Storage(id = "ActionVisibleProperties", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/ActionVisibleProperties.xml"))
public class ActionVisibleProperties implements PersistentStateComponent<ActionVisibleProperties> {
    private boolean visible;
    private boolean enabled;


    private ActionVisibleProperties() {
        visible = false;
        enabled = false;
    }

    public static ActionVisibleProperties getInstance(Project project) {
        return ServiceManager.getService(project, ActionVisibleProperties.class);
    }

    @Nullable
    @Override
    public ActionVisibleProperties getState() {
        return this;
    }

    @Override
    public void loadState(ActionVisibleProperties state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

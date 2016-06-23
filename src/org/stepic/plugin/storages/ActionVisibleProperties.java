package org.stepic.plugin.storages;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@State(name = "ActionVisibleProperties", storages = @Storage(id = "ActionVisibleProperties", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/ActionVisibleProperties.xml"))
public class ActionVisibleProperties implements PersistentStateComponent<ActionVisibleProperties> {
    private static final Map<String, Wrapper> multitone = new HashMap<>();

    public static Wrapper getInstance(Project project) {
        String key = project.getBasePath();
        if (multitone.containsKey(key)) {
            return multitone.get(key);
        } else {
            Wrapper n = new Wrapper();
            multitone.put(key, n);
            return n;
        }
    }

    private ActionVisibleProperties() {
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

    public static class Wrapper {
        Map<String,Object> prop;

        public Wrapper() {
            prop = new HashMap<>();
        }

        private void setProp(String key, Object value){
            prop.put(key, value);
        }

        private boolean getBooleanValue(String key){
            return (boolean) prop.get(key);
        }

        public void setVisible(boolean b){
            setProp("Visible" , b);
        }

        public void setEnabled(boolean b){
            setProp("Enabled" , b);
        }

        public boolean getVisible(){
            return getBooleanValue("Visible");
        }

        public boolean getEnabled(){
            return getBooleanValue("Enabled");
        }
    }
}

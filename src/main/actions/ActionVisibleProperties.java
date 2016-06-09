package main.actions;

import com.intellij.openapi.project.Project;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Petr on 09.06.2016.
 */
public class ActionVisibleProperties {
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

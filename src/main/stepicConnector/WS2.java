package main.stepicConnector;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Petr on 21.05.2016.
 */
//
// TODO: 27.05.2016 Module level

@State(name = "WS2", storages = @Storage(id = "WS2", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/WS2.xml"))
//@State(name = "WS2", storages = @Storage(id = "WS2"))
public class WS2 implements PersistentStateComponent<WS2> {

    private Map<String, String> map;


    public static WS2 getInstance(Project project) {
        return ServiceManager.getService(project, WS2.class);
    }

//    public static WS2 getInstance() {
//        return ServiceManager.getService(WS2.class);
//    }

    public WS2() {
        map = new HashMap<>();
    }

    public WS2 getState() {
        return this;
    }

    public void loadState(WS2 state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public void setAttemptId(String stepId, String attemptId) {
        if (map == null)
            map = new HashMap<>();
        map.put(stepId, attemptId);
    }

    public String getAttemptId(String stepId) {
        if (map == null)
            map = new HashMap<>();
        return map.getOrDefault(stepId, "");
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void removeAll(Set<String> removed) {
        map.keySet().removeAll(removed);
    }
}


package main.stepicConnector;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Petr on 21.05.2016.
 */
@State(name = "WS2", storages = @Storage(id = "WS2", file = "/WS2.xml"))
public class WS2 implements PersistentStateComponent<WS2> {

    private Map<String, String> map;


    public static WS2 getInstance() {
        return ServiceManager.getService(WS2.class);
    }

    public WS2() {
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
}


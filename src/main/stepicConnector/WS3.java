package main.stepicConnector;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Petr on 27.05.2016.
 */

// storage Path - StepNo
// TODO: 27.05.2016 Module level

@State(name = "WS3", storages = @Storage(id = "WS3", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/WS3.xml"))
//@State(name = "WS3", storages = @Storage(id = "WS3"))
public class WS3 implements PersistentStateComponent<WS3> {

    private Map<String, String> mapPathStep;

    public static WS3 getInstance(Project project) {
//    public static WS3 getInstance() {
        return ServiceManager.getService(project, WS3.class);
    }

    public WS3() {
        mapPathStep = new HashMap<>();
    }

    public WS3 getState() {
        return this;
    }

    public void loadState(WS3 state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public void addPathStep(String path, String stepId) {
//        if (mapPathStep == null)
//            mapPathStep = new HashMap<>();
        mapPathStep.put(path, stepId);
    }

    public Map<String, String> getMapPathStep() {
//        if (mapPathStep == null) {
//            mapPathStep = new HashMap<>();
//        }
        return mapPathStep;
    }

    public void setMapPathStep(Map<String, String> mapPathStep) {
        this.mapPathStep = mapPathStep;
    }

    public String getStepId(String path) {
//        if (mapPathStep == null)
//            mapPathStep = new HashMap<>();
        return mapPathStep.getOrDefault(path, "");
    }


}

package main.stepicConnector;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Created by Petr on 27.05.2016.
 */

// storage Path - StepInfo

@State(name = "StepicProjectService", storages = @Storage(id = "StepicProjectService", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/StepicProjectService.xml"))
public class StepicProjectService implements PersistentStateComponent<StepicProjectService> {

    private Map<String, String> mapPathStep;
    private Map<String, String> pathAttId;
    private Map<String, String> pathPackage;
    private String courseID;
    private boolean translate;

    private String projectName;

    @Nullable
    @Override
    public StepicProjectService getState() {
        return null;
    }

    @Override
    public void loadState(StepicProjectService stepicProjectService) {

    }

//    public static StepicProjectService getInstance(Project project) {
//        return ServiceManager.getService(project, StepicProjectService.class);
//    }
//
//    public StepicProjectService() {
//        mapPathStep = new HashMap<>();
//        pathAttId = new HashMap<>();
//        pathPackage = new HashMap<>();
//    }
//
//    public StepicProjectService getState() {
//        return this;
//    }
//
//    public void loadState(StepicProjectService state) {
//        XmlSerializerUtil.copyBean(state, this);
//    }
//
//    public void addPathStep(String path, String stepId) {
//        mapPathStep.put(path, stepId);
//    }
//
//    public Map<String, String> getMapPathStep() {
//        return mapPathStep;
//    }
//
//    public void setMapPathStep(Map<String, String> mapPathStep) {
//        this.mapPathStep = mapPathStep;
//    }
//
//    public String getStepId(String path) {
//        return mapPathStep.getOrDefault(path, "");
//    }
//
//
//    public void removeAll(Set<String> removed) {
//        mapPathStep.keySet().removeAll(removed);
//    }
//
//    public String getProjectName() {
//        return projectName;
//    }
//
//    public void setProjectName(String projectName) {
//        this.projectName = projectName;
//    }
//
//
//    public String getAttemptId(String path) {
//        return pathAttId.getOrDefault(path, "");
//    }
//
//    public void setAttemptId(String path, String attemptId) {
//        pathAttId.put(path, attemptId);
//    }
//
//    public Map<String, String> getPathAttId() {
//        return pathAttId;
//    }
//
//    public void setPathAttId(Map<String, String> pathAttId) {
//        this.pathAttId = pathAttId;
//    }
//
//    public void addPathPackage(String path, String pack) {
//        pathPackage.put(path, pack);
//    }
//
//    public String getPackage(String path){
//        return pathPackage.getOrDefault(path, "");
//    }
//
//    public Map<String, String> getPathPackage() {
//        return pathPackage;
//    }
//
//    public void setPathPackage(Map<String, String> pathPackage) {
//        this.pathPackage = pathPackage;
//    }
//
//    public void setTranslator(boolean translator) {
//        this.translate = translator;
//    }
//
//    public boolean isTranslate() {
//        return translate;
//    }
//
//    public String getCourseID() {
//        return courseID;
//    }
//
//    public void setCourseID(String courseID) {
//        this.courseID = courseID;
//    }

}

package main.stepicConnector;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Petr on 27.05.2016.
 */

// storage Path - StepInfo

@State(name = "ProjectService", storages = @Storage(id = "ProjectService", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/ProjectService.xml"))
public class ProjectService implements PersistentStateComponent<ProjectService> {

    private Map<String, String> mapPathStep;
    private Map<String, String> pathAttId;
    private String projectName;

    public static ProjectService getInstance(Project project) {
        return ServiceManager.getService(project, ProjectService.class);
    }

    public ProjectService() {
        mapPathStep = new HashMap<>();
    }

    public ProjectService getState() {
        return this;
    }

    public void loadState(ProjectService state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public void addPathStep(String path, String stepId) {
        mapPathStep.put(path, stepId);
    }

    public Map<String, String> getMapPathStep() {
        return mapPathStep;
    }

    public void setMapPathStep(Map<String, String> mapPathStep) {
        this.mapPathStep = mapPathStep;
    }

    public String getStepId(String path) {
        return mapPathStep.getOrDefault(path, "");
    }


    public void removeAll(Set<String> removed) {
        mapPathStep.keySet().removeAll(removed);
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public String getAttemptId(String path) {
        return pathAttId.getOrDefault(path, "");
    }

    public void setAttemptId(String path, String attemptId) {
       pathAttId.put(path, attemptId);
    }

}

package main.stepicConnector;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializer;
import main.edu.stepic.StepInfo;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Petr on 14.06.2016.
 */
@State(name = "NewProjectService", storages = @Storage(id = "NewProjectService", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/NewProjectService.xml"))
public class NewProjectService implements PersistentStateComponent<Element> {

    public Map<String, StepInfo> mapPathInfo = new HashMap<>();
    private String courseID;
    private boolean translate;
    private String projectName;

    //    public static final String COURSE_ELEMENT = "courseElement";
    public static final String MAIN_ELEMENT = "ProjectService";

    public static NewProjectService getInstance(@NotNull final Project project) {
        return ServiceManager.getService(project, NewProjectService.class);
    }

    @Nullable
    @Override
    public Element getState() {
        Element el = new Element("projectService");
//        if (myCourse != null) {
        Element courseElement = new Element(MAIN_ELEMENT);
        XmlSerializer.serializeInto(this, courseElement);
        el.addContent(courseElement);
//        }
        return el;
    }

    @Override
    public void loadState(Element state) {
        final Element mainElement = state.getChild(MAIN_ELEMENT);
        if (mainElement != null) {
            final NewProjectService projectService = XmlSerializer.deserialize(mainElement, NewProjectService.class);
            if (projectService != null) {
                mapPathInfo = projectService.mapPathInfo;
                courseID = projectService.courseID;
                translate = projectService.translate;
                projectName = projectService.projectName;
            }
        }

//        there can be your init
    }

    private StepInfo getStepInfo(String path) {
//        return mapPathInfo.getOrDefault(path, StepInfo.EMPTY);
        return mapPathInfo.getOrDefault(path, new StepInfo(""));
    }

    private StepInfo getOrCreateStepInfo(String path) {
        if (mapPathInfo.containsKey(path)) {
            return mapPathInfo.get(path);
        } else {
            StepInfo stepInfo = new StepInfo("");
            mapPathInfo.put(path, stepInfo);
            return stepInfo;
        }
    }

    public String getStepID(String path) {
        return getStepInfo(path).getStepID();
    }

    public String getAttemptID(String path) {
        return getStepInfo(path).getAttemptID();
    }

    public String getPackageName(String path) {
        return getStepInfo(path).getPackageName();
    }

    public void setStepID(String path, String stepID){
        StepInfo stepInfo = getOrCreateStepInfo(path);
        stepInfo.setStepID(stepID);
    }

    public void setAttemptID(String path, String attemptID){
        StepInfo stepInfo = getOrCreateStepInfo(path);
        stepInfo.setAttemptID(attemptID);
    }
    public void setPackageName(String path, String packageName){
        StepInfo stepInfo = getOrCreateStepInfo(path);
        stepInfo.setPackageName(packageName);
    }


    public void addStepInfo(String path, StepInfo stepInfo) {
        mapPathInfo.put(path, stepInfo);
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public boolean isTranslate() {
        return translate;
    }

    public void setTranslate(boolean translate) {
        this.translate = translate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Map<String, StepInfo> getMapPathInfo() {
        return mapPathInfo;
    }

    public void removeAll(Set<String> removed) {
        mapPathInfo.keySet().removeAll(removed);
    }
}
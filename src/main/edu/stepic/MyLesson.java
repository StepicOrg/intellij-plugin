package main.edu.stepic;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import main.projectWizard.MyFileInfoList;
import main.stepicConnector.StepicConnector;
import main.stepicConnector.StepicProjectService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Petr on 02.05.2016.
 */
public class MyLesson {

    private static final Logger LOG = Logger.getInstance(MyLesson.class);
    int id;
    @SerializedName("steps")
    List<Integer> stepsId;

    public String title;
    private String lessonName;

    public transient Map<Integer, MyStep> steps = new HashMap<>();
    private int lessonNo;

    public void build(int lessonNo, String courseDir, String sectionDir, Project project) {
        this.lessonNo = lessonNo;
        StepicProjectService projectService = StepicProjectService.getInstance(project);

        List<MyStep> myStepsList = StepicConnector.getSteps(StepicConnector.getIdQuery(stepsId));

        for (MyStep step : myStepsList) {
            if (step.isTask()) {
                steps.put(step.position, step);
                String filename = "Step" + step.position;
                String path = getPath(courseDir, sectionDir, lessonName) + filename + ".java";
                MyFileInfoList.getInstance().addFileInfo(path, courseDir, sectionDir + "." + lessonName, filename);
                projectService.addPathStep(path, Integer.toString(step.id));
                projectService.addPathPackage(path, sectionDir + "." + lessonName);
            }
        }
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    private String getPath(String... titles) {
        StringBuilder sb = new StringBuilder();
        for (String title : titles) {
            sb.append(title + "/");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "\n\tMyLesson{" +
                "id=" + id +
                ", steps=" + steps +
                '}';
    }
}

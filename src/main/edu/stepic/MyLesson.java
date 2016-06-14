package main.edu.stepic;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import main.stepicConnector.NewProjectService;
import main.stepicConnector.StepicConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        NewProjectService projectService = NewProjectService.getInstance(project);

        List<MyStep> myStepsList = StepicConnector.getSteps(StepicConnector.getIdQuery(stepsId));

        for (MyStep step : myStepsList) {
            if (step.isTask()) {
                steps.put(step.position, step);
                String filename = "Step" + step.position;
                String path = getPath(courseDir, sectionDir, lessonName) + filename + ".java";
                StepInfo tmp = new StepInfo(Integer.toString(step.id), sectionDir + "." + lessonName, filename);
                projectService.addStepInfo(path, tmp);
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

package main.courseFormat;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.Utils;
import main.stepicConnector.NewProjectService;
import main.stepicConnector.StepicConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lesson {

    private static final Logger LOG = Logger.getInstance(Lesson.class);
    int id;
    @SerializedName("steps")
    List<Integer> stepsId;

    public String title;
    private String lessonName;

    public transient Map<Integer, Step> steps = new HashMap<>();
    private int lessonNo;

    public void build(int lessonNo, String courseDir, String sectionDir, Project project) throws UnirestException {
        this.lessonNo = lessonNo;
        NewProjectService projectService = NewProjectService.getInstance(project);

        List<Step> stepsList = StepicConnector.getSteps(Utils.getIdQuery(stepsId));

        for (Step step : stepsList) {
            if (step.isTask()) {
                steps.put(step.position, step);
                String filename = "Step" + step.position;
                String path = getPath(courseDir, sectionDir, lessonName) + filename + ".java";
                StepInfo tmp = new StepInfo(Integer.toString(step.id), sectionDir + "." + lessonName, filename, step.getText());
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
        return "\n\tLesson{" +
                "id=" + id +
                ", steps=" + steps +
                '}';
    }
}

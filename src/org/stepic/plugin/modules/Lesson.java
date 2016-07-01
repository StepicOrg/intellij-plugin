package org.stepic.plugin.modules;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.stepic.plugin.stepicConnector.StepicConnector;
import org.stepic.plugin.storages.CourseDefinitionStorage;
import org.stepic.plugin.utils.Utils;

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

    public void build(int lessonNo, String courseDir, String sectionDir, Project project) {
        this.lessonNo = lessonNo;
        CourseDefinitionStorage projectService = CourseDefinitionStorage.getInstance(project);

        List<Step> stepsList = StepicConnector.getSteps(Utils.getIdQuery(stepsId), project);

        for (Step step : stepsList) {
            if (step.isCode()) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lesson lesson = (Lesson) o;

        if (id != lesson.id) return false;
        if (lessonNo != lesson.lessonNo) return false;
        if (stepsId != null ? !stepsId.equals(lesson.stepsId) : lesson.stepsId != null) return false;
        if (title != null ? !title.equals(lesson.title) : lesson.title != null) return false;
        if (lessonName != null ? !lessonName.equals(lesson.lessonName) : lesson.lessonName != null) return false;
        return steps != null ? steps.equals(lesson.steps) : lesson.steps == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (stepsId != null ? stepsId.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (lessonName != null ? lessonName.hashCode() : 0);
        result = 31 * result + (steps != null ? steps.hashCode() : 0);
        result = 31 * result + lessonNo;
        return result;
    }
}

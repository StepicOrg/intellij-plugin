package main.courseFormat;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.project.Project;
import com.mashape.unirest.http.exceptions.UnirestException;
import main.Utils;
import main.projectWizard.YaTranslator;
import main.stepicConnector.StepicConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Section {
    int id;
    int course;
    public String position;
    public String title;
    private String sectionName;

    @SerializedName("units")
    List<Integer> unitsId;
    public transient Map<Integer, Lesson> lessons = new HashMap<>();

    private int sectionNo;


    public void build(int sectionNo, String courseDir, Project project) throws UnirestException {
        this.sectionNo = sectionNo;
        int lessonNo = 0;

        List<Unit> units = StepicConnector.getUnits(Utils.getIdQuery(unitsId));
        if (units == null) return;
        List<Integer> lessonsId = new ArrayList<>();
        List<String> lessonNames = new ArrayList<>();
        units.forEach(x -> lessonsId.add(x.getLessonId()));
        List<Lesson> lessons = StepicConnector.getLessons(Utils.getIdQuery(lessonsId));
        lessons.forEach(x -> lessonNames.add(x.title));

        List<String> newLessonNames = YaTranslator.translateNames(lessonNames, "lesson", project);

        for (Lesson lesson : lessons) {
            lesson.setLessonName(newLessonNames.get(lessonNo));
            this.lessons.put(++lessonNo, lesson);
            lesson.build(lessonNo, courseDir, sectionName, project);
        }
    }

    public String getSectionName() {
        return sectionName;
    }

    @Override
    public String toString() {
        return "\nSection{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", lessons=" + lessons +
                '}';
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}

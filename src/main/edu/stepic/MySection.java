package main.edu.stepic;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.project.Project;
import main.projectWizard.YaTranslator;
import main.stepicConnector.StepicConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.stepicConnector.StepicConnector.getIdQuery;

/**
 * Created by Petr on 02.05.2016.
 */
public class MySection {
    int id;
    int course;
    public String position;
    public String title;
    private String sectionName;

    @SerializedName("units")
    List<Integer> unitsId;
    public transient Map<Integer, MyLesson> lessons = new HashMap<>();

    private int sectionNo;


    public void build(int sectionNo, String courseDir, Project project) {
        this.sectionNo = sectionNo;
        int lessonNo = 0;

        List<MyUnit> myUnits = StepicConnector.getUnits(getIdQuery(unitsId));
        if (myUnits == null) return;
        List<Integer> lessonsId = new ArrayList<>();
        List<String> lessonNames = new ArrayList<>();
        myUnits.forEach(x -> lessonsId.add(x.getLessonId()));
        List<MyLesson> myLessons = StepicConnector.getLessons(getIdQuery(lessonsId));
        myLessons.forEach(x -> lessonNames.add(x.title));

        List<String> newLessonNames = YaTranslator.translateNames(lessonNames, "lesson", project);

        for (MyLesson lesson : myLessons) {
            lesson.setLessonName(newLessonNames.get(lessonNo));
            lessons.put(++lessonNo, lesson);
            lesson.build(lessonNo, courseDir, sectionName, project);
        }
    }

    public String getSectionName() {
        return sectionName;
    }

    @Override
    public String toString() {
        return "\nMySection{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", lessons=" + lessons +
                '}';
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}

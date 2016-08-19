package org.stepik.plugin.modules;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.project.Project;
import org.stepik.plugin.stepikConnector.StepikConnector;
import org.stepik.plugin.utils.Utils;
import org.stepik.plugin.utils.YaTranslator;

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


    public void build(int sectionNo, String courseDir, Project project) {
        this.sectionNo = sectionNo;
        int lessonNo = 0;

        List<Unit> units = StepikConnector.getUnits(Utils.getIdQuery(unitsId), project);
        if (units == null) return;
        List<Integer> lessonsId = new ArrayList<>();
        List<String> lessonNames = new ArrayList<>();
        units.forEach(x -> lessonsId.add(x.getLessonId()));
        List<Lesson> lessons = StepikConnector.getLessons(Utils.getIdQuery(lessonsId), project);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        if (id != section.id) return false;
        if (course != section.course) return false;
        if (sectionNo != section.sectionNo) return false;
        if (position != null ? !position.equals(section.position) : section.position != null) return false;
        if (title != null ? !title.equals(section.title) : section.title != null) return false;
        if (sectionName != null ? !sectionName.equals(section.sectionName) : section.sectionName != null) return false;
        if (unitsId != null ? !unitsId.equals(section.unitsId) : section.unitsId != null) return false;
        return lessons != null ? lessons.equals(section.lessons) : section.lessons == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + course;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (sectionName != null ? sectionName.hashCode() : 0);
        result = 31 * result + (unitsId != null ? unitsId.hashCode() : 0);
        result = 31 * result + (lessons != null ? lessons.hashCode() : 0);
        result = 31 * result + sectionNo;
        return result;
    }
}

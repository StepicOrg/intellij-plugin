package org.stepik.plugin.modules;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.project.Project;
import org.stepik.plugin.stepikConnector.StepikConnector;
import org.stepik.plugin.storages.CourseDefinitionStorage;
import org.stepik.plugin.utils.Utils;
import org.stepik.plugin.utils.YaTranslator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    public int id;
    public String title;
    public String summary;
    private String courseName;

    @SerializedName("sections")
    public List<Integer> sectionsId;

    public transient Map<Integer, Section> sections = new HashMap<>();

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sections=" + sections +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course that = (Course) o;
        return that.id == this.id;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (courseName != null ? courseName.hashCode() : 0);
        result = 31 * result + (sectionsId != null ? sectionsId.hashCode() : 0);
        result = 31 * result + (sections != null ? sections.hashCode() : 0);
        return result;
    }

    public void build(String root, Project project) {
        int sectionNo = 0;
        List<Section> sectionList = StepikConnector.getSections(Utils.getIdQuery(sectionsId), project);
        List<String> sectionNames = new ArrayList<>();
        sectionList.forEach(x -> sectionNames.add(x.title));

        List<String> newSectionNames = YaTranslator.translateNames(sectionNames, "section", project);

        for (Section section : sectionList) {
            section.setSectionName(newSectionNames.get(sectionNo));
            sections.put(++sectionNo, section);
            section.build(sectionNo, root + "/" + getName(project), project);
        }
    }

    public String getName(Project project) {
        if (courseName == null) {
            CourseDefinitionStorage ws = CourseDefinitionStorage.getInstance(project);
            if (ws.isTranslate()) {
                courseName = YaTranslator.translateRuToEn(title);
            } else {
                courseName = "course";
            }
        }
        return Utils.normalize(courseName);
    }
}

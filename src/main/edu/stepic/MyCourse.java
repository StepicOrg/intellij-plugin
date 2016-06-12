package main.edu.stepic;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.project.Project;
import main.projectWizard.YaTranslator;
import main.stepicConnector.StepicConnector;
import main.stepicConnector.StepicProjectService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.stepicConnector.StepicConnector.getSections;

/**
 * Implementation of class which contains information to be shawn in course description in tool window
 * and when project is being created
 */
public class MyCourse {
    public int id;
    public String title;
    public String summary;
    private String courseName;

    @SerializedName("sections")
    public List<Integer> sectionsId;

    public transient Map<Integer, MySection> sections = new HashMap<>();

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "MyCourse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sections=" + sections +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyCourse that = (MyCourse) o;
        if (that.id == this.id) return true;
        return false;
    }

    public void build(String root, Project project) {
        int sectionNo = 0;
        List<MySection> mySectionList = getSections(StepicConnector.getIdQuery(sectionsId));
        List<String> sectionNames = new ArrayList<>();
        mySectionList.forEach(x -> sectionNames.add(x.title));

        List<String> newSectionNames = YaTranslator.translateNames(sectionNames, "section", project);

        for (MySection section : mySectionList) {
            section.setSectionName(newSectionNames.get(sectionNo));
            sections.put(++sectionNo, section);
            section.build(sectionNo, root + "/" + getName(project), project);
        }
    }

    public String getName(Project project) {
        if (courseName == null) {
            StepicProjectService ws = StepicProjectService.getInstance(project);
            if (ws.isTranslate()) {
                courseName =  YaTranslator.translateRuToEn(title);
            } else {
                courseName =  "course";
            }
        }
        return StringUtils.normalize(courseName);
    }
}

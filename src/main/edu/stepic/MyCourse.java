package main.edu.stepic;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.project.Project;
import main.projectWizard.YaTranslator;
import main.stepicConnector.WorkerService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.stepicConnector.StepicConnector.getSection;

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
        for (Integer sectionId : sectionsId) {
            MySection section = getSection(Integer.toString(sectionId));
            sections.put(++sectionNo, section);
            section.build(sectionNo, root + "/" + getName(), project);
        }
    }

    public String getName() {
        if (courseName == null) {
            WorkerService ws = WorkerService.getInstance();
//            if (ws.isTranslate()) {
            if (true) {
                courseName =  YaTranslator.translateRuToEn(title).replace('\"', ' ').replace(' ', '_').replace(':', '.');
            } else {
                courseName =  "course";
            }
        }
        return courseName;
    }
}

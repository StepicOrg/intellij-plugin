package main.edu.stepic;

import com.google.gson.annotations.SerializedName;
import com.intellij.ide.util.PropertiesComponent;
import main.projectWizard.YaTranslator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.stepicConnector.StepicConnector.getLesson;
import static main.stepicConnector.StepicConnector.getUnit;

/**
 * Created by Petr on 02.05.2016.
 */
public class MySection {
    int id;
    int course;
    public String position;
    public String title;
    @SerializedName("units")
    List<Integer> unitsId;
    public transient Map<Integer, MyLesson> lessons = new HashMap<>();

    private int sectionNo;


    public void build(int sectionNo, String courseDir) {
        this.sectionNo = sectionNo;
        int lessonNo = 0;
        for (Integer unitId : unitsId) {
            int lessonId = getUnit(Integer.toString(unitId)).getLessonId();
            MyLesson lesson = getLesson(Integer.toString(lessonId));
            lessons.put(++lessonNo, lesson);
            lesson.build(lessonNo, courseDir, getName(sectionNo));
        }
    }

    private String getName(int sectionNo) {
        PropertiesComponent props = PropertiesComponent.getInstance();
        if (props.getValue("translate").equals("1")) {
            return "_" + sectionNo + "." + YaTranslator.translateRuToEn(title).replace('\"',' ').replace(' ','_').replace(':','.');
        } else {
            return "section" + sectionNo;
        }
    }

    @Override
    public String toString() {
        return "\nMySection{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", lessons=" + lessons +
                '}';
    }
}

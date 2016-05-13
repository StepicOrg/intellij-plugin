package main.edu.stepic;

import com.google.gson.annotations.SerializedName;

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


    public void build() {
        int count = 0;
        for (Integer unitId : unitsId) {
            int lessonId = getUnit(Integer.toString(unitId)).getLessonId();
            MyLesson lesson = getLesson(Integer.toString(lessonId));
            lessons.put(++count, lesson);
            lesson.build();
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

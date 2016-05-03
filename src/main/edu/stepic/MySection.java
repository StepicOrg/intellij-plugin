package main.edu.stepic;

import com.google.gson.annotations.SerializedName;
import com.mashape.unirest.http.exceptions.UnirestException;

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
    String title;
    @SerializedName("units")
    List<Integer> unitsId;

    Map<Integer, MyLesson> lessons = new HashMap<>();


    public void build() throws UnirestException {
        for (Integer unitId : unitsId) {
            int lessonId = getUnit(Integer.toString(unitId)).getLessonId();
            MyLesson lesson = getLesson(Integer.toString(lessonId));
            lessons.put(unitId, lesson);
        }
    }
}

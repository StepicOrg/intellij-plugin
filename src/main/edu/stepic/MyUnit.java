package main.edu.stepic;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Petr on 02.05.2016.
 */
public class MyUnit {
    int id;
    @SerializedName("lesson")
    private int lessonId;

    public int getLessonId() {
        return lessonId;
    }
}

package main.edu.stepic;

import com.google.gson.annotations.SerializedName;

public class MyUnit {
    int id;
    @SerializedName("lesson")
    private int lessonId;

    public int getLessonId() {
        return lessonId;
    }
}

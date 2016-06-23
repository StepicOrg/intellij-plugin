package org.stepic.plugin.modules;

import com.google.gson.annotations.SerializedName;

public class Unit {
    int id;
    @SerializedName("lesson")
    private int lessonId;

    public int getLessonId() {
        return lessonId;
    }
}

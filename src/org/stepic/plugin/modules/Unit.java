package org.stepic.plugin.modules;

import com.google.gson.annotations.SerializedName;

public class Unit {
    int id;
    @SerializedName("lesson")
    private int lessonId;

    public int getLessonId() {
        return lessonId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unit unit = (Unit) o;

        if (id != unit.id) return false;
        return lessonId == unit.lessonId;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + lessonId;
        return result;
    }
}

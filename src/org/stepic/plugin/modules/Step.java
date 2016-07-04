package org.stepic.plugin.modules;

import java.util.Map;

public class Step {
    int id;
    int lesson;
    int position;
    Map<String, ?> block;

    public boolean isCode() {
        String tmp = (String) block.get("name");
        return tmp.equals("code");
    }

    public String getText() {
        return (String) block.get("text");
    }

    @Override
    public String toString() {
        return "\n\t\tStep{" +
                "position=" + position +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Step step = (Step) o;

        if (id != step.id) return false;
        if (lesson != step.lesson) return false;
        if (position != step.position) return false;
        return block != null ? block.equals(step.block) : step.block == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + lesson;
        result = 31 * result + position;
        result = 31 * result + (block != null ? block.hashCode() : 0);
        return result;
    }
}

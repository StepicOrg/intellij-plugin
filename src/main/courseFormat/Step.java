package main.courseFormat;

import java.util.Map;

public class Step {
    int id;
    int lesson;
    int position;
    Map<String, ? extends Object> block;

    public boolean isTask() {
        String tmp = (String) block.get("name");
        if (tmp.equals("code"))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "\n\t\tStep{" +
                "position=" + position +
                '}';
    }
}
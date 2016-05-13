package main.edu.stepic;

import java.util.Map;

/**
 * Created by Petr on 04.05.2016.
 */
public class MyStep {
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
        return "\n\t\tMyStep{" +
                "position=" + position +
                '}';
    }
}

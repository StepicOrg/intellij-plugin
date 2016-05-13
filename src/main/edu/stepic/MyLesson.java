package main.edu.stepic;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static main.stepicConnector.StepicConnector.getStep;

/**
 * Created by Petr on 02.05.2016.
 */
public class MyLesson {
    int id;
    @SerializedName("steps")
    List<Integer> stepsId;

    public String title;

    public transient Map<Integer, MyStep> steps = new HashMap<>();

    public void build() {
        for (Integer stepId : stepsId) {
            MyStep step = getStep(Integer.toString(stepId));
            if (step.isTask())
                steps.put(step.position, step);
        }
    }

    @Override
    public String toString() {
        return "\n\tMyLesson{" +
                "id=" + id +
                ", steps=" + steps +
                '}';
    }
}

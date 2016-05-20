package main.edu.stepic;

import com.google.gson.annotations.SerializedName;
import main.stepicConnector.WorkerService;

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
        Map<String,String> map = WorkerService.getInstance().getMetaFileInfo();
        for (Integer stepId : stepsId) {
            MyStep step = getStep(Integer.toString(stepId));
            if (step.isTask()) {
                steps.put(step.position, step);
//                map.put()
            }
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

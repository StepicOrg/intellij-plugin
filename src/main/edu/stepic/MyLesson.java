package main.edu.stepic;

import com.google.gson.annotations.SerializedName;
import com.intellij.openapi.project.Project;
import main.projectWizard.MyFileInfoList;
import main.projectWizard.YaTranslator;
import main.stepicConnector.WS3;
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
    private int lessonNo;

    public void build(int lessonNo, String courseDir, String sectionDir, Project project) {
        this.lessonNo = lessonNo;
        WS3 ws3 = WS3.getInstance(project);
        for (Integer stepId : stepsId) {
            MyStep step = getStep(Integer.toString(stepId));
            if (step.isTask()) {
                steps.put(step.position, step);
                String filename = "Step" + step.position;
                String path = getPath(courseDir, sectionDir, getName(lessonNo)) + filename + ".java";
                MyFileInfoList.getInstance().addFileInfo(path, courseDir, sectionDir + "." + getName(lessonNo), filename);
                ws3.addPathStep(path, Integer.toString(stepId));
            }
        }
    }


    private String getName(int lessonNo) {
        WorkerService ws = WorkerService.getInstance();
        if (ws.isTranslate()) {
            return "_" + lessonNo + "." + YaTranslator.translateRuToEn(title).replace('\"', ' ').replace(' ', '_').replace(':', '.');
        } else {
            return "lesson" + lessonNo;
        }
    }


    private String getPath(String... titles) {
        StringBuilder sb = new StringBuilder();
        for (String title : titles) {
//            sb.append(title + File.separator);
            sb.append(title + "/");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "\n\tMyLesson{" +
                "id=" + id +
                ", steps=" + steps +
                '}';
    }
}

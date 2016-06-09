package main.actions.popupMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import main.stepicConnector.StepicConnector;
import main.edu.stepic.SubmissionsNode;
import main.stepicConnector.WS2;
import main.stepicConnector.WS3;

import java.util.List;

/**
 * Created by Petr on 21.05.2016.
 */
public class GetStepStatus extends PopupMenuAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

//        WorkerService ws = WorkerService.getInstance();
        WS3 ws = WS3.getInstance(project);
//        WS3 ws = WS3.getInstance();
        String stepId = ws.getStepId(vf.getPath());


//        StepicConnector.initToken();
        String ans = "";
        int size = StepicConnector.getStatusTask(stepId, Pair.pair("status", "correct")).size();
        if (size > 0) {
            ans = "Step was solved";
        } else {
            ans = "Step wasn't solved";
        }

        WS2 ws2 = WS2.getInstance(project);
//        WS2 ws2 = WS2.getInstance();

        String attemptId = ws2.getAttemptId(stepId);
//        String attemptId = "";
        if (!attemptId.equals("")) {
            List<SubmissionsNode> list = StepicConnector.getStatusTask(stepId, Pair.pair("attempt", attemptId));
            if (!list.isEmpty()) {
                SubmissionsNode max = list.get(0);
                for (SubmissionsNode node : list) {
                    if (max.getTime().compareTo(node.getTime()) < 0)
                        max = node;
                }

                ans += "\nlast sending is " + max.getStatus();
            } else {
                ans += "\nlast sending is unknown";
            }
        } else {
            ans += "\nlast sending is unknown";
        }

        Messages.showMessageDialog(project, ans, "Information", Messages.getInformationIcon());

    }
}

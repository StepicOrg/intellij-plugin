package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import main.edu.stepic.MyCourse;
import main.stepicConnector.StepicConnector;

/**
 * Created by Petr on 20.04.2016.
 */
public class GetCourse extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getData(PlatformDataKeys.PROJECT);

        String courseId = Messages.showInputDialog(project, "Please, input a course_id", "Get a course", Messages.getQuestionIcon());

        StepicConnector.initToken();

        MyCourse course = StepicConnector.getCourse(courseId);
        course.build();
        Messages.showMessageDialog(project, course.toString(), "Information", Messages.getInformationIcon());

    }
}

package main.actions.mainMenu;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

/**
 * Created by Petr on 09.06.2016.
 */
public class SomeAction extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
//        Object navigatable = anActionEvent.getData(CommonDataKeys.NAVIGATABLE);
//        if (navigatable != null) {
//            Messages.showDialog(navigatable.toString(), "Selected Element: ", new String[]{"OK"}, -1, null);
//        }

//        BalloonBuilder balloonBuilder =
//                JBPopupFactory.getInstance().createHtmlTextBalloonBuilder(getNavigationFinishedMessage(), MessageType.INFO, null);
//        Balloon balloon = balloonBuilder.createBalloon();
//        assert studyEditor != null;
//        balloon.show(StudyUtils.computeLocation(studyEditor.getEditor()), Balloon.Position.above);

//        JBPopupFactory.getInstance().createMessage("test");

        final Application application = ApplicationManager.getApplication();
//        application.executeOnPooledThread(new Runnable() {
//            @Override
//            public void run() {
        final Notification notification =
                new Notification("Update.course", "Course update", "Current course is synchronized", NotificationType.INFORMATION);
        notification.notify(project);

//            }
//        }
    }


    public void update(AnActionEvent anActionEvent) {
        final Project project = anActionEvent.getData(CommonDataKeys.PROJECT);
        if (project == null)
            return;
        Object navigatable = anActionEvent.getData(CommonDataKeys.NAVIGATABLE);
        anActionEvent.getPresentation().setEnabled(navigatable != null);
    }
}

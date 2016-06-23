package org.stepic.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created by Petr on 09.06.2016.
 */
public class SomeAction extends MainMenuAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
//        Project project = anActionEvent.getProject();
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

//        final Application application = ApplicationManager.getApplication();
//        final Notification notification =
//                new Notification("Update.course", "Course update", "Current course is synchronized", NotificationType.INFORMATION);
//        notification.notify(project);

//        JComponent jComponent = new JBPopupMenu("Test");
//        BalloonBuilder balloonBuilder = JBPopupFactory.getInstance()..createBalloonBuilder(jComponent);
//        Balloon balloon = balloonBuilder.createBalloon();
//        balloon.show();


    }


    public void update(AnActionEvent anActionEvent) {
    }
}

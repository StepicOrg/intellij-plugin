package org.stepik.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.stepik.plugin.modules.Course;
import org.stepik.plugin.modules.StepInfo;
import org.stepik.plugin.projectWizard.StepikModuleBuilder;
import org.stepik.plugin.stepikConnector.StepikConnector;
import org.stepik.plugin.storages.ActionVisibleProperties;
import org.stepik.plugin.storages.CourseDefinitionStorage;
import org.stepik.plugin.storages.StepikApplicationStorage;
import org.stepik.plugin.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UpdateCourse extends MainMenuAction {
    private static final Logger LOG = Logger.getInstance(UpdateCourse.class);
    private static String success = "Course is successfully updated.";
    private static String error = "Course was not updated.";
    private static String old = "No new lessons.";
    private static String nnew = "New lessons are created.";
    private static String course_is_broken = "Your course_no is broke.\nPlease insert correct course_no or course_link.";
    private static String login_is_broken = "Your login is broke.\nPlease insert your e-mail on Stepik account.";
    private static String password_is_broken = "Your password is broke.\nPlease insert your password on Stepik account.";

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();

        final VirtualFile root = project.getBaseDir();
        CourseDefinitionStorage projectService = CourseDefinitionStorage.getInstance(project);
        String courseID = projectService.getCourseID();
        String login = StepikConnector.getLogin(project);
        projectService.setProjectName(project.getName());


        ActionVisibleProperties prop = ActionVisibleProperties.getInstance(project);
        if (isCourseBroke(courseID, login) || !prop.isVisible()) {
            if (Messages.showYesNoDialog(project, "Your project of course is broke.\nDo you want to repair it?", "Repair", Messages.getQuestionIcon()) == 0) {
                if (courseID == null || courseID.isEmpty()) {
                    repairCourseID(project);
                }

                if (login == null || login.isEmpty()) {
                    repairLoginAndPassword(project);
                }

                prop.setEnabled(true);
                prop.setVisible(true);
            } else {
                return;
            }
        }
        StepikConnector.initToken(project);


        Map<String, StepInfo> map;
        Set<String> newFiles = new HashSet<>();
        Course course = StepikConnector.getCourse(courseID, project);

        Utils.refreshFiles(project);
        map = new HashMap<>(projectService.getMapPathInfo());
        course.build(root.getPath(), project);

        projectService.mapPathInfo.entrySet().forEach(x -> {
            if (!map.containsKey(x.getKey())) {
                String path = x.getKey();
                StepInfo stepInfo = x.getValue();

                File file = new File(path);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    newFiles.add(path);
                    try {
                        Path path1 = Paths.get(path);
                        Files.write(path1, StepikModuleBuilder.getText(stepInfo.getFilename(), stepInfo.getPackageName()), Charset.forName("UTF-8"));
                    } catch (IOException ex) {
                        LOG.error("Create file error\n" + ex.getMessage());
                    }
                }
            }
        });

        LocalFileSystem.getInstance().refresh(true);

        StringBuilder sb = new StringBuilder();
        sb.append(success + "\n");
        if (newFiles.isEmpty()) {
            sb.append(old);
        } else {
            sb.append(nnew);
        }
        Messages.showMessageDialog(project, sb.toString(), "Information", Messages.getInformationIcon());

    }

    private boolean isCourseBroke(String courseID, String login) {
        return courseID == null || courseID.isEmpty() || login == null || login.isEmpty();
    }

    private void repairCourseID(Project project) {
        CourseDefinitionStorage projectService = CourseDefinitionStorage.getInstance(project);
        String courseID = Messages.showInputDialog(project, course_is_broken, "Repair", Messages.getQuestionIcon());
        courseID = Utils.parseUrl(courseID);
        boolean translate = Messages.showYesNoDialog(project, "Do you want to translate package names?", "Translator", Messages.getQuestionIcon()) == 0;
        projectService.setCourseID(courseID);
        projectService.setTranslate(translate);
        ActionVisibleProperties prop = ActionVisibleProperties.getInstance(project);
        prop.setEnabled(true);
        prop.setVisible(true);
    }

    private void repairLoginAndPassword(Project project) {
        String login = Messages.showInputDialog(project, login_is_broken, "Repair", Messages.getQuestionIcon(), StepikApplicationStorage.getInstance().getLogin(), null);

        if (StepikConnector.isPasswordSet(project)) {
            String password = Messages.showInputDialog(project, password_is_broken, "Repair", Messages.getQuestionIcon());
            StepikConnector.setLoginAndPassword(login, password, project);
        } else {
            StepikConnector.setLogin(login, project);
        }
    }

    @Override
    public void update(AnActionEvent e) {
//        super.update(e);
    }
}

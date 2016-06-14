package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import main.Utils;
import main.edu.stepic.Course;
import main.edu.stepic.StepInfo;
import main.projectWizard.StepicModuleBuilder;
import main.stepicConnector.NewProjectService;
import main.stepicConnector.StepicConnector;

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

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        StepicConnector.initToken();

        final VirtualFile root = project.getBaseDir();
        NewProjectService projectService = NewProjectService.getInstance(project);
        String courseID = projectService.getCourseID();
        Course course = StepicConnector.getCourses(courseID).get(0);

        Set<String> newFiles = new HashSet<>();

        Utils.refreshFiles(project);
        Map<String, StepInfo> map = new HashMap<>(projectService.getMapPathInfo());
        course.build(root.getPath(), project);

        projectService.mapPathInfo.entrySet().forEach(x -> {
            if (map.containsKey(x.getKey())) {

            } else {
                String path = x.getKey();
                StepInfo stepInfo = x.getValue();

                newFiles.add(path);
                File file = new File(path);
                file.getParentFile().mkdirs();
                try {
                    Path path1 = Paths.get(path);
                    Files.write(path1, StepicModuleBuilder.getText(stepInfo.getFilename(), stepInfo.getPackageName()), Charset.forName("UTF-8"));
                } catch (IOException ex) {
                    LOG.error("Create file error\n" + ex.getMessage());
                }
            }
        });

        LocalFileSystem.getInstance().refresh(true);

        StringBuilder sb = new StringBuilder();
        sb.append(success+"\n");
        if (newFiles.isEmpty()){
            sb.append(old);
        } else {
            sb.append(nnew);
        }
//        newFiles.forEach((x) -> sb.append(x.toString() + "\n"));
        Messages.showMessageDialog(project, sb.toString(), "Information", Messages.getInformationIcon());

    }

    @Override
    public void update(AnActionEvent e) {
        super.update(e);
    }
}

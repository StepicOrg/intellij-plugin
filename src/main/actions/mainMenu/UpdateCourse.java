package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import main.edu.stepic.MyCourse;
import main.projectWizard.MyFileInfoList;
import main.projectWizard.StepicModuleBuilder;
import main.stepicConnector.StepicConnector;
import main.stepicConnector.WS3;
import main.stepicConnector.WorkerService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Petr on 31.05.2016.
 */
public class UpdateCourse extends AnAction {
    private static final Logger LOG = Logger.getInstance(UpdateCourse.class);

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();

        final VirtualFile root = project.getBaseDir();
        String courseID = WorkerService.getInstance().getCourseID();

        MyCourse course = StepicConnector.getCourses(courseID).get(0);

        WS3 ws3 = WS3.getInstance(e.getProject());

        Set<String> nFiles = new HashSet<>();
        nFiles.addAll(ws3.getMapPathStep().keySet());
        Set<String> nnFiles = new HashSet<>();

        course.build(root.getPath(),e.getProject());
//        MyLesson lesson = StepicConnector.getLesson("28340");
//        lesson.build(1, root.getPath() + "/course", "section", project);

        MyFileInfoList.getInstance().getList().forEach((x) -> {
            if (nFiles.contains(x.path)) {

            } else {
                nnFiles.add(x.path);
                File f = new File(x.path);
                f.getParentFile().mkdirs();
                try {
                    Path path1 = Paths.get(x.path);
                    Files.write(path1, StepicModuleBuilder.getText(x.filename, x.pack), Charset.forName("UTF-8"));
                } catch (IOException ex) {
                LOG.error("Create file error\n" + ex.getMessage());
                }
            }
        });
        MyFileInfoList.getInstance().clear();
        LocalFileSystem.getInstance().refresh(true);

        StringBuilder sb = new StringBuilder();
        nnFiles.forEach((x) -> sb.append(x.toString() + "\n"));
        Messages.showMessageDialog(project, sb.toString(), "Information", Messages.getInformationIcon());

    }


}

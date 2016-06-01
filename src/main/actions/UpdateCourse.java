package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import main.edu.stepic.MyLesson;
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

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
//        project

        final VirtualFile root = project.getBaseDir();
        String courseLink = WorkerService.getInstance().getCourseLink();
//        LOG.warn("build course structure " + courseLink);
//        LOG.warn("build course structure " + root.getPath());

//        Messages.showInputDialog(e.getProject(), , "Sing in", Messages.getQuestionIcon());


//        MyCourse course = StepicConnector.getCourse(courseLink);
//        course.build(root.getPath(),e.getProject());
        WS3 ws = WS3.getInstance(e.getProject());

        Set<String> nFiles = ws.getMapPathStep().keySet();
        Set<String> nnFiles = new HashSet<>();

        MyLesson lesson = StepicConnector.getLesson("28340");
        lesson.build(1,root.getPath()+"/course","section",project);

        MyFileInfoList.getInstance().getList().forEach( (x) ->{
            if (nFiles.contains(x.path)) {

            } else {
                nnFiles.add(x.path);
                File f = new File(x.path);
                f.getParentFile().mkdirs();
                try {
                    Path path1 = Paths.get(x.path);
                    Files.write(path1, StepicModuleBuilder.getText(x.filename, x.pack), Charset.forName("UTF-8"));
                } catch (IOException ex) {
//                LOG.error("Create file error\n" + ex.getMessage());
                }
            }
//            addSourcePath(Pair.create(x.source, x.pack));
        });


        StringBuilder sb = new StringBuilder();
        nnFiles.forEach((x) -> sb.append(x.toString() + "\n"));
        Messages.showMessageDialog(project, sb.toString(), "Information", Messages.getInformationIcon());

    }


}

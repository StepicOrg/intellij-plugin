package main.projectWizard;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import main.edu.stepic.MyCourse;
import main.stepicConnector.StepicConnector;
import main.stepicConnector.WS3;
import main.stepicConnector.WorkerService;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StepicModuleBuilder extends JavaModuleBuilder {
    private static final Logger LOG = Logger.getInstance(StepicModuleBuilder.class);

    @Override
    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        Project project = rootModel.getProject();
        final VirtualFile root = project.getBaseDir();

        WS3.getInstance(project).setProjectName(project.getName());
        StepicConnector.initToken();
        String courseId = WorkerService.getInstance().getCourseID();
        LOG.warn("build course structure " + courseId);
        LOG.warn("build course structure " + root.getPath());

        LOG.warn("root = " + root.getPath());

        MyCourse course = StepicConnector.getCourses(courseId).get(0);
        course.build(root.getPath(), rootModel.getProject());

        MyFileInfoList.getInstance().getList().forEach((x) -> {
            File f = new File(x.path);
            f.getParentFile().mkdirs();
            try {
                Path path1 = Paths.get(x.path);
                Files.write(path1, getText(x.filename, x.pack), Charset.forName("UTF-8"));
            } catch (IOException e) {
                LOG.error("Create file error\n" + e.getMessage());
            }
        });
        addSourcePath(Pair.create(root.getPath() + "/" + course.getName(),""));
        MyFileInfoList.getInstance().clear();

        super.setupRootModel(rootModel);
    }

    public static List<String> getText(String name, String pack) {
        List<String> text = new ArrayList<>();
        text.add("package " + pack + ";\n");
        text.add("class " + name + " {");
        text.add("\tpublic static void main(String[] args){");
        text.add("\t\t//Write your code here");
        text.add("\t}\n}");
        return text;
    }

    public ModuleType getModuleType() {
        return StepicModuleType.STEPIC_MODULE_TYPE;
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext,
                                                @NotNull ModulesProvider modulesProvider) {
        LOG.warn("Create Wizard Steps");
        return new StepicModuleWizardStep[]{new StepicModuleWizardStep(this, wizardContext)};
    }

}

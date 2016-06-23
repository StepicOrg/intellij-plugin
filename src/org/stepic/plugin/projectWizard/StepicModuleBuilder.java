package org.stepic.plugin.projectWizard;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.stepic.plugin.modules.Course;
import org.stepic.plugin.modules.StepInfo;
import org.stepic.plugin.storages.CourseDefinitionStorage;
import org.stepic.plugin.stepicConnector.StepicConnector;
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

        CourseDefinitionStorage projectService = CourseDefinitionStorage.getInstance(project);
//        StudentStorage studentService = StudentStorage.getInstance(project);

        PropertiesComponent props = PropertiesComponent.getInstance();
        projectService.setTranslate(props.getValue("translate").equals("true") ? true : false);
        projectService.setCourseID(props.getValue("courseId"));
        projectService.setProjectName(project.getName());

        StepicConnector.setLoginAndPassword(props.getValue("login"), props.getValue("password"), project);
//        studentService.setPassword();
        LOG.warn("login = " + props.getValue("login"));

        StepicConnector.initToken(project);
        String courseId = projectService.getCourseID();
        LOG.warn("build course structure " + root.getPath());

        String token = StepicConnector.getToken(project);
        Course course = null;
        try {
            course = StepicConnector.getCourses(courseId, project).get(0);
            course.build(root.getPath(), rootModel.getProject());
        } catch (UnirestException e) {
//            e.printStackTrace();
            Messages.showMessageDialog(project, "Build Course error", "Error", Messages.getErrorIcon());
        }


        projectService.mapPathInfo.entrySet().forEach(x -> {
            String path = x.getKey();
            StepInfo stepInfo = x.getValue();

            File file = new File(path);
            file.getParentFile().mkdirs();
            try {
                Path path1 = Paths.get(path);
                Files.write(path1, getText(stepInfo.getFilename(), stepInfo.getPackageName()), Charset.forName("UTF-8"));
            } catch (IOException e) {
                LOG.error("Create file error\n" + e.getMessage());
            }
        });

        addSourcePath(Pair.create(root.getPath() + "/" + course.getName(project), ""));

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
        return new StepicModuleWizardStep[]{new StepicModuleWizardStep(this, wizardContext)};
    }

}

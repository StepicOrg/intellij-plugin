package org.stepik.plugin.projectWizard;

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
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.stepik.plugin.modules.Course;
import org.stepik.plugin.modules.StepInfo;
import org.stepik.plugin.stepikConnector.StepikConnector;
import org.stepik.plugin.storages.ActionVisibleProperties;
import org.stepik.plugin.storages.CourseDefinitionStorage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StepikModuleBuilder extends JavaModuleBuilder {
    private static final Logger LOG = Logger.getInstance(StepikModuleBuilder.class);

    @Override
    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        Project project = rootModel.getProject();
        final VirtualFile root = project.getBaseDir();

        CourseDefinitionStorage projectService = CourseDefinitionStorage.getInstance(project);

        PropertiesComponent props = PropertiesComponent.getInstance();
        projectService.setTranslate(Boolean.parseBoolean(props.getValue("translate")));
//        projectService.setTranslate(props.getBoolean("translate"));
        projectService.setCourseID(props.getValue("courseId"));
        projectService.setProjectName(project.getName());

        ActionVisibleProperties visibleProperties = ActionVisibleProperties.getInstance(project);
        visibleProperties.setEnabled(true);
        visibleProperties.setVisible(true);

        StepikConnector.setLoginAndPassword(props.getValue("login"), props.getValue("password"), project);
        LOG.debug("login = " + props.getValue("login"));

        StepikConnector.initToken(project);
        String courseId = projectService.getCourseID();
        LOG.debug("build course structure " + root.getPath());

        Course course = StepikConnector.getCourse(courseId, project);
        course.build(root.getPath(), rootModel.getProject());

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
        return StepikModuleType.STEPIK_MODULE_TYPE;
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext,
                                                @NotNull ModulesProvider modulesProvider) {
        return new StepikModuleWizardStep[]{new StepikModuleWizardStep(this, wizardContext)};
    }

}

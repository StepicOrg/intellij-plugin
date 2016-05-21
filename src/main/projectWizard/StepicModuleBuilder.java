package main.projectWizard;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleWithNameAlreadyExists;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import main.edu.stepic.MyCourse;
import main.stepicConnector.StepicConnector;
import org.jdom.JDOMException;
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
//        Map<WorkerService.MyFileInfo,String> map = WorkerService.getInstance().getMetaFileInfo();
        final VirtualFile root = createAndGetContentEntry();

        PropertiesComponent props = PropertiesComponent.getInstance();
        String courseLink = props.getValue("courseLink");
        LOG.warn("build course structure " + courseLink);
        LOG.warn("build course structure " + root.getPath());

        StepicConnector.initToken();
        MyCourse course = StepicConnector.getCourse(courseLink);
        course.build(root.getPath());

//        map.keySet().forEach( (x) ->{
        MyFileInfoList.getInstance().getList().forEach( (x) ->{
            File f = new File(x.path);
            f.getParentFile().mkdirs();
            try {
                Path path1 = Paths.get(x.path);
                Files.write(path1, getText(x.filename, x.pack), Charset.forName("UTF-8"));
            } catch (IOException e) {
                LOG.error("Create file error\n" + e.getMessage());
            }
            addSourcePath(Pair.create(x.source, x.pack));
        });

        super.setupRootModel(rootModel);
    }

    private List<String> getText(String name, String pack) {
        List<String> text = new ArrayList<>();
        text.add("package " + pack + ";\n");
        text.add("class " + name + " {");
        text.add("\tpublic static void main(String[] args){");
        text.add("\t\t//Write your code here");
        text.add("\t}\n}");
        return text;
    }

    private static String getUrlByPath(final String path) {
        return VfsUtil.getUrlForLibraryRoot(new File(path));
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

    private VirtualFile createAndGetContentEntry() {
        String path = FileUtil.toSystemIndependentName(getContentEntryPath());
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }

    @NotNull
    @Override
    public Module createModule(@NotNull ModifiableModuleModel moduleModel) throws InvalidDataException, IOException, ModuleWithNameAlreadyExists, JDOMException, ConfigurationException {
        Module baseModule = super.createModule(moduleModel);

        return baseModule;
    }

}

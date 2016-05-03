package main.projectWizard;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class StepicModuleBuilder extends ModuleBuilder {

    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        final Project project = rootModel.getProject();

        final VirtualFile root = createAndGetContentEntry();
        rootModel.addContentEntry(root);

        // todo this should be moved to generic ModuleBuilder
        if (myJdk != null){
            rootModel.setSdk(myJdk);
        } else {
            rootModel.inheritSdk();
        }
    }

    public ModuleType getModuleType() {
        return StepicModuleType.STEPIC_MODULE;
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext,
                                                @NotNull ModulesProvider modulesProvider) {
        return new StepicModuleWizardStep[]{new StepicModuleWizardStep(this, wizardContext)};
    }

    private VirtualFile createAndGetContentEntry() {
        String path = FileUtil.toSystemIndependentName(getContentEntryPath());
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }
}

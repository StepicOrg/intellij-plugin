package org.stepik.plugin.projectWizard;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.psi.CommonClassNames;
import com.intellij.psi.JavaPsiFacade;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.java.JavaModuleSourceRootTypes;
import org.stepik.plugin.icons.PluginIcons;

import javax.swing.*;

public class StepikModuleType extends ModuleType<StepikModuleBuilder> {
    public static final String MODULE_NAME = "Stepik";
    public static final StepikModuleType STEPIK_MODULE_TYPE;

    static {
        STEPIK_MODULE_TYPE = (StepikModuleType) instantiate("org.stepik.plugin.projectWizard.StepikModuleType");
    }

    private static final String ID = "STEPIK_MODULE_TYPE";

    public StepikModuleType() {
        super(ID);
    }

    public static StepikModuleType getInstance() {
        return (StepikModuleType) ModuleTypeManager.getInstance().findByID(ID);
    }

    @NotNull
    @Override
    public StepikModuleBuilder createModuleBuilder() {
        return new StepikModuleBuilder();
    }

    @NotNull
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Stepik Module Type";
    }

    @Override
    public Icon getBigIcon() {
        return PluginIcons.STEPIK_LOGO;
    }

    @Override
    public Icon getNodeIcon(@Deprecated boolean b) {
        return PluginIcons.STEPIK_LOGO;
    }

    @NotNull
    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull StepikModuleBuilder moduleBuilder, @NotNull ModulesProvider modulesProvider) {
//        return super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider);
        return new StepikModuleWizardStep[]{new StepikModuleWizardStep(moduleBuilder, wizardContext)};
    }

    @NotNull
    private static ModuleType instantiate(String className) {
        try {
            return (ModuleType) Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Nullable
    @Override
    public ModuleWizardStep modifyProjectTypeStep(@NotNull SettingsStep settingsStep, @NotNull final ModuleBuilder moduleBuilder) {
        return ProjectWizardStepFactory.getInstance().createJavaSettingsStep(settingsStep, moduleBuilder, moduleBuilder::isSuitableSdkType);
    }

    @Override
    public boolean isValidSdk(@NotNull final Module module, final Sdk projectSdk) {
        return isValidJavaSdk(module);
    }

    private static boolean isValidJavaSdk(@NotNull Module module) {
        if (ModuleRootManager.getInstance(module).getSourceRoots(JavaModuleSourceRootTypes.SOURCES).isEmpty())
            return true;
        return JavaPsiFacade.getInstance(module.getProject()).findClass(CommonClassNames.JAVA_LANG_OBJECT,
                module.getModuleWithLibrariesScope()) != null;
    }
}
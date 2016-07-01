package org.stepic.plugin.projectWizard;

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
import org.stepic.plugin.icons.PluginIcons;

import javax.swing.*;

public class StepicModuleType extends ModuleType<StepicModuleBuilder> {
    public static final String MODULE_NAME = "Stepic";
    public static final StepicModuleType STEPIC_MODULE_TYPE;

    static {
        STEPIC_MODULE_TYPE = (StepicModuleType) instantiate("org.stepic.plugin.projectWizard.StepicModuleType");
    }

    private static final String ID = "STEPIC_MODULE_TYPE";

    public StepicModuleType() {
        super(ID);
    }

    public static StepicModuleType getInstance() {
        return (StepicModuleType) ModuleTypeManager.getInstance().findByID(ID);
    }

    @NotNull
    @Override
    public StepicModuleBuilder createModuleBuilder() {
        return new StepicModuleBuilder();
    }

    @NotNull
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Stepic Module Type";
    }

    @Override
    public Icon getBigIcon() {
        return PluginIcons.STEPIC_LOGO;
    }

    @Override
    public Icon getNodeIcon(@Deprecated boolean b) {
        return PluginIcons.STEPIC_LOGO;
    }

    @NotNull
    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull StepicModuleBuilder moduleBuilder, @NotNull ModulesProvider modulesProvider) {
//        return super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider);
        return new StepicModuleWizardStep[]{new StepicModuleWizardStep(moduleBuilder, wizardContext)};
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
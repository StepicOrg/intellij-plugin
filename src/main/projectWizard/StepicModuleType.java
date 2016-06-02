package main.projectWizard;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.Condition;
import main.icons.PluginIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class StepicModuleType extends ModuleType<StepicModuleBuilder> {
    public static final String MODULE_NAME = "Stepic";
    public static final StepicModuleType STEPIC_MODULE_TYPE;
    static {
        STEPIC_MODULE_TYPE = (StepicModuleType) instantiate("main.projectWizard.StepicModuleType");
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
            return (ModuleType)Class.forName(className).newInstance();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Nullable
    @Override
    public ModuleWizardStep modifyProjectTypeStep(@NotNull SettingsStep settingsStep, @NotNull final ModuleBuilder moduleBuilder) {
        return ProjectWizardStepFactory.getInstance().createJavaSettingsStep(settingsStep, moduleBuilder, new Condition<SdkTypeId>() {
            @Override
            public boolean value(SdkTypeId sdkType) {
                return moduleBuilder.isSuitableSdkType(sdkType);
            }
        });
    }
}
package main.projectWizard;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.externalSystem.model.project.ProjectData;
import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * Created by Petr on 29.04.2016.
 */
public class StepicModuleWizardStep extends ModuleWizardStep {
    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JTextField courseLinkFiled;

    private final Project myProjectOrNull;
    private final StepicModuleBuilder myBuilder;
    private final WizardContext myContext;
    private ProjectData myParent;

    public StepicModuleWizardStep(StepicModuleBuilder builder, WizardContext context) {
        myProjectOrNull = context.getProject();
        myBuilder = builder;
        myContext = context;

        initComponents();
    }

    private void initComponents() {
        panel1.setVisible(true);
    }

    @Override
    public JComponent getComponent() {
        return panel1;
    }

    public String getCourseLink(){
        return courseLinkFiled.getText();
    }

    @Override
    public void updateDataModel() {

    }

    @Override
    public void onStepLeaving() {
        saveSettings();
    }

    private void saveSettings() {
//        saveValue(courseLinkFiled.getName(),courseLinkFiled.getText());
        saveValue("courseLink",courseLinkFiled.getText());
    }

    private static void saveValue(String key, String value) {
        PropertiesComponent props = PropertiesComponent.getInstance();
        props.setValue(key, value);
    }
}

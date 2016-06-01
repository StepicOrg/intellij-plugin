package main.projectWizard;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.externalSystem.model.project.ProjectData;
import com.intellij.openapi.project.Project;
import main.stepicConnector.WorkerService;

import javax.swing.*;

/**
 * Created by Petr on 29.04.2016.
 */
public class StepicModuleWizardStep extends ModuleWizardStep {
    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JTextField courseLinkFiled;
    private JCheckBox CheckBox;

    private final Project myProjectOrNull;
    private final StepicModuleBuilder myBuilder;
    private final WizardContext myContext;
    private ProjectData myParent;

    private boolean translate = false;

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

    public String getCourseLink() {
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
        saveValue("courseLink", courseLinkFiled.getText());
        saveValue("translate", CheckBox.isSelected() ? "1" : "0");
        WorkerService ws = WorkerService.getInstance();
        ws.setPassword(new String(passwordField1.getPassword()));
        ws.setUsername(textField1.getText());
        ws.setCourseLink(courseLinkFiled.getText());
    }

    private static void saveValue(String key, String value) {
        PropertiesComponent props = PropertiesComponent.getInstance();
        props.setValue(key, value);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        WorkerService ws = WorkerService.getInstance();
//        textField1.setText(ws.getUsername());
        textField1 = new JTextField(ws.getUsername());
//        passwordField1.setText(ws.getPassword());
        passwordField1 = new JPasswordField(ws.getPassword());
    }
}

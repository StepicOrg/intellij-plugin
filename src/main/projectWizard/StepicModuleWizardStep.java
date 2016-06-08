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

    public StepicModuleWizardStep(StepicModuleBuilder builder, WizardContext context) {
        myProjectOrNull = context.getProject();
        myBuilder = builder;
        myContext = context;

        initComponents();
    }

    private void initComponents() {
        panel1.setVisible(true);
        CheckBox.setSelected(true);
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
        WorkerService ws = WorkerService.getInstance();
        ws.setLogin(textField1.getText());
        ws.setPassword(new String(passwordField1.getPassword()));
        ws.setCourseID(parseUrl(courseLinkFiled.getText()));
        ws.setTranslator(CheckBox.isSelected());
    }

    private static void saveValue(String key, String value) {
        PropertiesComponent props = PropertiesComponent.getInstance();
        props.setValue(key, value);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        WorkerService ws = WorkerService.getInstance();
        textField1 = new JTextField(ws.getLogin());
        passwordField1 = new JPasswordField(ws.getPassword());
    }

    private static String parseUrl(String url){
        if (url.isEmpty()) return "";
        if (Character.isDigit(url.charAt(0))){
            return url;
        } else {
            String[] path = url.split("/");
            if (path[3].equals("course")) {
                String tmp[] = path[4].split("-");
                return tmp[tmp.length - 1];
            }
        }
        return "";
    }
}

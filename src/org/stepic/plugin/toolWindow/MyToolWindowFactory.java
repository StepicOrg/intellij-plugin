package org.stepic.plugin.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;

public class MyToolWindowFactory implements ToolWindowFactory {
    public static final String STUDY_TOOL_WINDOW = "Step Description";

    private ToolWindow myToolWindow;
    private JPanel myToolWindowContent;
    private JButton sendStepButton;
    private JLabel stepName;
    private JLabel stepDescription;


    public MyToolWindowFactory() {

    }

    // Create the tool window content.
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {

        myToolWindow = toolWindow;
        stepName.setText("Step Name");
        stepDescription.setText("Description");
//    set context
//    VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);

    }


}

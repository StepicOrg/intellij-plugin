package org.stepic.plugin.toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;

public class MyToolWindowFactory implements ToolWindowFactory {
    public static final String STUDY_TOOL_WINDOW = "Task Description";

    private ToolWindow myToolWindow;
    private JPanel myToolWindowContent;
    private JButton send;
    private JLabel taskName;
    private JLabel taskDescription;


    public MyToolWindowFactory() {

    }

    // Create the tool window content.
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {

        myToolWindow = toolWindow;
        taskName.setText("Task Name");
        taskDescription.setText("Description");
//    set context
//    VirtualFile vf = e.getData(CommonDataKeys.VIRTUAL_FILE);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);

    }


}

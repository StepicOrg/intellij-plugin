package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import main.edu.stepic.TreeBuilder;

/**
 * Created by Petr on 05.05.2016.
 */
public class ProjectBuilder extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        TreeBuilder.build(e.getProject());
    }
}

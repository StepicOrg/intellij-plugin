package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import main.stepicConnector.WS3;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Petr on 03.06.2016.
 */
public class RefreshMap extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        WS3 ws3 = WS3.getInstance(project);

        LocalFileSystem lfs = LocalFileSystem.getInstance();
        Set<String> set = ws3.getMapPathStep().keySet();
        Set<String> removed = new HashSet<>();
        set.forEach(x -> {
            if (lfs.findFileByPath(x) == null)
                removed.add(x);
        });

        ws3.removeAll(removed);
//        set.removeAll(removed);

    }
}

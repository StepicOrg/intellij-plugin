package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SendMain extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
//        LocalFileSystem.getInstance().findFileByIoFile();
//        Project project = event.getData(PlatformDataKeys.PROJECT);
        Project project = event.getProject();
        VirtualFile[] vFiles ;

        StringBuilder sb = new StringBuilder();

        vFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();

        List<VirtualFile> files = new ArrayList<>();
        sb.append("getContentSourceRoots:\n");
        for (int i = 0; i < vFiles.length; i++) {
            sb.append(vFiles[i].getName()+"\n");
            VirtualFile[] vfs = vFiles[i].getChildren();
            for (int j = 0; j < vfs.length; j++) {
                files.add(vfs[j]);
                sb.append("./"+vfs[j].getName()+"\n");
            }
        }
        Document doc ;
//        doc.get
        for (int i = 0; i < files.size(); i++) {
            VirtualFile vf = VirtualFileManager.getInstance().findFileByUrl(files.get(i).getUrl());
            try (InputStream in = vf.getInputStream();
                 BufferedReader reader =
                         new BufferedReader(new InputStreamReader(in))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException x) {
                System.err.println(x);
            }
            sb.append("\n\n");
        }


        Messages.showMessageDialog(project, sb.toString() , "Information", Messages.getInformationIcon());
    }

}

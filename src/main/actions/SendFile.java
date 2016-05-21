package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;

/**
 * Created by Petr on 19.05.2016.
 */
public class SendFile extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Project project = e.getProject();
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile == null) return;

        String text = psiFile.getName();

//        Document doc = PsiDocumentManager.getInstance(project).getDocument(psiFile);
//        String text = doc.getText();

        Messages.showMessageDialog(project, text, "Information", Messages.getInformationIcon());
        PsiDirectory psiDirectory = psiFile.getContainingDirectory();
        text = psiDirectory.getName();
        Messages.showMessageDialog(project, text, "Information", Messages.getInformationIcon());

//        PsiUtil.
    }

    @Override
    public void setDefaultIcon(boolean isDefaultIconSet) {
        super.setDefaultIcon(isDefaultIconSet);
    }
}

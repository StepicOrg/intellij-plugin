package org.stepic.plugin.actions.popupMenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.stepic.plugin.storages.ActionVisibleProperties;

/**
 * Created by Petr on 09.06.2016.
 */
public abstract class PopupMenuAction extends AnAction{

    @Override
    public void update(AnActionEvent anActionEvent) {
        VirtualFile vf = anActionEvent.getData(CommonDataKeys.VIRTUAL_FILE);
        if (vf == null) return;

        ActionVisibleProperties.Wrapper properties = ActionVisibleProperties.getInstance(anActionEvent.getProject());
        anActionEvent.getPresentation().setVisible(properties.getVisible() && !vf.isDirectory() );
    }
}

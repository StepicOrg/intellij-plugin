package org.stepic.plugin.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.stepic.plugin.storages.ActionVisibleProperties;

public abstract class MainMenuAction extends AnAction{
    @Override
    public void update(AnActionEvent e) {
        ActionVisibleProperties properties = ActionVisibleProperties.getInstance(e.getProject());
        e.getPresentation().setEnabled(properties.isEnabled());
    }
}

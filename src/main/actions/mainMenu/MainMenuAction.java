package main.actions.mainMenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import main.actions.ActionVisibleProperties;

/**
 * Created by Petr on 09.06.2016.
 */
public abstract class MainMenuAction extends AnAction{
    @Override
    public void update(AnActionEvent e) {
        ActionVisibleProperties.Wrapper properties = ActionVisibleProperties.getInstance(e.getProject());
        e.getPresentation().setEnabled(properties.getEnabled());
    }
}

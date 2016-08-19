package org.stepik.plugin.icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

//public class PluginIcons {
public interface PluginIcons {
    //    public static final Icon STEPIK_LOGO = IconLoader.getIcon("/icons/stepik_logo_green.png");
    Icon STEPIK_LOGO_MINI = IconLoader.getIcon("/icons/stepik_logotype_13x13-2.png");
    Icon STEPIK_LOGO = IconLoader.getIcon("/icons/stepik_logo_green.png");
    Icon DOWNLOAD = IconLoader.getIcon("/icons/download_blue.png");
    //    public static final Icon Question = AllIcons.General.TodoQuestion;
    Icon Question = AllIcons.General.TodoQuestion;

}
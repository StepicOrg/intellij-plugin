package org.stepic.plugin.icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

//public class PluginIcons {
public interface PluginIcons {
    //    public static final Icon STEPIC_LOGO = IconLoader.getIcon("/icons/stepic_logo_green.png");
    public static Icon STEPIC_LOGO_MINI = IconLoader.getIcon("/icons/stepic_logotype_13x13-2.png");
    public static Icon STEPIC_LOGO = IconLoader.getIcon("/icons/stepic_logo_green.png");
    public static Icon DOWNLOAD = IconLoader.getIcon("/icons/download_blue.png");
    //    public static final Icon Question = AllIcons.General.TodoQuestion;
    Icon Question = AllIcons.General.TodoQuestion;

}
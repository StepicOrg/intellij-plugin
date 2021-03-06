package org.stepik.plugin.toolWindow;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import org.stepik.plugin.icons.PluginIcons;
import org.stepik.plugin.utils.MyLogger;

public class StudyToolWindowFactory implements ToolWindowFactory, DumbAware {
    public static final String STUDY_TOOL_WINDOW = "Step Description";


    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull final ToolWindow toolWindow) {
        MyLogger.getInstance().getLOG().debug("createToolWindowContent");
        toolWindow.setIcon(PluginIcons.STEPIK_LOGO_MINI);

        final StudyToolWindow studyToolWindow;
        if (StudyUtils.hasJavaFx()) {
            studyToolWindow = new StudyJavaFxToolWindow();
        } else {
            studyToolWindow = new StudySwingToolWindow();
        }
        studyToolWindow.init(project);
        final ContentManager contentManager = toolWindow.getContentManager();
        final Content content = contentManager.getFactory().createContent(studyToolWindow, null, false);
        contentManager.addContent(content);
        Disposer.register(project, studyToolWindow);
    }
}


package org.stepik.plugin.toolWindow;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Map;

public interface StudyPluginConfigurator {
    ExtensionPointName<StudyPluginConfigurator> EP_NAME = ExtensionPointName.create("Edu.studyToolWindowConfigurator");

    /**
     * Provide action group that should be placed on the tool window toolbar.
     *
     * @param project
     * @return
     */
    @NotNull
    DefaultActionGroup getActionGroup(Project project);

    /**
     * Provide panels, that could be added to Task tool window.
     *
     * @param project
     * @return Map from panel id, i.e. "Task description", to panel itself.
     */
    @NotNull
    Map<String, JPanel> getAdditionalPanels(Project project);

    @NotNull
    FileEditorManagerListener getFileEditorManagerListener(@NotNull final Project project, @NotNull final StudyToolWindow toolWindow);

    /**
     * @return parameter for CodeMirror script. Available languages: @see <@linktourl http://codemirror.net/mode/>
     */
    @NotNull
    String getDefaultHighlightingMode();

    @NotNull
    String getLanguageScriptUrl();

    boolean accept(@NotNull final Project project);

}

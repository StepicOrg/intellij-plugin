package org.stepic.plugin.toolWindow;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.content.Content;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.stepic.plugin.storages.CourseDefinitionStorage;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Collection;

public class StudyUtils {
    private StudyUtils() {
    }

    private static final Logger LOG = Logger.getInstance(StudyUtils.class.getName());
    private static final String EMPTY_STEP_TEXT = "Please, open any Step to see Step description";

    public static void closeSilently(@Nullable final Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                // close silently
            }
        }
    }

    public static boolean isZip(String fileName) {
        return fileName.contains(".zip");
    }

    public static <T> T getFirst(@NotNull final Iterable<T> container) {
        return container.iterator().next();
    }

    public static boolean indexIsValid(int index, @NotNull final Collection collection) {
        int size = collection.size();
        return index >= 0 && index < size;
    }

    @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
    @Nullable
    public static String getFileText(@Nullable final String parentDir, @NotNull final String fileName, boolean wrapHTML,
                                     @NotNull final String encoding) {
        final File inputFile = parentDir != null ? new File(parentDir, fileName) : new File(fileName);
        if (!inputFile.exists()) return null;
        final StringBuilder stepText = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                stepText.append(line).append("\n");
                if (wrapHTML) {
                    stepText.append("<br>");
                }
            }
            return wrapHTML ? UIUtil.toHtml(stepText.toString()) : stepText.toString();
        } catch (IOException e) {
            LOG.info("Failed to get file text from file " + fileName, e);
        } finally {
            closeSilently(reader);
        }
        return null;
    }

    public static void updateAction(@NotNull final AnActionEvent e) {
        final Presentation presentation = e.getPresentation();
        presentation.setEnabled(false);
        final Project project = e.getProject();
        if (project != null) {
            final StudyEditor studyEditor = getSelectedStudyEditor(project);
            if (studyEditor != null) {
                presentation.setEnabledAndVisible(true);
            }
        }
    }

    public static void updateToolWindows(@NotNull final Project project) {
        update(project);

//    final ToolWindowManager windowManager = ToolWindowManager.getInstance(project);
//    createProgressToolWindowContent(project, windowManager);
    }

    public static void initToolWindows(@NotNull final Project project) {
        final ToolWindowManager windowManager = ToolWindowManager.getInstance(project);
        windowManager.getToolWindow(StudyToolWindowFactory.STUDY_TOOL_WINDOW).getContentManager().removeAllContents(false);
        StudyToolWindowFactory factory = new StudyToolWindowFactory();
        factory.createToolWindowContent(project, windowManager.getToolWindow(StudyToolWindowFactory.STUDY_TOOL_WINDOW));

//        createProgressToolWindowContent(project, windowManager);
    }

    private static void createProgressToolWindowContent(@NotNull Project project, ToolWindowManager windowManager) {
//    windowManager.getToolWindow(StudyProgressToolWindowFactory.ID).getContentManager().removeAllContents(false);
//    StudyProgressToolWindowFactory windowFactory = new StudyProgressToolWindowFactory();
//    windowFactory.createToolWindowContent(project, windowManager.getToolWindow(StudyProgressToolWindowFactory.ID));
    }

    @Nullable
    public static StudyToolWindow getStudyToolWindow(@NotNull final Project project) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(StudyToolWindowFactory.STUDY_TOOL_WINDOW);
        if (toolWindow != null) {
            Content[] contents = toolWindow.getContentManager().getContents();
            for (Content content : contents) {
                JComponent component = content.getComponent();
                if (component != null && component instanceof StudyToolWindow) {
                    return (StudyToolWindow) component;
                }
            }
        }
        return null;
    }

    public static void deleteFile(@NotNull final VirtualFile file) {
        try {
            file.delete(StudyUtils.class);
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    public static void showCheckPopUp(@NotNull final Project project, @NotNull final Balloon balloon) {
        final StudyEditor studyEditor = getSelectedStudyEditor(project);
        assert studyEditor != null;

        balloon.show(computeLocation(studyEditor.getEditor()), Balloon.Position.above);
        Disposer.register(project, balloon);
    }

    public static RelativePoint computeLocation(Editor editor) {

        final Rectangle visibleRect = editor.getComponent().getVisibleRect();
        Point point = new Point(visibleRect.x + visibleRect.width + 10,
                visibleRect.y + 10);
        return new RelativePoint(editor.getComponent(), point);
    }


    @Nullable
    public static StudyEditor getSelectedStudyEditor(@NotNull final Project project) {
        try {
            final FileEditor fileEditor = FileEditorManagerEx.getInstanceEx(project).getSplitters().getCurrentWindow().
                    getSelectedEditor().getSelectedEditorWithProvider().getFirst();
            if (fileEditor instanceof StudyEditor) {
                return (StudyEditor) fileEditor;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Nullable
    public static Editor getSelectedEditor(@NotNull final Project project) {
        final StudyEditor studyEditor = getSelectedStudyEditor(project);
        if (studyEditor != null) {
            return studyEditor.getEditor();
        }
        return null;
    }

    @Nullable
    public static String getStepTextFromStep(VirtualFile file, Project project) {
        return CourseDefinitionStorage.getInstance(project).getText(file.getPath());
    }

    @Nullable
    public static StudyPluginConfigurator getConfigurator(@NotNull final Project project) {
        StudyPluginConfigurator[] extensions = StudyPluginConfigurator.EP_NAME.getExtensions();
        for (StudyPluginConfigurator extension : extensions) {
            if (extension.accept(project)) {
                return extension;
            }
        }
        return null;
    }

    public static String getStepText(@NotNull final Project project) {
        VirtualFile[] files = FileEditorManager.getInstance(project).getSelectedFiles();
//    TaskFile taskFile = null;
        CourseDefinitionStorage ps = CourseDefinitionStorage.getInstance(project);
        String text = null;
        for (VirtualFile file : files) {
            text = ps.getText(file.getPath());
            if (text != null && !text.isEmpty()) {
                break;
            }
        }
        if (text == null) {
            return EMPTY_STEP_TEXT;
        }
        return text;
    }

    public static void update(Project project) {
        final StudyToolWindow studyToolWindow = getStudyToolWindow(project);
        if (studyToolWindow != null) {
            String stepText = getStepText(project);
            studyToolWindow.setStepText(stepText);
        }
    }
}

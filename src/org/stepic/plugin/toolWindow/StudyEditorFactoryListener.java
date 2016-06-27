package org.stepic.plugin.toolWindow;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.editor.event.EditorMouseAdapter;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.problems.WolfTheProblemSolver;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


public class StudyEditorFactoryListener implements EditorFactoryListener {

    private static class WindowSelectionListener extends EditorMouseAdapter {
        @Override
        public void mouseClicked(EditorMouseEvent e) {
            final Editor editor = e.getEditor();
            final Point point = e.getMouseEvent().getPoint();
            final LogicalPosition pos = editor.xyToLogicalPosition(point);
        }
    }

    @Override
    public void editorCreated(@NotNull final EditorFactoryEvent event) {
        final Editor editor = event.getEditor();

        final Project project = editor.getProject();
        if (project == null) {
            return;
        }
        ApplicationManager.getApplication().invokeLater(
                new Runnable() {
                    @Override
                    public void run() {
                        ApplicationManager.getApplication().runWriteAction(new Runnable() {
                            @Override
                            public void run() {
                                final Document document = editor.getDocument();
                                final VirtualFile openedFile = FileDocumentManager.getInstance().getFile(document);
                                if (openedFile != null) {
                                    WolfTheProblemSolver.getInstance(project).clearProblems(openedFile);
                                    final ToolWindow studyToolWindow = ToolWindowManager.getInstance(project).getToolWindow(MyToolWindowFactory.STUDY_TOOL_WINDOW);
                                    if (studyToolWindow != null) {
                                        StudyUtils.updateToolWindows(project);
                                        studyToolWindow.show(null);
                                    }
                                    if (true) {
//                                        StudyNavigator.navigateToFirstAnswerPlaceholder(editor, taskFile);
//                                        StudyEditor.addDocumentListener(document, new EduDocumentListener(taskFile));
//                                        StudyUtils.drawAllWindows(editor, taskFile);
//                                        editor.addEditorMouseListener(new WindowSelectionListener(taskFile));
                                    }
                                }
                            }
                        });
                    }
                }
        );
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) {
        final Editor editor = event.getEditor();
        final Document document = editor.getDocument();
//        StudyEditor.removeListener(document);
        editor.getMarkupModel().removeAllHighlighters();
        editor.getSelectionModel().removeSelection();
    }
}

package org.stepic.plugin.toolWindow;

import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class StudyEditor extends PsiAwareTextEditorImpl {
//  public StudyEditor(@NotNull Project project, @NotNull VirtualFile virtualFile, TextEditorProvider textEditorProvider) {
//    super(project, virtualFile, textEditorProvider);
//  }
//  private final TaskFile myTaskFile;
//  private static final Map<Document, EduDocumentListener> myDocumentListeners = new HashMap<Document, EduDocumentListener>();
//
//  public TaskFile getTaskFile() {
//    return myTaskFile;
//  }

    //  public static void addDocumentListener(@NotNull final Document document, @NotNull final EduDocumentListener listener) {
//    document.addDocumentListener(listener);
//    myDocumentListeners.put(document, listener);
//  }
//
    public StudyEditor(@NotNull final Project project, @NotNull final VirtualFile file) {
        super(project, file, TextEditorProvider.getInstance());
//    myTaskFile = StudyUtils.getTaskFile(project, file);
    }
//
//  public static void removeListener(Document document) {
//    final EduDocumentListener listener = myDocumentListeners.get(document);
//    if (listener != null) {
//      document.removeDocumentListener(listener);
//    }
//    myDocumentListeners.remove(document);
//  }
}

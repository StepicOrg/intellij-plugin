package main.toolWindow;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class JavaStudyToolWindowConfigurator extends StudyBaseToolWindowConfigurator {

    @NotNull
    @Override
    public DefaultActionGroup getActionGroup(Project project) {
        final DefaultActionGroup baseGroup = super.getActionGroup(project);
        final DefaultActionGroup group = new DefaultActionGroup();
//    group.add(new PyStudyCheckAction());
        group.addAll(baseGroup);
        return group;
    }

    @NotNull
    @Override
    public String getDefaultHighlightingMode() {
        return "java";
    }

    @NotNull
    @Override
    public String getLanguageScriptUrl() {
        return getClass().getResource("/python.js").toExternalForm();
    }

    @Override
    public boolean accept(@NotNull Project project) {
//    StudyTaskManager taskManager = StudyTaskManager.getInstance(project);
//    if (taskManager == null) return false;
//    Course course = taskManager.getCourse();
//    return course != null && "Python".equals(course.getLanguage()) && "PyCharm".equals(course.getCourseType());
        return true;
    }
}

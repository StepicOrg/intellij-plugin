package org.stepic.plugin.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import org.stepic.plugin.storages.CourseDefinitionStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
    public static void refreshFiles(Project project) {
        CourseDefinitionStorage projectService = CourseDefinitionStorage.getInstance(project);

        LocalFileSystem lfs = LocalFileSystem.getInstance();
        Set<String> set = projectService.getMapPathInfo().keySet();
        Set<String> removed = new HashSet<>();
        set.forEach(x -> {
            if (lfs.findFileByPath(x) == null)
                removed.add(x);
        });

        projectService.removeAll(removed);
        set.removeAll(removed);
    }

    public static String normalize(String text) {
        String[] words = text.split("\\W+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length - 1; i++) {
            sb.append(words[i] + "_");
        }
        if (words.length > 0)
            sb.append(words[words.length - 1]);
        return sb.toString();
    }

    public static String getIdQuery(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        for (Integer id : list) {
            sb.append("ids[]=" + id + "&");
        }
        return sb.toString();
    }

    public static String parseUrl(String url) {
        if (url == null || url.isEmpty()) return "";
        url = url.trim();
        if (Character.isDigit(url.charAt(0))) {
            return url;
        } else {
            String[] path = url.split("/");
            if (path[3].equals("course")) {
                String tmp[] = path[4].split("-");
                return tmp[tmp.length - 1];
            }
        }
        return "";
    }
}

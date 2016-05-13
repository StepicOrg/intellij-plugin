package main.edu.stepic;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vcs.changes.ui.TreeModelBuilder;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

/**
 * Created by Petr on 05.05.2016.
 */
public class TreeBuilder {
    private static final Logger LOG = Logger.getInstance(TreeBuilder.class);
    private static String myContentEntryPath;
    protected Sdk myJdk;
    private String myName;
    @NonNls
    private static String myModuleFilePath;

    public static void build(Project project) {
        TreeModelBuilder treeModelBuilder = new TreeModelBuilder(project, true);
//         List<Change> changes =  new ArrayList<>();
//         changes.add(new Change())
//         VirtualFile vf = VirtualFileManager
//        VfsUtil.

        final VirtualFile root = createAndGetContentEntry();
        try {
            VfsUtil.createDirectories(root.getPath() + "/src/main/java");
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    private static VirtualFile createAndGetContentEntry() {
        String path = FileUtil.toSystemIndependentName(getContentEntryPath());
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }

    @Nullable
    public static String getContentEntryPath() {
        if (myContentEntryPath == null) {
            final String directory = getModuleFileDirectory();
            if (directory == null) {
                return null;
            }
            new File(directory).mkdirs();
            return directory;
        }
        return myContentEntryPath;
    }

    @Nullable
    public static String getModuleFileDirectory() {
        if (myModuleFilePath == null) {
            return null;
        }
        final String parent = new File(myModuleFilePath).getParent();
        if (parent == null) {
            return null;
        }
        return parent.replace(File.separatorChar, '/');
    }
}

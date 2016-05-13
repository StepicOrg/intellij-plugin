package main.projectWizard;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModifiableModuleModel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleWithNameAlreadyExists;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import main.edu.stepic.MyCourse;
import main.stepicConnector.StepicConnector;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StepicModuleBuilder extends JavaModuleBuilder {
    private static final Logger LOG = Logger.getInstance(StepicModuleBuilder.class);
    private String myCompilerOutputPath;
    // Pair<Source Path, Package Prefix>
    private List<Pair<String, String>> mySourcePaths;
    // Pair<Library path, Source path>
    private final List<Pair<String, String>> myModuleLibraries = new ArrayList<Pair<String, String>>();

//    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
//        final Project project = rootModel.getProject();
//
//        final VirtualFile root = createAndGetContentEntry();
//        rootModel.addContentEntry(root);
//
//        // todo this should be moved to generic ModuleBuilder
//        if (myJdk != null){
//            rootModel.setSdk(myJdk);
//        } else {
//            rootModel.inheritSdk();
//        }
//    }

    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        final CompilerModuleExtension compilerModuleExtension = rootModel.getModuleExtension(CompilerModuleExtension.class);
        compilerModuleExtension.setExcludeOutput(true);
        LOG.warn("setup root model");
        if (myJdk != null) {
            rootModel.setSdk(myJdk);
        } else {
            rootModel.inheritSdk();
        }


        try {
            buidCourseStructe();
        } catch (IOException e) {
            LOG.error("build course structure error\n" + e.getMessage());
        }

        ContentEntry contentEntry = doAddContentEntry(rootModel);
        if (contentEntry != null) {
            final List<Pair<String, String>> sourcePaths = getSourcePaths();

            if (sourcePaths != null) {
                for (final Pair<String, String> sourcePath : sourcePaths) {
                    String first = sourcePath.first;
                    new File(first).mkdirs();
                    final VirtualFile sourceRoot = LocalFileSystem.getInstance()
                            .refreshAndFindFileByPath(FileUtil.toSystemIndependentName(first));
                    if (sourceRoot != null) {
                        contentEntry.addSourceFolder(sourceRoot, false, sourcePath.second);
                    }
                }
            }
        }


        if (myCompilerOutputPath != null) {
            // should set only absolute paths
            String canonicalPath;
            try {
                canonicalPath = FileUtil.resolveShortWindowsName(myCompilerOutputPath);
            } catch (IOException e) {
                canonicalPath = myCompilerOutputPath;
            }
            compilerModuleExtension
                    .setCompilerOutputPath(VfsUtilCore.pathToUrl(FileUtil.toSystemIndependentName(canonicalPath)));
        } else {
            compilerModuleExtension.inheritCompilerOutputPath(true);
        }

        LibraryTable libraryTable = rootModel.getModuleLibraryTable();
        for (Pair<String, String> libInfo : myModuleLibraries) {
            final String moduleLibraryPath = libInfo.first;
            final String sourceLibraryPath = libInfo.second;
            Library library = libraryTable.createLibrary();
            Library.ModifiableModel modifiableModel = library.getModifiableModel();
            modifiableModel.addRoot(getUrlByPath(moduleLibraryPath), OrderRootType.CLASSES);
            if (sourceLibraryPath != null) {
                modifiableModel.addRoot(getUrlByPath(sourceLibraryPath), OrderRootType.SOURCES);
            }
            modifiableModel.commit();
        }
    }

    private static String getUrlByPath(final String path) {
        return VfsUtil.getUrlForLibraryRoot(new File(path));
    }


    public ModuleType getModuleType() {
        return StepicModuleType.STEPIC_MODULE;
    }

    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext,
                                                @NotNull ModulesProvider modulesProvider) {
        LOG.warn("Create Wizard Steps");
        return new StepicModuleWizardStep[]{new StepicModuleWizardStep(this, wizardContext)};
    }

    private VirtualFile createAndGetContentEntry() {
        String path = FileUtil.toSystemIndependentName(getContentEntryPath());
        new File(path).mkdirs();
        return LocalFileSystem.getInstance().refreshAndFindFileByPath(path);
    }

    public List<Pair<String, String>> getSourcePaths() {
        if (mySourcePaths == null) {
            final List<Pair<String, String>> paths = new ArrayList<Pair<String, String>>();
            @NonNls final String path = getContentEntryPath() + File.separator + "src";
            new File(path).mkdirs();
            paths.add(Pair.create(path, ""));
            return paths;
        }
        return mySourcePaths;
    }

    public void setSourcePaths(List<Pair<String, String>> sourcePaths) {
        mySourcePaths = sourcePaths != null ? new ArrayList<Pair<String, String>>(sourcePaths) : null;
    }

    public void addSourcePath(Pair<String, String> sourcePathInfo) {
        if (mySourcePaths == null) {
            mySourcePaths = new ArrayList<Pair<String, String>>();
        }
        mySourcePaths.add(sourcePathInfo);
    }

    public void addModuleLibrary(String moduleLibraryPath, String sourcePath) {
        myModuleLibraries.add(Pair.create(moduleLibraryPath, sourcePath));
    }

    @Nullable
    protected static String getPathForOutputPathStep() {
        return null;
    }

    @Nullable
    @Override
    public List<Module> commit(@NotNull Project project, ModifiableModuleModel model, ModulesProvider modulesProvider) {
        LanguageLevelProjectExtension extension = LanguageLevelProjectExtension.getInstance(ProjectManager.getInstance().getDefaultProject());
        Boolean aDefault = extension.getDefault();
        LanguageLevelProjectExtension instance = LanguageLevelProjectExtension.getInstance(project);
        if (aDefault != null && !aDefault) {
            instance.setLanguageLevel(extension.getLanguageLevel());
            instance.setDefault(false);
        } else {
            instance.setDefault(true);
        }
        return super.commit(project, model, modulesProvider);
    }


    private void buidCourseStructe() throws IOException {
        final VirtualFile root = createAndGetContentEntry();

        PropertiesComponent props = PropertiesComponent.getInstance();
        String courseLink = props.getValue("courseLink");
        LOG.warn("build course structure " + courseLink);

        StepicConnector.initToken();
        MyCourse course = StepicConnector.getCourse(courseLink);
        course.build();
        List<String> paths = new ArrayList();
        course.sections.forEach((x, y) -> {
            //TODO: clean titles from  { \/*?<>\" }
//            String sectionTitle = y.position + "." + y.title.replace(':',' ');
            String sectionTitle = "section" + y.position;
            y.lessons.forEach((xx, yy) -> {
//                String lessonTitle =  xx.toString() + "." + yy.title.replace(':',' ');
                String lessonTitle = "lesson" + xx.toString();
                yy.steps.forEach((xxx, yyy) -> {
                    String step = "step" + xxx.toString() + ".java";
                    String path = root.getPath() + File.separator +
                            sectionTitle + File.separator +
                            lessonTitle + File.separator + step;
                    File f = new File(path);
                    f.getParentFile().mkdirs();
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        LOG.error("Create file error\n" + e.getMessage());
                    }
                });
            });
            addSourcePath(Pair.create(root.getPath() + File.separator +
                    sectionTitle, ""));
        });
    }


    @NotNull
    @Override
    public Module createModule(@NotNull ModifiableModuleModel moduleModel) throws InvalidDataException, IOException, ModuleWithNameAlreadyExists, JDOMException, ConfigurationException {
        Module baseModule = super.createModule(moduleModel);

        return baseModule;
    }
}

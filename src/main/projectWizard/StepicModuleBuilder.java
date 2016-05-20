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
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.OrderRootType;
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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static main.projectWizard.YaTranslator.translateRuToEn;

public class StepicModuleBuilder extends JavaModuleBuilder {
    private static final Logger LOG = Logger.getInstance(StepicModuleBuilder.class);
    private String myCompilerOutputPath;
    // Pair<Source Path, Package Prefix>
//    private List<Pair<String, String>> mySourcePaths;
    // Pair<Source Path, filename>
//    private List<Pair<String, String>> myFileList;

    private List<MyFileInfo> myFileInfos;
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
        try {
            buildCourseStructe();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

        final CompilerModuleExtension compilerModuleExtension = rootModel.getModuleExtension(CompilerModuleExtension.class);
        compilerModuleExtension.setExcludeOutput(true);
        if (myJdk != null) {
            rootModel.setSdk(myJdk);
        } else {
            rootModel.inheritSdk();
        }

//        ContentEntry contentEntry = doAddContentEntry(rootModel);
//        if (contentEntry != null) {
//            final List<MyFileInfo> fileInfos = getMyFileInfos();
//
//            if (fileInfos != null) {
//                for (final MyFileInfo fileInfo : fileInfos) {
//                    String path = fileInfo.path;
//                    String pack = fileInfo.pack;
//                    String filename = fileInfo.filename;
//                    File f = new File(path);
//                    f.getParentFile().mkdirs();
//
//                    Path rPath = Paths.get(fileInfo.path);
//
//                    try {
//                        Files.write(rPath, getText(filename, pack), Charset.forName("UTF-8"));
//                    } catch (IOException e) {
//                        LOG.error("Create File error\n" + e.getMessage());
//                    }
//
//                    final VirtualFile sourceRoot = LocalFileSystem.getInstance()
//                            .refreshAndFindFileByPath(FileUtil.toSystemIndependentName(fileInfo.source));
//                    if (sourceRoot != null) {
//                        contentEntry.addSourceFolder(sourceRoot, false, "");
//                    }
//                }
//            }
//        }
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

    private List<String> getText(String name, String pack) {
        List<String> text = new ArrayList<>();
        text.add("package " + pack + ";\n");
        text.add("class " + name + " {");
        text.add("\tpublic static void main(String[] args){");
        text.add("\t\t//Write your code here");
        text.add("\t}\n}");
        return text;
    }

    private static String getUrlByPath(final String path) {
        return VfsUtil.getUrlForLibraryRoot(new File(path));
    }


    public ModuleType getModuleType() {
        return StepicModuleType.STEPIC_MODULE_TYPE;
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

    private void buildCourseStructe() throws IOException {
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
                    String pack = sectionTitle + "." + lessonTitle;
                    String path = root.getPath() + File.separator + "src" + File.separator +
                            sectionTitle + File.separator +
                            lessonTitle + File.separator + step;
                    File f = new File(path);
                    f.getParentFile().mkdirs();
                    try {
//                        f.createNewFile();
                        Path path1 = Paths.get(path);
                        Files.write(path1, getText("step" + xxx.toString(), pack), Charset.forName("UTF-8"));
                    } catch (IOException e) {
                        LOG.error("Create file error\n" + e.getMessage());
                    }
                });
            });
        });
        addSourcePath(Pair.create(root.getPath() + File.separator + "src", ""));
    }

    private void buildCourseStructe2() {
        final VirtualFile root = createAndGetContentEntry();

        PropertiesComponent props = PropertiesComponent.getInstance();
        String courseLink = props.getValue("courseLink");
        LOG.warn("build course structure " + courseLink);

        final boolean translate;
        if (props.getValue("translate").equals("1")) {
            translate = true;
        } else {
            translate = false;
        }
        StepicConnector.initToken();
        MyCourse course = StepicConnector.getCourse(courseLink);
        course.build();
        List<String> paths = new ArrayList();
        course.sections.forEach((x, y) -> {
            //TODO: clean titles from  { \/*?<>\" }
            String sectionTitle;
            if (translate) {
                sectionTitle = "_" + y.position + "." + translateRuToEn(y.title).replace(':', ' ').replace(' ', '_');
            } else {
                sectionTitle = "_" + y.position + "." + y.title.replace(':', ' ').replace(' ', '_');
            }

            y.lessons.forEach((xx, yy) -> {
                String lessonTitle;

//                if (translate) {
//                    lessonTitle = "_" + xx.toString() + "." + translateRuToEn(yy.title).replace(':', ' ').replace(' ', '_');
//                } else {
//                    lessonTitle = "_" + xx.toString() + "." + yy.title.replace(':', ' ').replace(' ', '_');
//                }
                lessonTitle = "Lesson" + xx.toString();

                String path = root.getPath() + File.separator +
                        sectionTitle + File.separator +
                        lessonTitle;
                yy.steps.forEach((xxx, yyy) -> {
                    String filename = "Step" + xxx.toString();
                    String filePath = path + File.separator + filename + ".java";
                    addFileInfo(new MyFileInfo(filePath, root.getPath() + File.separator +
                            sectionTitle, lessonTitle, filename));
                });
            });
        });
    }

    private void addFileInfo(MyFileInfo myFileInfo) {
        if (myFileInfos == null) {
            myFileInfos = new ArrayList<>();
        }
        myFileInfos.add(myFileInfo);
    }

    @NotNull
    @Override
    public Module createModule(@NotNull ModifiableModuleModel moduleModel) throws InvalidDataException, IOException, ModuleWithNameAlreadyExists, JDOMException, ConfigurationException {
        Module baseModule = super.createModule(moduleModel);

        return baseModule;
    }

    public List<MyFileInfo> getMyFileInfos() {
        if (myFileInfos == null) {
            myFileInfos = new ArrayList<>();
        }
        return myFileInfos;
    }
}

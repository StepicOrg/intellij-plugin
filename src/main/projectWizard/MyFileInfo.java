package main.projectWizard;

/**
 * Created by Petr on 19.05.2016.
 */
public class MyFileInfo {

    public MyFileInfo(String path, String source, String pack, String filename) {
        this.path = path;
        this.source = source;
        this.pack = pack;
        this.filename = filename;
    }

    String path;
    String source;
    String pack;
    String filename;
}

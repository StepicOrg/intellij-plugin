package main.projectWizard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Petr on 21.05.2016.
 */
public class MyFileInfoList {

    private static List<MyFileInfo> list;

    public static MyFileInfoList getInstance() {
        return new MyFileInfoList();
    }

    private MyFileInfoList() {
        if (list == null)
            list = new ArrayList<>();
    }

    public void addFileInfo(String path, String source, String pack, String filename) {
//        if (list == null) list = new ArrayList<>();
        list.add(new MyFileInfo(path, source, pack, filename));
    }

    public List<MyFileInfo> getList() {
        return list;
    }

    public void setList(List<MyFileInfo> list) {
        this.list = list;
    }

    public class MyFileInfo {
        public MyFileInfo(String path, String source, String pack, String filename) {
            this.path = path;
            this.source = source;
            this.pack = pack;
            this.filename = filename;
        }

        public String path;
        public String source;
        public String pack;
        public String filename;
    }
}
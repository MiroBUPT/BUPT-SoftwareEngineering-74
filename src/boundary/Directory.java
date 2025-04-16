package boundary;

import java.util.ArrayList;
import java.util.List;

class Directory {
    private String name;
    private Image image;
    private List<Entry> entries = new ArrayList<>();

    public Directory(String name, Image image) {
        this.name = name;
        this.image = image;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    private class Image {
    }

    // 返回目录名称
    public String getName() {
        return name;
    }
}

package boundary;

import java.util.ArrayList;
import java.util.List;

class IndexView {
    private List<Directory> directories = new ArrayList<>();

    public void addDirectory(Directory directory) {
        directories.add(directory);
    }
}

package boundary;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a directory in the application's navigation structure.
 * This class manages a collection of entries and their associated images.
 */
class Directory {
    /** The name of the directory */
    private String name;
    /** The image associated with this directory */
    private Image image;
    /** The list of entries in this directory */
    private List<Entry> entries = new ArrayList<>();

    /**
     * Constructs a new Directory with the specified name and image.
     * @param name The name of the directory
     * @param image The image to associate with this directory
     */
    public Directory(String name, Image image) {
        this.name = name;
        this.image = image;
    }

    /**
     * Adds an entry to this directory.
     * @param entry The entry to add
     */
    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    /**
     * Inner class representing an image associated with a directory.
     */
    private class Image {
    }

    // 返回目录名称
    /**
     * Gets the name of this directory.
     * @return The directory name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the list of entries in this directory.
     * @return The list of entries
     */
    public List<Entry> getEntries() {
        return entries;
    }
}

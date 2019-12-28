package common.model;

import java.io.File;

public class Music {
    private final String name;
    private final String author;
    private final String album;
    private final Integer year;
    private final File file;

    public Music(String name, String author, String album, Integer year, File file){
        this.name = name;
        this.author = author;
        this.album = album;
        this.year = year;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getAlbum() {
        return album;
    }

    public Integer getYear() {
        return year;
    }

    public File getFile() {
        return file;
    }
}

package common.model;

public class Music {
    private final String name;
    private final String author;
    private final String album;
    private final Integer year;
    private final String filePath;

    public Music(String name, String author, String album, Integer year, String filePath){
        this.name = name;
        this.author = author;
        this.album = album;
        this.year = year;
        this.filePath = filePath;
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

    public String getFilePath() {
        return filePath;
    }
}

package common.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Music {
    private StringProperty name;
    public void setName(String value) { nameProperty().set(value); }
    public String getName() { return nameProperty().get(); }
    public StringProperty nameProperty() {
        if (name == null) name = new SimpleStringProperty(this, "name");
        return name;
    }

    private StringProperty album;
    public void setAlbum(String value) { albumProperty().set(value); }
    public String getAlbum() { return albumProperty().get(); }
    public StringProperty albumProperty() {
        if (album == null) album = new SimpleStringProperty(this, "album");
        return album;
    }

    private IntegerProperty year;
    public void setYear(Integer value) { yearProperty().set(value); }
    public Integer getYear() { return yearProperty().get(); }
    public IntegerProperty yearProperty() {
        if (year == null) year = new SimpleIntegerProperty(this, "year");
        return year;
    }

    public Music(String name, String album, Integer year) {
        setName(name);
        setAlbum(album);
        setYear(year);
    }
}

package sample;

import javafx.beans.property.StringProperty;

import java.util.Date;

public class FileForTableView {
    private String name;
    private String size;
    private boolean isDirectory;
    private Date lastModifiedDate;
    private String formatedLastModifiedDate;

    public FileForTableView(String name, Long size, boolean isDirectory, Date lastModifiedDate) {
        this.name = name;
        this.size = size.toString();
        this.isDirectory = isDirectory;
        this.lastModifiedDate = lastModifiedDate;
    }

    public FileForTableView(String name, String size, boolean isDirectory, Date lastModifiedDate) {
        this.name = name;
        this.size = size;
        this.isDirectory = isDirectory;
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getFormatedLastModifiedDate() {
        return formatedLastModifiedDate;
    }

    public void setFormatedLastModifiedDate(String formatedLastModifiedDate) {
        this.formatedLastModifiedDate = formatedLastModifiedDate;
    }
}

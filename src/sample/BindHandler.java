package sample;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ObservableList;

public class BindHandler {
    private ObservableList<FileForTableView> list;

    public BindHandler(ObservableList<FileForTableView> list) {
        this.list = list;
    }

    public ObservableList<FileForTableView> getList() {
        return list;
    }

    public void setList(ObservableList<FileForTableView> list) {
        this.list = list;
    }
}

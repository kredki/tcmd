package sample;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

public class Controller {

    //@FXML // ResourceBundle that was given to the FXMLLoader
    //private ResourceBundle resources;

    private static ResourceBundle resourceBundle;
    private static Locale locale;
    private static String lang = "en";

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button upLeftButton;

    @FXML
    private Button upRightButton;

    @FXML
    private Button changeLng;

    @FXML
    private Button copy;

    @FXML
    private Button move;

    @FXML
    private Button delete;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ChoiceBox<File> leftChoiceBox;

    @FXML
    private ChoiceBox<File> rightChoiceBox;

    @FXML
    private TableView<FileForTableView> leftTableView;

    @FXML
    private TableColumn<FileForTableView, String> leftTableViewColumnName;

    @FXML
    private TableColumn<FileForTableView, String> leftTableViewColumSize;

    @FXML
    private TableColumn<FileForTableView, String> leftTableViewColumDate;

    @FXML
    private TableView<FileForTableView> rightTableView;

    @FXML
    private TableColumn<FileForTableView, String> rightTableViewColumnName;

    @FXML
    private TableColumn<FileForTableView, String> rightTableViewColumSize;

    @FXML
    private TableColumn<FileForTableView, String> rightTableViewColumDate;

    @FXML
    private Label leftPathLabel;

    @FXML
    private Label rightPathLabel;

    //method is called automatically when the FXML is loaded
    public void initialize() {
        locale = new Locale("en");
        File[] paths;
        //FileSystemView fsv = FileSystemView.getFileSystemView();

        // returns path names for files and directory
        paths = File.listRoots();

        // for each pathname in pathname array
        for(File path:paths)
        {
            //add driver names to ChoiceBox
            leftChoiceBox.getItems().add(path);
            rightChoiceBox.getItems().add(path);
            // prints file and directory paths
            //System.out.println("Drive Name: "+path);
            //System.out.println("Description: "+fsv.getSystemTypeDescription(path));
        }

        //set listener for ChoiceBox
        leftChoiceBox.setOnAction(event -> choiceBoxListener(event));
        rightChoiceBox.setOnAction(event -> choiceBoxListener(event));

        //set first drive as default
        leftChoiceBox.getSelectionModel().selectFirst();
        rightChoiceBox.getSelectionModel().selectFirst();

        //set TableView columns properties: name of variable to put in table
        leftTableViewColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        leftTableViewColumSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        leftTableViewColumDate.setCellValueFactory(new PropertyValueFactory<>("formatedLastModifiedDate"));
        rightTableViewColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        rightTableViewColumSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        rightTableViewColumDate.setCellValueFactory(new PropertyValueFactory<>("formatedLastModifiedDate"));

        rightTableViewColumSize.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String dir = resourceBundle.getString("directory");
                if(o1.equals(dir)) {
                    if(o2.equals(dir)) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    if(o2.equals(dir)) {
                        return -1;
                    } else {
                        long size1 = Long.parseLong(o1);
                        long size2 = Long.parseLong(o2);
                        return Long.compare(size1, size2);
                    }
                }
            }
        });

        leftTableViewColumSize.setComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String dir = resourceBundle.getString("directory");
                if(o1.equals(dir)) {
                    if(o2.equals(dir)) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    if(o2.equals(dir)) {
                        return -1;
                    } else {
                        long size1 = Long.parseLong(o1);
                        long size2 = Long.parseLong(o2);
                        return Long.compare(size1, size2);
                    }
                }
            }
        });

        //set lang
        loadLang("en");
    }

    @FXML
    void changeLanguage(ActionEvent event) {
        if(locale.equals(Locale.ENGLISH)) {
            lang = "pl";
            loadLang(lang);
            //resources = ResourceBundle.getBundle("sample.lang", new Locale("pl"));
        } else {
            lang = "en";
            loadLang(lang);
            //resources = ResourceBundle.getBundle("sample.lang", new Locale("en"));
        }
        System.out.println("change language clicked");
    }

    @FXML
    void upLeft(ActionEvent event) {
        System.out.println("up left clicked");
       up(false);
       loadLang(lang);
    }

    @FXML
    void upRight(ActionEvent event) {
        System.out.println("up right clicked");
        up(true);
        loadLang(lang);
    }

    @FXML
    void copy(ActionEvent event) {
        System.out.println("copy clicked");

        FileForTableView fileForTableView = (FileForTableView) leftTableView.getSelectionModel().getSelectedItem();
        if (fileForTableView != null) {
            String pathSource = leftPathLabel.getText();
            String pathDestination = rightPathLabel.getText();
            if(!(pathDestination.substring(pathDestination.length() - 1).equals("\\"))) {
                pathSource +="\\";
            }
            pathDestination +=  "\\" + fileForTableView.getName();

            if(!(pathSource.substring(pathSource.length() - 1).equals("\\"))) {
                pathSource +="\\";
            }
            pathSource += fileForTableView.getName();
            System.out.println("source path " + pathSource);
            System.out.println("destiation path " + pathDestination);

            File source = new File(pathSource);
            File destination = new File(pathDestination);
            if(destination.exists()) {
                System.out.println("file exists");
                ButtonType button_tak = new ButtonType(resourceBundle.getString("yes"), ButtonBar.ButtonData.OK_DONE);
                ButtonType button_nie = new ButtonType(resourceBundle.getString("no"), ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        resourceBundle.getString("copyDialogContentText"),
                        button_tak,
                        button_nie);
                alert.setTitle(resourceBundle.getString("copyDialogTitle"));
                alert.setHeaderText(resourceBundle.getString("copyDialogHeaderText"));
                //alert.setContentText(resourceBundle.getString("copyDialogContentText"));

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == button_nie){
                    System.out.println("cancel");
                    return;
                }
            }
            CopyFileTask task = new CopyFileTask(source, destination, rightTableView);

            // Unbind progress property
            progressBar.progressProperty().unbind();

            // Bind progress property
            progressBar.progressProperty().bind(task.progressProperty());

            bindToWorker(task);
            try {
                addTask(task);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //rightTableView.setItems(getDirectoryContent(new File(rightPathLabel.getText())));
        }
    }

    @FXML
    void delete(ActionEvent event) {
        System.out.println("delete clicked");

        ButtonType button_tak = new ButtonType(resourceBundle.getString("yes"), ButtonBar.ButtonData.OK_DONE);
        ButtonType button_nie = new ButtonType(resourceBundle.getString("no"), ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                resourceBundle.getString("deleteDialogContentText"),
                button_tak,
                button_nie);
        alert.setTitle(resourceBundle.getString("deleteDialogTitle"));
        alert.setHeaderText(resourceBundle.getString("deleteDialogHeaderText"));
        //alert.setContentText(resourceBundle.getString("deleteDialogContentText"));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == button_nie){
            return;
        }

        FileForTableView fileForTableView = (FileForTableView) leftTableView.getSelectionModel().getSelectedItem();
        if (fileForTableView != null) {
            String pathSource = leftPathLabel.getText();

            if(!(pathSource.substring(pathSource.length() - 1).equals("\\"))) {
                pathSource +="\\";
            }
            pathSource += fileForTableView.getName();
            System.out.println("source path " + pathSource);

            File source = new File(pathSource);
            DeleteFileTask task = new DeleteFileTask(source);
            try {
                addTask(task);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //leftTableView.setItems(getDirectoryContent(new File(leftPathLabel.getText())));
        }
    }

    @FXML
    void move(ActionEvent event) {
        System.out.println("move clicked");
        FileForTableView fileForTableView = (FileForTableView) leftTableView.getSelectionModel().getSelectedItem();
        if (fileForTableView != null) {
            String pathSource = leftPathLabel.getText();
            String pathDestination = rightPathLabel.getText();
            if(!(pathDestination.substring(pathDestination.length() - 1).equals("\\"))) {
                pathSource +="\\";
            }
            pathDestination +=  "\\" + fileForTableView.getName();

            if(!(pathSource.substring(pathSource.length() - 1).equals("\\"))) {
                pathSource +="\\";
            }
            pathSource += fileForTableView.getName();
            System.out.println("source path " + pathSource);
            System.out.println("destiation path " + pathDestination);

            File source = new File(pathSource);
            File destination = new File(pathDestination);

            if(destination.exists()) {
                System.out.println("file exists");

                ButtonType button_tak = new ButtonType(resourceBundle.getString("yes"), ButtonBar.ButtonData.OK_DONE);
                ButtonType button_nie = new ButtonType(resourceBundle.getString("no"), ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        resourceBundle.getString("moveDialogContentText"),
                        button_tak,
                        button_nie);

                alert.setTitle(resourceBundle.getString("moveDialogTitle"));
                alert.setHeaderText(resourceBundle.getString("moveDialogHeaderText"));
                alert.setContentText(resourceBundle.getString("moveDialogContentText"));

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == button_nie){
                    return;
                }
            }

            MoveFileTask task = new MoveFileTask(source, destination);

            // Unbind progress property
            progressBar.progressProperty().unbind();

            // Bind progress property
            progressBar.progressProperty().bind(task.progressProperty());

            try {
                addTask(task);
            } catch (IOException e) {
                e.printStackTrace();
            }

            fillTableView();
        }
    }

    @FXML
    void mouseClickedOnTable(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
            TableView table = (TableView) event.getSource();

            FileForTableView filesList = (FileForTableView) table.getSelectionModel().selectedItemProperty().get();

            String path;
            Label label;
            if(table == leftTableView) {
                label = leftPathLabel;
                path = label.getText();
                System.out.println(path);
            } else {
                label = rightPathLabel;
                path = label.getText();
                System.out.println(path);
            }
            if(!(path.substring(path.length() - 1).equals("\\"))) {
                path +="\\";
            }
            path += filesList.getName();
            System.out.println(path);

            File file = new File(path);
            System.out.println(file.isDirectory());
            if(file.isDirectory()) {
                label.setText(path);
                table.setItems(getDirectoryContent(file));
            } else if(file.isFile()) {
                boolean openFile = openFile(file);
                System.out.println("open file " + openFile);
            }

            loadLang(lang);
        }
    }

    private void loadLang(String lang) {
        locale = new Locale(lang);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        resourceBundle = ResourceBundle.getBundle("sample.lang", locale);
        copy.setText(resourceBundle.getString("copy"));
        delete.setText(resourceBundle.getString("delete"));
        move.setText(resourceBundle.getString("move"));
        changeLng.setText(resourceBundle.getString("changeLng"));
        rightTableViewColumnName.setText(resourceBundle.getString("nameColumn"));
        leftTableViewColumnName.setText(resourceBundle.getString("nameColumn"));
        leftTableViewColumDate.setText(resourceBundle.getString("dateColumn"));
        rightTableViewColumDate.setText(resourceBundle.getString("dateColumn"));
        rightTableViewColumSize.setText(resourceBundle.getString("sizeColumn"));
        leftTableViewColumSize.setText(resourceBundle.getString("sizeColumn"));
        upLeftButton.setText(resourceBundle.getString("up"));
        upRightButton.setText(resourceBundle.getString("up"));

        ObservableList<FileForTableView> list1 = leftTableView.getItems();
        int i = 0;
        if (!list1.isEmpty()) {
            for (FileForTableView file : list1) {
                file.setFormatedLastModifiedDate(df.format(file.getLastModifiedDate()));
                if(file.isDirectory()) {
                    file.setSize(resourceBundle.getString("directory"));
                }
                list1.set(i, file);
                i++;
            }
        }

        i = 0;
        ObservableList<FileForTableView> list2 = rightTableView.getItems();
        if (!list2.isEmpty()) {
            for (FileForTableView file : list2) {
                file.setFormatedLastModifiedDate(df.format(file.getLastModifiedDate()));
                if(file.isDirectory()) {
                    file.setSize(resourceBundle.getString("directory"));
                }
                list2.set(i, file);
                i++;
            }
        }
        fillTableView(list1, list2);
    }

    private void choiceBoxListener(Event event) {
        ChoiceBox<File> cb = (ChoiceBox<File>) event.getTarget();
        if (cb == leftChoiceBox) {
            leftPathLabel.setText(cb.getValue().toString());
            fillTableView(false);
        } else {
            rightPathLabel.setText(cb.getValue().toString());
            fillTableView(true);
        }
        loadLang(lang);
        System.out.println(cb.getValue());
    }

    public static ObservableList<FileForTableView> getDirectoryContent(File file) {
        String[] fileList = file.list();
        String path = file.getPath();
        if(!(path.substring(path.length() - 1).equals("\\"))) {
            path +="\\";
        }

        ObservableList<FileForTableView> list = FXCollections.observableArrayList();
        if (fileList != null) {
            ArrayList<String> names = new ArrayList<String>(Arrays.asList(fileList));
            for (String fileName : names) {
                if (!fileName.equals("System Volume Information")) {
                    File file2 = new File(path + fileName);
                    Long size = file2.length();
                    resourceBundle = ResourceBundle.getBundle("sample.lang", locale);
                    if(file2.isDirectory()) {
                        String sizeString = resourceBundle.getString("directory");
                        Date date = new Date(file2.lastModified());
                        list.add(new FileForTableView(fileName, sizeString, true, date));
                    } else {
                        Date date = new Date(file2.lastModified());
                        list.add(new FileForTableView(fileName, size, false, date));
                    }
                }
            }
        }
        return list;
    }

    private void up(boolean isRight) {
        Label pathlabel;
        TableView tableView;
        if(isRight) {
            pathlabel = rightPathLabel;
            tableView = rightTableView;
        } else {
            pathlabel = leftPathLabel;
            tableView = leftTableView;
        }

        String path = pathlabel.getText();
        if(!(path.substring(path.length() - 1).equals("\\"))) {
            path += "\\..";
        } else {
            return;
        }
        File file = new File(path);
        tableView.setItems(getDirectoryContent(file));
        try {
            pathlabel.setText(file.getCanonicalPath().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean openFile(final File file) {
        if (!Desktop.isDesktopSupported()) {
            System.out.println("Desktop not supported");
            return false;
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.OPEN)) {
            System.out.println("Desktop .Action.EDIT not supported");
            return false;
        }

        try {
            desktop.open(file);
        } catch (IOException e) {
            // Log an error
            System.out.println("File not opened");
            return false;
        }

        return true;
    }

    private void addTask(CopyFileTask task) throws IOException {
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        task.setOnSucceeded(tEvent -> {
            System.out.println("Success");
            String path = rightPathLabel.getText();
            File file = new File(path);
            ObservableList<FileForTableView> list = getDirectoryContent(file);
            rightTableView.setItems(list);
            loadLang(lang);
        });
        backgroundThread.start();
    }

    private void addTask(MoveFileTask task) throws IOException {
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        task.setOnSucceeded(tEvent -> {
            System.out.println("Success");
            String path = rightPathLabel.getText();
            File file = new File(path);
            ObservableList<FileForTableView> list = getDirectoryContent(file);
            rightTableView.setItems(list);

            String path2 = leftPathLabel.getText();
            File file2 = new File(path2);
            list = getDirectoryContent(file2);
            leftTableView.setItems(list);
            loadLang(lang);
        });
        backgroundThread.start();
    }

    private void addTask(DeleteFileTask task) throws IOException {
        Thread backgroundThread = new Thread(task);
        backgroundThread.setDaemon(true);
        task.setOnSucceeded(tEvent -> {
            System.out.println("Success");
            String path = leftPathLabel.getText();
            File file = new File(path);
            ObservableList<FileForTableView> list = getDirectoryContent(file);
            leftTableView.setItems(list);
            loadLang(lang);
        });
        backgroundThread.start();
    }

    public void bindToWorker(final Worker<ObservableList<FileForTableView>> worker)
    {
        // Bind TableView to the properties of the worker
        //rightTableView.itemsProperty().bind(worker.valueProperty());

    }

    public void fillTableView() {
        String path = rightPathLabel.getText();
        System.out.println("right path = " + path);
        rightTableView.setItems(getDirectoryContent(new File(path)));
        path = leftPathLabel.getText();
        System.out.print(" left path = " + path);
        leftTableView.setItems(getDirectoryContent(new File(path)));
        //loadLang(lang);
    }

    public void fillTableView(boolean fillRightTableView) {
        if(fillRightTableView) {
            String path = rightPathLabel.getText();
            System.out.println("right path = " + path);
            rightTableView.setItems(getDirectoryContent(new File(path)));
        } else {
            String path = leftPathLabel.getText();
            System.out.println("left path = " + path);
            leftTableView.setItems(getDirectoryContent(new File(path)));
        }
        //loadLang(lang);
    }

    public void fillTableView(ObservableList<FileForTableView> leftList, ObservableList<FileForTableView> rightList) {
        leftTableView.setItems(leftList);
        rightTableView.setItems(rightList);
        //loadLang(lang);
    }
}

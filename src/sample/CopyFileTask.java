package sample;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class CopyFileTask extends Task<ObservableList<FileForTableView>> {

    private File source;
    private File destination;
    private javafx.scene.control.TableView rightTable;

    public CopyFileTask(File source, File destination, javafx.scene.control.TableView rightTable) {
        this.source = source;
        this.destination = destination;
        this.rightTable = rightTable;
    }

    public CopyFileTask(String sourceString, String destinationString, javafx.scene.control.TableView rightTable) {
        this.source = new File(sourceString);
        this.destination = new File(destinationString);
        this.rightTable = rightTable;
    }

    // The task implementation
    @Override
    protected ObservableList<FileForTableView> call()
    {
        final ObservableList<FileForTableView> results = Controller.getDirectoryContent(destination);
        // Update the title
        this.updateTitle("Prime Number Finder Task");

        // Sleep for some time
        try {
            this.updateProgress(1,10);
            copyFile(source, destination);
            this.updateProgress(10,10);
            System.out.println("file copied");
            results.add(new FileForTableView(destination.getName(), destination.length(), destination.isDirectory(), new Date(destination.lastModified())));
            rightTable.setItems(results);
        } /*catch (InterruptedException e) {
                // Check if the task is cancelled
                if (this.isCancelled())
                {
                    break;
                }
            }*/catch (IOException e) {
                String s = e.getMessage();
                s = s.substring(s.length() - 12);
                if(s.equals("are the same")) {
                    this.updateProgress(10,10);
                } else {
                    e.printStackTrace();
                }
        }
        return results;
    }

    @Override
    protected void cancelled()
    {
        super.cancelled();
        updateMessage("The task was cancelled.");
    }

    @Override
    protected void failed()
    {
        super.failed();
        updateMessage("The task failed.");
    }

    @Override
    public void succeeded()
    {
        super.succeeded();
        updateMessage("The task finished successfully.");
    }


    private void copyFile(File source, File destination) throws IOException {
        this.updateProgress(3,10);
        if (source.isDirectory()) {
            FileUtils.copyDirectory(source, destination, new ProgressFilter());
        } else {
            FileUtils.copyFile(source, destination);
            //Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        this.updateProgress(6,10);
    }

    public File getSource() {
        return source;
    }

    public File getDestination() {
        return destination;
    }

    class ProgressFilter implements FileFilter {
        private int workDone;
        private int max;

        public ProgressFilter() {
            this.workDone = 6;
            this.max = 10;
        }

        @Override
        public boolean accept(File f) {
            workDone++;
            max++;
            updateProgress(workDone, max);
            return true;
        }
    }
}

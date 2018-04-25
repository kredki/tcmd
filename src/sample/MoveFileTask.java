package sample;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;

public class MoveFileTask extends Task<ObservableList<FileForTableView>> {
    private File source;
    private File destination;

    public MoveFileTask(File source, File destination) {
        this.source = source;
        this.destination = destination;
    }

    public MoveFileTask(String sourceString, String destinationString) {
        this.source = new File(sourceString);
        this.destination = new File(destinationString);
    }

    // The task implementation
    @Override
    protected ObservableList<FileForTableView> call()
    {
        final ObservableList<FileForTableView> results = Controller.getDirectoryContent(destination);

        // Sleep for some time
        try {
            this.updateProgress(1,10);
            moveFile(source, destination);
            this.updateProgress(10,10);
            System.out.println("file moved");
            results.add(new FileForTableView(destination.getName(), destination.length(), destination.isDirectory(), new Date(destination.lastModified())));
        } /*catch (InterruptedException e) {
                // Check if the task is cancelled
                if (this.isCancelled())
                {
                    break;
                }
            }*/ catch (IOException e) {
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


    private void moveFile(File source, File destination) throws IOException {
        this.updateProgress(3,10);
        if (source.isDirectory()) {
            FileUtils.moveDirectory(source, destination);
        } else {
            //FileUtils.moveFile(source, destination);
            Files.move(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        this.updateProgress(6,10);
    }

    public File getSource() {
        return source;
    }

    public File getDestination() {
        return destination;
    }
}

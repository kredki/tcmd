package sample;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

public class DeleteFileTask extends Task<ObservableList<FileForTableView>> {
    private File file;

    public DeleteFileTask(File file) {
        this.file = file;
    }

    public DeleteFileTask(String filePath) {
        this.file = new File(filePath);
    }

    // The task implementation
    @Override
    protected ObservableList<FileForTableView> call()
    {
        final ObservableList<FileForTableView> results = Controller.getDirectoryContent(file);

        // Sleep for some time
        try {
            FileForTableView fileToRemove = new FileForTableView(file.getName(), file.length(), file.isDirectory(), new Date(file.lastModified()));
            deleteFile(file);
            System.out.println("file deleted");
            results.remove(fileToRemove);
        } /*catch (InterruptedException e) {
                // Check if the task is cancelled
                if (this.isCancelled())
                {
                    break;
                }
            }*/ catch (IOException e) {
            e.printStackTrace();
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


    private static void deleteFile(File file) throws IOException {
        if (file.isDirectory()) {
            FileUtils.deleteDirectory(file);
        } else {
            FileUtils.deleteQuietly(file);
            //Files.delete(file.toPath());
        }
    }

    public File getFile() {
        return file;
    }
}

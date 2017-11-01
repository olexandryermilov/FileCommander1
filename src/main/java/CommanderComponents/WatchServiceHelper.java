package CommanderComponents;

import Adapters.FileSystemObject;
import CommanderComponents.FileCommanderOperations;

import java.io.IOException;
import java.nio.file.WatchKey;
import java.nio.file.*;

public class WatchServiceHelper {

    private WatchService watchService;
    private WatchKey watchKey;
    private volatile Thread watchThread;
    private String currentDirectory;

    WatchServiceHelper(FileCommanderOperationsFacade operations, FileCommanderListPanel listPanel) {

        try {
            watchService = FileSystems.getDefault().newWatchService();
            watchKey =new FileSystemObject(listPanel.
                    getFileCommanderListModel().
                    getSelectedDirectory()).toPath().register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        watchThread = new Thread(()->{
            while(true){
                if(currentDirectory==null||currentDirectory.equals(""))continue;
                try {
                    WatchKey tempWatchKey = watchService.take();
                    tempWatchKey.pollEvents();
                    operations.refreshLists();
                    tempWatchKey.reset();
                } catch (InterruptedException e) {

                }
            }
        });
        watchThread.start();
    }

    void changeObservableDirectory(String newDirectory) {

        if (currentDirectory!=null&&currentDirectory.equals(newDirectory)) return;
        watchKey.cancel();
        try {
            watchKey = new FileSystemObject(newDirectory).toPath().register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
            currentDirectory = newDirectory;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
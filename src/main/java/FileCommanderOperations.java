import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;

public class FileCommanderOperations {
    FileCommanderFrame frame;
    FileCommanderOperations(FileCommanderFrame frame){
        this.frame = frame;
    }
    public void createNewFile(String path){
        try {
            FileSystemObject file = new FileSystemObject(path);
            file.createNewFile();
            refreshLists();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    void refreshLists(){

        FileSystemObject selectedDirectoryFile = new FileSystemObject(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory());
        if(!frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory().equals("")){
            frame.getLeftListPanel().getFileCommanderListModel().getListModel().clear();
            frame.getLeftListPanel().getFileCommanderListModel().getListModel().addElement("..");
            for(File file: selectedDirectoryFile.listFiles()){
                if(!file.isHidden())frame.getLeftListPanel().getFileCommanderListModel().getListModel().addElement(file.toString());
            }
        }

        selectedDirectoryFile = new FileSystemObject(frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory());
        if(!frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory().equals("")) {
            frame.getRightListPanel().getFileCommanderListModel().getListModel().clear();
            frame.getRightListPanel().getFileCommanderListModel().getListModel().addElement("..");
            for (File file : selectedDirectoryFile.listFiles()) {
                if(!file.isHidden())frame.getRightListPanel().getFileCommanderListModel().getListModel().addElement(file.toString());
            }
        }
    }
    void createNewFolder(String path){
        try{
            FileSystemObject file = new FileSystemObject(path);
            FileUtils.forceMkdir(file);
            refreshLists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void copyFromLeft(){
        String from = frame.getLeftListPanel().getList().getSelectedValue();
        String to = frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory();
        copyFile(from,to);
    }
    void copyFromRight(){
        String from = frame.getRightListPanel().getList().getSelectedValue();
        String to = frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory();
        copyFile(from,to);
    }
    void copyFile(String from, String to){
        try {
            FileSystemObject fileFrom = new FileSystemObject(from);
            FileSystemObject fileTo = new FileSystemObject(to + "\\" + fileFrom.getName());
            if (!fileFrom.isDirectory()) FileUtils.copyFile(fileFrom, fileTo);
            else FileUtils.copyDirectory(fileFrom,fileTo);
            refreshLists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void deleteFile(String path){
        try{
            FileSystemObject file = new FileSystemObject(path);
            FileUtils.forceDelete(file);
            refreshLists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void removeFile(String from, String to){
        try{
            copyFile(from,to);
            deleteFile(from);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void renameFile(String path, String name){
        try{
            FileSystemObject file = new FileSystemObject(path);
            file.renameTo(new FileSystemObject(file.getParent()+"\\"+name));
            refreshLists();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

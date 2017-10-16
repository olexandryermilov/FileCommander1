import java.io.File;
import java.io.IOException;

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
}

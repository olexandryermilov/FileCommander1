package commanderComponents;

import javax.swing.*;

public class FileCommanderListModel {
    FileCommanderListController getFileCommanderListController() {
        return fileCommanderListController;
    }

    private FileCommanderListController fileCommanderListController;

    private FileCommanderFrame frame;
    private String selectedDirectory;

    public String getSelectedExtension() {
        return selectedExtension;
    }

    public void setSelectedExtension(String selectedExtension) {
        this.selectedExtension = selectedExtension;
    }

    private String selectedExtension;
    private DefaultListModel<String> listModel;
    DefaultListModel<String> getListModel(){
        return listModel;
    }

    FileCommanderListModel(FileCommanderFrame frame, FileCommanderListPanel listPanel){
        this.frame = frame;
        selectedDirectory = "";
        selectedExtension="";
        listModel = new DefaultListModel<>();
        fileCommanderListController = new FileCommanderListController(this, frame,listPanel);
        fileCommanderListController.addRootsToListModel();
    }

    String getSelectedDirectory() {
        return selectedDirectory;
    }

    void setSelectedDirectory(String selectedDirectory) {
        this.selectedDirectory = selectedDirectory;
    }
}

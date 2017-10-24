package CommanderComponents;

import CommanderComponents.FileCommanderFrame;
import CommanderComponents.FileCommanderListController;

import javax.swing.*;

public class FileCommanderListModel {
    public FileCommanderListController getFileCommanderListController() {
        return fileCommanderListController;
    }

    private FileCommanderListController fileCommanderListController;

    private FileCommanderFrame frame;
    private String selectedDirectory;
    private DefaultListModel<String> listModel;
    public DefaultListModel<String> getListModel(){
        return listModel;
    }

    public FileCommanderListModel(FileCommanderFrame frame, FileCommanderListPanel listPanel){
        this.frame = frame;
        selectedDirectory = "";
        listModel = new DefaultListModel<>();
        fileCommanderListController = new FileCommanderListController(this, frame,listPanel);
        fileCommanderListController.addRootsToListModel();
    }

    public String getSelectedDirectory() {
        return selectedDirectory;
    }

    public void setSelectedDirectory(String selectedDirectory) {
        this.selectedDirectory = selectedDirectory;
    }
}

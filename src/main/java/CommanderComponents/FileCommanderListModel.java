package CommanderComponents;

import CommanderComponents.FileCommanderFrame;
import CommanderComponents.FileCommanderListController;

import javax.swing.*;

public class FileCommanderListModel {
    FileCommanderListController getFileCommanderListController() {
        return fileCommanderListController;
    }

    private FileCommanderListController fileCommanderListController;

    private FileCommanderFrame frame;
    private String selectedDirectory;
    private DefaultListModel<String> listModel;
    DefaultListModel<String> getListModel(){
        return listModel;
    }

    FileCommanderListModel(FileCommanderFrame frame, FileCommanderListPanel listPanel){
        this.frame = frame;
        selectedDirectory = "";
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

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

public class FileCommanderListModel {
    private FileCommanderListController fileCommanderListController;
    private FileCommanderFrame frame;
    private String selectedDirectory;
    private DefaultListModel<String> listModel;
    public DefaultListModel<String> getListModel(){
        return listModel;
    }
    public FileCommanderListModel(){
        selectedDirectory = "";
        listModel = new DefaultListModel<>();
        fileCommanderListController = new FileCommanderListController(this, frame);
        fileCommanderListController.addRootsToListModel();
    }

    public String getSelectedDirectory() {
        return selectedDirectory;
    }

    public void setSelectedDirectory(String selectedDirectory) {
        this.selectedDirectory = selectedDirectory;
    }
}

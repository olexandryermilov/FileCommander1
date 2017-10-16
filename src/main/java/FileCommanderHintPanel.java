import javax.swing.*;

public class FileCommanderHintPanel extends JPanel {
    private FileCommanderFrame frame;
    private String half;
    private JButton newFileButton, newFolderButton, copyFileButton, deleteFileButton, removeFileButton, renameFileButton;
    private HintPanelActionListener actionListener;
    private void initializeNewFileButton(){
        newFileButton = new JButton("New File");
        newFileButton.setActionCommand("New File "+half);
        newFileButton.addActionListener(actionListener);
        this.add(newFileButton);
    }
    private void initializeNewFolderButton(){
        newFolderButton = new JButton("New Folder");
        newFolderButton.setActionCommand("New Folder "+ half);
        newFolderButton.addActionListener(actionListener);
        this.add(newFolderButton);
    }
    private void initializeCopyFileButton(){
        copyFileButton = new JButton("Copy");
        copyFileButton.setActionCommand("Copy "+ half);
        copyFileButton.addActionListener(actionListener);
        this.add(copyFileButton);
    }
    private void initializeDeleteFileButton(){
        deleteFileButton = new JButton("Delete");
        deleteFileButton.setActionCommand("Delete "+half);
        deleteFileButton.addActionListener(actionListener);
        this.add(deleteFileButton);
    }
    private void initializeRemoveFileButton(){
        removeFileButton = new JButton("Remove");
        removeFileButton.setActionCommand("Remove "+half);
        removeFileButton.addActionListener(actionListener);
        this.add(removeFileButton);
    }
    private void initializeRenameFileButton(){
        renameFileButton =new JButton("Rename");
        renameFileButton.setActionCommand("Rename "+ half);
        renameFileButton.addActionListener(actionListener);
        this.add(renameFileButton);
    }
    private void addButtons(){
        initializeNewFileButton();
        initializeNewFolderButton();
        initializeCopyFileButton();
        initializeDeleteFileButton();
        initializeRemoveFileButton();
        initializeRenameFileButton();
    }
    public FileCommanderHintPanel(FileCommanderFrame frame, String half){
        super();
        this.frame = frame;
        this.half = half;
        actionListener = new HintPanelActionListener(frame);
        addButtons();
    }
}

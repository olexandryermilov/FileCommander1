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
    private void addButtons(){
        initializeNewFileButton();
        initializeNewFolderButton();
    }
    public FileCommanderHintPanel(FileCommanderFrame frame, String half){
        super();
        this.frame = frame;
        this.half = half;
        actionListener = new HintPanelActionListener(frame);
        addButtons();
    }
}

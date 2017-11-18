package commanderComponents;

import javax.swing.*;

class FileCommanderHintPanel extends JPanel {
    private FileCommanderFrame frame;
    private String half;
    private JButton newFileButton, newFolderButton, copyFileButton, deleteFileButton, removeFileButton, renameFileButton, openFileButton;
    private JButton editFileButton,copyFileWithoutRepeatingLinesButton,convertFileFromHtmlToRtfButton,copySelectedExtensionButton,copyHtmlButton,frequencyButton;
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
    private void initializeOpenFileButton(){
        openFileButton = new JButton("Open");
        openFileButton.setActionCommand("Open "+ half);
        openFileButton.addActionListener(actionListener);
        this.add(openFileButton);
    }
    private void initializeEditFileButton(){
        editFileButton = new JButton("Open same dir");
        editFileButton.setActionCommand("Open same dir "+half);
        editFileButton.addActionListener(actionListener);
        this.add(editFileButton);
    }
    private void initializeCopyFileWithoutRepeatingLinesButton(){
        copyFileWithoutRepeatingLinesButton = new JButton("Non-repeating copy");
        copyFileWithoutRepeatingLinesButton.setActionCommand("Copy without repeating lines "+half);
        copyFileWithoutRepeatingLinesButton.addActionListener(actionListener);
        this.add(copyFileWithoutRepeatingLinesButton);
    }
    private void initializeConvertFileFromHtmlToRtfButton(){
        convertFileFromHtmlToRtfButton = new JButton("Html to PDF");
        convertFileFromHtmlToRtfButton.addActionListener(actionListener);
        convertFileFromHtmlToRtfButton.setActionCommand("Html to PDF "+half);
        this.add(convertFileFromHtmlToRtfButton);
    }
    private void initializeCopySelectedExtensionButton(){
        copySelectedExtensionButton = new JButton("Copy selected extension");
        copySelectedExtensionButton.setActionCommand("Copy extension "+ half);
        copySelectedExtensionButton.addActionListener(actionListener);
        this.add(copySelectedExtensionButton);
    }
    private void initializeCopyHtmlButton(){
        copyHtmlButton = new JButton("Copy HTML");
        copyHtmlButton.addActionListener(actionListener);
        copyHtmlButton.setActionCommand("Copy HTML "+half);
        this.add(copyHtmlButton);
    }
    private void initializeFrequencyButton(){
        frequencyButton = new JButton("Frequency");
        frequencyButton.setActionCommand("Frequency "+ half);
        frequencyButton.addActionListener(actionListener);
        this.add(frequencyButton);
    }
    private void addButtons(){
        initializeNewFileButton();
        initializeNewFolderButton();
        initializeCopyFileButton();
        initializeOpenFileButton();
        initializeDeleteFileButton();
        initializeRemoveFileButton();
        initializeRenameFileButton();
        initializeEditFileButton();
        initializeCopyFileWithoutRepeatingLinesButton();
        initializeConvertFileFromHtmlToRtfButton();
        initializeCopyHtmlButton();
        initializeFrequencyButton();
        initializeCopySelectedExtensionButton();
    }
    private JLabel extensionLabel;
    private String[] extensions = {".*", ".txt",".jpg",".html",".rtf",".doc",".pdf",".xlsx"};

    public JComboBox<String> getExtensionComboBox() {
        return extensionComboBox;
    }

    private JComboBox<String> extensionComboBox;
    private void addExtensionSelection(){
        extensionLabel = new JLabel("Select extension filter to show");
        extensionComboBox = new JComboBox<>(extensions);
        extensionComboBox.addActionListener((e -> {
            frame.getFileCommanderOperationsFacade().updateListWithExtension((String)extensionComboBox.getSelectedItem(),half);
        }));
        this.add(extensionLabel);
        this.add(extensionComboBox);
    }
    FileCommanderHintPanel(FileCommanderFrame frame, String half){
        super();
        this.frame = frame;
        this.half = half;
        actionListener = new HintPanelActionListener(frame);
        addButtons();
        addExtensionSelection();
    }
}

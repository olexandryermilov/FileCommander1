package editor;

import javax.swing.*;

public class FileEditorButtonPanel extends JPanel {

    private FileEditorController controller;
    private FileEditorFrame frame;
    private EditorTableModel model;
    private JButton addColumnButton,addRowButton;
    private void initAddColumnButton(){
        addColumnButton = new JButton("Add column");
        addColumnButton.setActionCommand("add column");
        addColumnButton.addActionListener(e -> {
            if(e.getActionCommand().equals("add column")){
                controller.addColumn();
            }
        });
        this.add(addColumnButton);
    }
    private void initAddRowButton(){
        addRowButton = new JButton("Add row");
        addRowButton.setActionCommand("add row");
        addRowButton.addActionListener(e -> {
            if(e.getActionCommand().equals("add row")){
                controller.addRow();
            }
        });
        this.add(addRowButton);
    }
    private void initButtons(){
        initAddColumnButton();
        initAddRowButton();
    }
    public FileEditorButtonPanel(FileEditorController controller, FileEditorFrame frame, EditorTableModel model){
        super();
        this.controller = controller;
        this.frame=frame;
        this.model=model;
        initButtons();
    }

}

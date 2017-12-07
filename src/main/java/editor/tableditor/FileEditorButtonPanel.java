package editor.tableditor;

import javax.swing.*;

public class FileEditorButtonPanel extends JPanel {

    private FileEditorController controller;
    private FileEditorFrame frame;
    private EditorTableModel model;
    private JButton addColumnButton,addRowButton,helpButton;
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
    private void initHelpButton(){
        helpButton = new JButton("Help");
        helpButton.setActionCommand("help");
        helpButton.addActionListener(e -> {
            if(e.getActionCommand().equals("help")){
                JOptionPane.showMessageDialog(frame,"Help:\n" +
                        "To calculate something with if you should use it like:\n" +
                        "if(booleanExpression)then Expression; else Expression;\n" +
                        "Supported operations:\n" +
                        "+ - * / ^ < > != <= >= and or xor not\n" +
                        "max(a1,...,an)n>=1 min(a1,...,an)n>=1 sum(a1,...,an)n>=1");
            }
        });
        this.add(helpButton);
    }
    private void initButtons(){
        initAddColumnButton();
        initAddRowButton();
        initHelpButton();
    }
    public FileEditorButtonPanel(FileEditorController controller, FileEditorFrame frame, EditorTableModel model){
        super();
        this.controller = controller;
        this.frame=frame;
        this.model=model;
        initButtons();
    }

}

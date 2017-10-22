package CommanderComponents;

import javax.swing.*;

public class FileCommanderMenuBar extends JMenuBar {
    JMenu file,operations,help;
    JMenuItem open,edit;
    //JMenuItem ;
    FileCommanderFrame frame;
    HintPanelActionListener actionListener;
    private void initializeFileItem(){
        file = new JMenu("File");
        open = new JMenuItem("Open");
        open.addActionListener(new HintPanelActionListener(frame));
        open.setActionCommand("Open left");
        edit = new JMenuItem("Edit");
        file.add(open);
        file.add(edit);
        this.add(file);
    }
    private void initializeOperationsItem(){
        operations = new JMenu("Operations");
        this.add(operations);
    }
    private void initializeHelpItem(){
        help = new JMenu("Help");
        this.add(help);
    }
    FileCommanderMenuBar(FileCommanderFrame frame){
        super();
        this.frame = frame;
        actionListener = new HintPanelActionListener(frame);
        initializeFileItem();
        initializeOperationsItem();
        initializeHelpItem();
        //frame.add(this);
    }
}

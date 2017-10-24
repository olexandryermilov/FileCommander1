package CommanderComponents;

import Adapters.GridBagConstraintsAdapter;
import CommanderComponents.FileCommanderFrame;
import CommanderComponents.FileCommanderHintPanel;
import CommanderComponents.FileCommanderListController;
import CommanderComponents.FileCommanderListModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.WatchService;

public class FileCommanderListPanel extends JPanel {
    private JList<String> list;

    public JList<String> getList() {
        return list;
    }

    private FileCommanderListModel fileCommanderListModel;
   ///FileCommanderListController fileCommanderListController;
    FileCommanderHintPanel hintPanel;
    JScrollPane scrollPane;
    JSplitPane splitPane;
    FileCommanderFrame frame;
    String half;
    WatchServiceHelper watchServiceHelper;
    public FileCommanderListModel getFileCommanderListModel(){
        return fileCommanderListModel;
    }
    public FileCommanderListPanel(FileCommanderFrame frame, String half){
        super();
        this.frame = frame;
        this.half = half;
        this.setLayout(new GridBagLayout());
        fileCommanderListModel = new FileCommanderListModel(frame,this);
        list = new JList<>(fileCommanderListModel.getListModel());
        list.addMouseListener(new FileCommanderMouseListener());
        scrollPane = new JScrollPane(list);
        hintPanel = new FileCommanderHintPanel(frame,half);
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scrollPane,hintPanel);
        splitPane.setResizeWeight(0.8);

        this.add(splitPane,new GridBagConstraintsAdapter(1,1,1,1,1,1).setFill(GridBagConstraintsAdapter.BOTH));
    }
    public void setWatchServiceHelper(WatchServiceHelper watchServiceHelper){
        this.watchServiceHelper = watchServiceHelper;
    }
    //public FileCommanderListController getFileCommanderListController() {
        //return fileCommanderListController;
    //}

    private class FileCommanderMouseListener extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton()== MouseEvent.BUTTON1&&e.getClickCount()>=2){
                fileCommanderListModel.getFileCommanderListController().handleDoubleClick(list.getSelectedValue());
            }
        }
    }
}

package commanderComponents;

import adapters.GridBagConstraintsAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FileCommanderListPanel extends JPanel {
    private JList<String> list;

    public JList<String> getList() {
        return list;
    }

    private FileCommanderListModel fileCommanderListModel;
    private FileCommanderListPanel anotherListPanel;
    private FileCommanderHintPanel hintPanel;
    private JScrollPane scrollPane;
    private JSplitPane splitPane;

    public String getHalf() {
        return half;
    }

    public FileCommanderHintPanel getHintPanel() {
        return hintPanel;
    }

    private FileCommanderFrame frame;
    private String half;
    WatchServiceHelper watchServiceHelper;
    FileCommanderListModel getFileCommanderListModel(){
        return fileCommanderListModel;
    }
    FileCommanderListPanel(FileCommanderFrame frame, String half){
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
        splitPane.setResizeWeight(0.75);

        this.add(splitPane,new GridBagConstraintsAdapter(1,1,1,1,1,1).setFill(GridBagConstraintsAdapter.BOTH));
    }
    void setAnotherListPanel(FileCommanderListPanel anotherListPanel){
        this.anotherListPanel=anotherListPanel;
    }
    void setWatchServiceHelper(WatchServiceHelper watchServiceHelper){
        this.watchServiceHelper = watchServiceHelper;
    }
    private class FileCommanderMouseListener extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton()== MouseEvent.BUTTON1&&e.getClickCount()>=2){
                fileCommanderListModel.getFileCommanderListController().handleDoubleClick(list.getSelectedValue());
            }
            if(e.getButton()== MouseEvent.BUTTON1&&e.getClickCount()==1){
                list.setSelectionBackground(Color.CYAN);
                anotherListPanel.getList().setSelectionBackground(Color.LIGHT_GRAY);
            }
        }
    }
}

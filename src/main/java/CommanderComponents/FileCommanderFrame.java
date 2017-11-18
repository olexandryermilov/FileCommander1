package commanderComponents;

import adapters.GridBagConstraintsAdapter;

import javax.swing.*;
import java.awt.*;


public class FileCommanderFrame extends JFrame{
    private final static String TITLE = "File Commander";
    private FileCommanderMenuBar menuBar;
    private JPanel mainPanel;
    private FileCommanderListPanel leftListPanel, rightListPanel;
    private FileCommanderOperationsFacade fileCommanderOperationsFacade;
    private JSplitPane splitPane;

    private void initializeMainPanel(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
    }
    FileCommanderListPanel getLeftListPanel(){
        return leftListPanel;
    }
    FileCommanderListPanel getRightListPanel(){
        return rightListPanel;
    }

    public FileCommanderFrame(){
        super();
        this.setTitle(TITLE);
        this.setLayout(new GridBagLayout());
        setFrameSize();
        menuBar = new FileCommanderMenuBar(this);
        this.fileCommanderOperationsFacade = new FileCommanderOperationsFacade(this);
        this.fileCommanderOperationsFacade.setFrame(this);
        leftListPanel = new FileCommanderListPanel(this,"left");
        leftListPanel.setWatchServiceHelper(new WatchServiceHelper(fileCommanderOperationsFacade,leftListPanel));

        rightListPanel = new FileCommanderListPanel(this,"right");
        rightListPanel.setWatchServiceHelper(new WatchServiceHelper(fileCommanderOperationsFacade,rightListPanel));
        leftListPanel.setAnotherListPanel(rightListPanel);
        rightListPanel.setAnotherListPanel(leftListPanel);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,leftListPanel,rightListPanel);
        splitPane.setResizeWeight(0.5);
        initializeMainPanel();
        mainPanel.add(splitPane,new GridBagConstraintsAdapter(1, 1, 1, 1, 1, 12).setFill(GridBagConstraintsAdapter.BOTH));
        this.setJMenuBar(menuBar);
        this.add(mainPanel, new GridBagConstraintsAdapter(1,1,1,1,1,1).setFill(GridBagConstraintsAdapter.BOTH));
    }
    private void setFrameSize(){
        super.setLocationByPlatform(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = dim.width, y = dim.height;
        super.setSize((4 * x) / 5, 4 * y / 5);
    }

    public FileCommanderOperationsFacade getFileCommanderOperationsFacade() {
        return fileCommanderOperationsFacade;
    }
}

package CommanderComponents;

import Adapters.GridBagConstraintsAdapter;

import javax.swing.*;
import java.awt.*;


public class FileCommanderFrame extends JFrame{
    private final static String TITLE = "File Commander";
    private FileCommanderMenuBar menuBar;
    private JPanel mainPanel;
    private FileCommanderListPanel leftListPanel, rightListPanel;
    private FileCommanderOperations fileCommanderOperations;
    private JSplitPane splitPane;

    public void initializeMainPanel(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
    }
    public FileCommanderListPanel getLeftListPanel(){
        return leftListPanel;
    }
    public FileCommanderListPanel getRightListPanel(){
        return rightListPanel;
    }

    public FileCommanderFrame(){
        super();
        this.setTitle(TITLE);
        this.setLayout(new GridBagLayout());
        setFrameSize();
        menuBar = new FileCommanderMenuBar(this);
        this.fileCommanderOperations = new FileCommanderOperations(this);
        this.fileCommanderOperations.setFrame(this);
        leftListPanel = new FileCommanderListPanel(this,"left");
        leftListPanel.setWatchServiceHelper(new WatchServiceHelper(fileCommanderOperations,leftListPanel));

        rightListPanel = new FileCommanderListPanel(this,"right");
        rightListPanel.setWatchServiceHelper(new WatchServiceHelper(fileCommanderOperations,rightListPanel));
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

    public FileCommanderOperations getFileCommanderOperations() {
        return fileCommanderOperations;
    }
}

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.openInputStream;

public class FileEditorFrame extends JFrame {
    private static final String TITLE = "File Editor";
    private JPanel mainPanel;
    private JEditorPane editorPane;
    private void initializeMainPanel(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
    }
    private FileSystemObject file;
    private void openFile(){
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fileInputStream = FileUtils.openInputStream(file);
            while(fileInputStream.available()>0){
                sb.append((char)fileInputStream.read());
            }
            editorPane.setText(new String(sb));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initializeEditorPane(){
        editorPane = new JEditorPane();
        mainPanel.add(editorPane,new GridBagConstraintsAdapter(1,1,1,1,1,1).setFill(GridBagConstraintsAdapter.BOTH));
    }
    private void setFrameSize(){
        super.setLocationByPlatform(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = dim.width, y = dim.height;
        super.setSize((4 * x) / 5, 4 * y / 5);
    }
    FileEditorFrame(FileSystemObject file){
        super();
        this.setTitle(TITLE);
        this.file=file;
        setFrameSize();
        initializeMainPanel();
        initializeEditorPane();
        openFile();
        this.add(mainPanel);
    }
//todo:add message is file exits
}

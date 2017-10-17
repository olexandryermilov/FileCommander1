import javax.swing.*;
import java.awt.*;

public class FileEditorFrame extends JFrame {
    private static final String TITLE = "File Editor";
    private JPanel mainPanel;
    private JEditorPane editorPane;
    private void initializeMainPanel(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
    }
    FileEditorFrame(){
        super();
        this.setTitle(TITLE);

    }

}

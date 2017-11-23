package editor;

import adapters.FileSystemObject;
import adapters.GridBagConstraintsAdapter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class FileEditorFrame extends JFrame {
    private static final String TITLE = "File editor";
    private JPanel mainPanel;
    private JTable table;
    private JScrollPane scrollPane;
    private static final int DEFAULT_WIDTH = 20;
    private static final int DEFAULT_HEIGHT = 30;
    private int height = DEFAULT_HEIGHT;
    private int width = DEFAULT_WIDTH;
    private FileEditorMenuBar menuBar;
    private FileEditorController controller;
    private JSplitPane splitPane;
    //private ArrayList<ArrayList<Object>> tableContent;
    private Object[][] tableContent;
    private String[] columns;
    private EditorTableModel tableModel;
    private FileEditorButtonPanel buttonPanel;

    private File file;
    public FileEditorFrame(File file){
        super();
        this.setTitle(TITLE);
        this.file=file;
        setFrameSize();
        initializeMainPanel();
        tableModel = new EditorTableModel();
        initializeTable();

        //initializeMenuBar();
        controller = new FileEditorController(tableModel,this);
        tableModel.setController(controller);
        buttonPanel=new FileEditorButtonPanel(controller,this,tableModel);
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scrollPane,buttonPanel);
        splitPane.setResizeWeight(0.9);
        addListener();
        mainPanel.add(splitPane,new GridBagConstraintsAdapter(1,1,1,1,1,1).setFill(GridBagConstraintsAdapter.BOTH));
        this.add(mainPanel);
    }

    public File getFile() {
        return file;
    }

    private void initializeMainPanel(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
    }
    private void setFrameSize(){
        super.setLocationByPlatform(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = dim.width, y = dim.height;
        super.setSize((4 * x) / 5, 4 * y / 5);
    }
    private void addListener(){
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(tableModel.isSaved())return;
                int confirm = JOptionPane.showOptionDialog(
                        null, "Do you want to save file before closing?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                /*if (confirm == 0) {
                    try{
                        //saveFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }*/
            }
        });
    }


    public void setTable(JTable table) {
        this.removeAll();
        this.revalidate();
        this.repaint();
        System.out.println(Arrays.toString(this.getComponents()));
        mainPanel.remove(splitPane);
        scrollPane = new JScrollPane(table);
        this.table = table;
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scrollPane,buttonPanel);
        splitPane.setResizeWeight(0.9);
        mainPanel.add(splitPane,new GridBagConstraintsAdapter(1,1,1,1,1,1).setFill(GridBagConstraintsAdapter.BOTH));
        //this.add(mainPanel);
        this.table=table;
    }

    public JTable getTable() {
        return table;
    }
    private void initializeTable(){
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //mainPanel.add(scrollPane,new GridBagConstraintsAdapter(1,1,1,1,1,1).setFill(GridBagConstraintsAdapter.BOTH));
    }

    //------------------------------------------------------------------------------------------------
    private void openFile() throws IOException {
        FileInputStream ExcelFileToRead = new FileInputStream(this.file);
        XSSFWorkbook  wb = new XSSFWorkbook(ExcelFileToRead);
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;

        Iterator rows = sheet.rowIterator();
        int i=0,j=0;
        while (i<height&&rows.hasNext())
        {
            row=(XSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (j<width&&cells.hasNext())
            {
                cell=(XSSFCell)cells.next();
                System.out.print(cell+" ");
                tableContent[i][j++]=cell.getStringCellValue();
            }
            i++;
            System.out.println();
        }
    }
    private void saveFile() throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        XSSFRow row = sheet.createRow(0);
        TableModel model = table.getModel();
        for (int i = 0; i < model.getColumnCount(); i++) {
            if(i<WIDTH&&i<HEIGHT)row.createCell(i).setCellValue(model.getColumnName(i+1));
        }
        for (int i = 0; i+1 < model.getRowCount(); i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j+1 < model.getColumnCount(); j++) {
                row.createCell(j).setCellValue(
                        model.getValueAt(i+1, j).toString()
                );
            }
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
    }




//todo:add message is file exits
}


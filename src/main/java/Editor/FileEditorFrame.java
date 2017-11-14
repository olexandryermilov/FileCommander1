package Editor;

import Adapters.FileSystemObject;
import Adapters.GridBagConstraintsAdapter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import groovy.util.Eval;



public class FileEditorFrame extends JFrame {
    private static final String TITLE = "File Editor";
    private JPanel mainPanel;
    private JTable table;
    private JScrollPane scrollPane;
    private static final int DEFAULT_WIDTH = 20;
    private static final int DEFAULT_HEIGHT = 30;
    private int height = DEFAULT_HEIGHT;
    private int width = DEFAULT_WIDTH;
    private FileEditorMenuBar menuBar;
    private void initializeMainPanel(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
    }
    private FileSystemObject file;
    //private ArrayList<ArrayList<Object>> tableContent;
    private Object[][] tableContent;
    private String[] columns;
    TableModel model;
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
    private String getColumnName(int number){
        StringBuilder sb = new StringBuilder();
        while(number>0){
            sb.append((char)(number%26+(int)'A'-((number<26)?1:0)));
            number/=26;
        }
        return new String(sb.reverse());
    }
    private void initializeTable(){
        columns = new String[width];
        for(int i=0;i<width;i++){
            columns[i] = getColumnName(i+1);
        }
        tableContent = new Object[height][width];
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                tableContent[i][j]=(Object)"";//tableContent.get(i).set(j,"");
            }
        }
        table = new JTable(tableContent,columns);
        model=table.getModel();
        scrollPane = new JScrollPane(table);
        //scrollPane.add(table);
        mainPanel.add(scrollPane,new GridBagConstraintsAdapter(1,1,1,1,1,1).setFill(GridBagConstraintsAdapter.BOTH));
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
                int confirm = JOptionPane.showOptionDialog(
                        null, "Do you want to save file before closing?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                    try{
                        saveFile();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
    public FileEditorFrame(FileSystemObject file){
        super();
        this.setTitle(TITLE);
        this.file=file;
        setFrameSize();
        initializeMainPanel();
        initializeTable();
        //initializeMenuBar();
        addListener();
        this.add(mainPanel);
    }


//todo:add message is file exits
}


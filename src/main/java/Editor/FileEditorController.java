package editor;

import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import groovy.util.Eval;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


public class FileEditorController {

    EditorTableModel tableModel;
    FileEditorFrame frame;
    private final String min = "def min(BigDecimal[] a){\n" +
            "    BigDecimal m = a[0];\n" +
            "    for(int i=1;i<a.length;i++){\n" +
            "        if(a[i]<m){\n" +
            "            m=a[i];\n" +
            "        }\n" +
            "    }\n" +
            "    return m;\n" +
            "}\n";
    private final String max = "def max(BigDecimal[] a){\n" +
            "    BigDecimal m = a[0];\n" +
            "    for(int i=1;i<a.length;i++){\n" +
            "        if(a[i]>m){\n" +
            "            m=a[i]\n" +
            "        }\n" +
            "    }\n" +
            "    return m;\n" +
            "}\n";

    public FileEditorController(EditorTableModel tableModel, FileEditorFrame frame){
        this.frame=frame;
        this.tableModel = tableModel;
    }
    public FileEditorController(EditorTableModel tableModel){
        this.tableModel = tableModel;
    }
    public BigDecimal calculateExpression(String exp) throws ArithmeticException{
        exp = exp.replaceAll("\\u005E","**");
        //exp = exp.replaceAll("(\\s)*div\\s*","intdiv(");
        StringBuilder values = new StringBuilder();
        for(Map.Entry<String,String> entry : tableModel.getCellsValues().entrySet()){
            try{
                Double d = Double.parseDouble(entry.getValue());
            }
            catch (Exception e){
                //throw e;
                continue;
            }
            values.append("def "+ entry.getKey()+" = "+entry.getValue().toString()+"\n");
        }
        return (BigDecimal)((Eval.me(min+max+new String(values) + "\n return 1.0*("+exp+")")));
    }
    public ExpressionConstraints checkExpressionType(String exp){
        if(exp.charAt(0)!='=')return ExpressionConstraints.NotAnExpression;
        exp=exp.substring(1);
        try{
            calculateExpression(exp);
            return ExpressionConstraints.BigDecimal;
        }
        catch (Exception e){
            try{
                if(e instanceof java.lang.ArithmeticException)return ExpressionConstraints.BigDecimal;
                calculateBooleanExpression(exp);
                return ExpressionConstraints.Boolean;
            }
            catch (Exception e2){
                return ExpressionConstraints.NotAnExpression;
            }
        }
        //return null;
    }
    public Boolean calculateBooleanExpression(String exp){
        exp = exp.replaceAll("\\u005E","**");
        exp = exp.replaceAll("(\\s)*XOR\\s*","^");
        exp = exp.replaceAll("(\\s)*AND\\s*","&&");
        exp = exp.replaceAll("(\\s)*NOT\\s*","!");
        exp = exp.replaceAll("(\\s)*OR\\s*","||");
        exp = exp.replaceAll("(\\s)*xor\\s*","^");
        exp = exp.replaceAll("(\\s)*and\\s*","&&");
        exp = exp.replaceAll("(\\s)*not\\s*","!");
        exp = exp.replaceAll("(\\s)*or\\s*","||");
        StringBuilder values = new StringBuilder();
        for(Map.Entry<String,String> entry : tableModel.getCellsValues().entrySet()){
            try{
                Double d = Double.parseDouble(entry.getValue());
            }
            catch (Exception e){
                try{
                    Boolean b = Boolean.parseBoolean(entry.getValue());
                }
                catch (Exception e1){
                    continue;
                }
                //throw e;
            }
            values.append("def "+ entry.getKey()+" = "+entry.getValue().toString()+"\n");
        }
        return (Boolean)((Eval.me(min+max+new String(values) + "\n return ("+exp+")")));
    }
    String getColumnName(int number){
        StringBuilder sb = new StringBuilder();
        while(number>0){
            sb.append((char)(number%26+(int)'A'-((number<26)?1:0)));
            number/=26;
        }
        return new String(sb.reverse());
    }
    public void addColumn(){
        String newColumnName = getColumnName(tableModel.getColumnCount()+1);
        tableModel.getColumnNames().add(newColumnName);
        //columnMap.put(newColumnName, columnCount);
        for (ArrayList<String> row : tableModel.getData()) {
            row.add(null);
        }

        tableModel.incColumnCount();
        tableModel.fireTableStructureChanged();
        tableModel.setSaved(false);
    }
    public void addRow(){
        ArrayList<String> dataRow = new ArrayList<>(Collections.nCopies(tableModel.getColumnCount(), null));
        String newRowName = Integer.toString(tableModel.getRowCount()+1);
        tableModel.getRowNames().add(newRowName);
        tableModel.getData().add(dataRow);
        tableModel.incRowCount();
        tableModel.fireTableRowsInserted(tableModel.getRowCount() - 1,tableModel.getRowCount() - 1);
        tableModel.setSaved(false);
    }

    private String tableToJson(){
        Gson gson = new Gson();
        gson.toJson(tableModel);
        String result="";
        return result;
    }
    public void saveTable(){
        File file = frame.getFile();
        String toWrite = tableToJson();
        OutputStream os;
        try{
            os = new FileOutputStream(file);
            for(int i=0;i<toWrite.length();i++){
                os.write((byte)(toWrite.charAt(i)));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,"Couldn't save to file: "+e.getCause().toString());
        }
    }

    public EditorTableModel parseJson(String jsonString) {
        return tableModel;
    }

    public EditorTableModel readTableModel() {
        File file = frame.getFile();
        String toParse;
        StringBuilder sb = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line=br.readLine())!=null){
                sb.append(line);
            }
        }  catch (IOException e) {
            JOptionPane.showMessageDialog(frame,"Couldn't read file: "+e.getCause().toString());
        }
        toParse=new String(sb);
        return parseJson(toParse);
    }
}

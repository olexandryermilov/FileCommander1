package editor;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import groovy.util.Eval;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileEditorController {
    private EditorTableModel tableModel;
    private FileEditorFrame frame;
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
    public String getValuesFromMapForExpression(String exp){
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("([A-Z])+[1-9][0-9]*");
        Matcher matcher = pattern.matcher(exp);
        HashMap<String, Boolean> used = new HashMap<>();
        while(matcher.find()){
            String id = matcher.group();
            if(used.containsKey(id))continue;
            String val;
            if(!tableModel.getCellsValues().containsKey(id))val="0";else
            val = tableModel.getCellsValues().get(id);
            try{
                Double d = Double.parseDouble(val);
            }
            catch (Exception e){
                try{
                    Boolean b;
                    if (val.equals("true")) b = true;
                    else  if(val.equals("false"))  b= false; else throw e;
                }
                catch (Exception e1){
                    continue;
                }
                //throw e;
            }
            used.put(id,true);
            result.append("def ").append(id).append(" = ").append(val).append("\n");
        }
        return new String(result);
    }
    public FileEditorController(FileEditorFrame frame){
        this.frame=frame;
    }
    public FileEditorController(EditorTableModel tableModel){
        this.tableModel = tableModel;
    }
    public BigDecimal calculateExpression(String exp) throws ArithmeticException{
        if(exp.startsWith("if")){
            String ifExp = exp.substring(3,exp.indexOf("then")-2);
            String trueExp = exp.substring(exp.indexOf("then")+4,exp.indexOf("else")-1);
            if(trueExp.endsWith(";"))trueExp=trueExp.substring(0,trueExp.length()-1);
            String falseExp = exp.substring(exp.indexOf("else")+4);
            if(falseExp.endsWith(";"))falseExp=falseExp.substring(0,trueExp.length());
            try{
                if(calculateBooleanExpression(ifExp)){
                    return calculateExpression(trueExp);
                }
                else{
                    return calculateExpression(falseExp);
                }
            }catch (Exception e){
               // JOptionPane.showMessageDialog(frame,"Wrong expression syntax");
            }
        }
        exp = exp.replaceAll("\\u005E","**");
        //exp = exp.replaceAll("(\\s)*div\\s*","intdiv(");
        StringBuilder values = new StringBuilder();
        /*for(Map.Entry<String,String> entry : tableModel.getCellsValues().entrySet()){
            try{
                Double d = Double.parseDouble(entry.getValue());
            }
            catch (Exception e){
                //throw e;
                continue;
            }
            values.append("def "+ entry.getKey()+" = "+((entry.getValue().startsWith("="))?entry.getValue().substring(1):entry.getValue())+"\n");
        }*/
        return (BigDecimal)((Eval.me(min+max+getValuesFromMapForExpression(exp) + "\n return 1.0*("+exp+")")));
    }
    public ExpressionConstraints checkExpressionType(String exp){
        if(exp.contains("System"))return ExpressionConstraints.NotAnExpression;
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
    public Boolean calculateBooleanExpression (String exp)throws ArithmeticException{

        if(exp.startsWith("if")){
            String ifExp = exp.substring(3,exp.indexOf("then")-2);
            String trueExp = exp.substring(exp.indexOf("then")+4,exp.indexOf("else")-1);
            if(trueExp.endsWith(";"))trueExp=trueExp.substring(0,trueExp.length()-1);
            String falseExp = exp.substring(exp.indexOf("else")+4);
            if(falseExp.endsWith(";"))falseExp=falseExp.substring(0,falseExp.length()-1);
            try{
                if(calculateBooleanExpression(ifExp)){
                    return calculateBooleanExpression(trueExp);
                }
                else{
                    return calculateBooleanExpression(falseExp);
                }
            }catch (Exception e){
                //JOptionPane.showMessageDialog(frame,"Wrong expression syntax");
                return null;
            }
        }
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
                    Boolean b;
                    if (entry.getValue().equals("true")) b = true;
                    else  if(entry.getValue().equals("false"))  b= false; else throw e;
                }
                catch (Exception e1){
                    continue;
                }
                //throw e;
            }
            values.append("def "+ entry.getKey()+" = "+entry.getValue().toString()+"\n");
        }
        Boolean res=null;
        try{
            res =  (Boolean)((Eval.me(min+max+getValuesFromMapForExpression(exp) + "\n return ("+exp+")")));
        }catch (ArithmeticException e){
            throw e;
        }
        return res;
    }


    private String getColumnName(int number){
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

    public String tableToJson(){
        Gson gson = new Gson();
        EditorTableToSave tableToSave = new EditorTableToSave(tableModel.getCellsValues(),tableModel.getCellsRawData(),
                tableModel.getRowCount(),tableModel.getColumnCount());
        StringBuilder result = new StringBuilder();
        result.append(gson.toJson(tableToSave,tableToSave.getClass()));
        return new String(result);
    }
    public void saveTableModel(){
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

    private String getId(int row, int column){
        String res = getColumnName(column)+""+(row+1);
        return res;
    }

    //int rowCount, int columnCount, ArrayList<ArrayList<String>> data,
    //boolean saved, Map<String, String> cellsValues, Map<String, String> cellsRawData,
    //ArrayList<String> columnNames, ArrayList<String> rowNames, FileEditorController controller
    public EditorTableModel parseJson(String jsonString) {
        Gson gson = new GsonBuilder().create();
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(jsonString).getAsJsonObject();
        Type mapType = new TypeToken<Map<String,String>>(){}.getType();
        Map<String,String> cellsValues = gson.fromJson(object.get("cellsValues"),mapType);
        Map<String,String> cellsRawData = gson.fromJson(object.get("cellsRawData"),mapType);
        int rowCount = gson.fromJson(object.get("rowCount"),Integer.class);
        int columnCount = gson.fromJson(object.get("columnCount"),Integer.class);
        boolean isSaved = true;
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ArrayList<String>columnNames = new ArrayList<>();
        ArrayList<String>rowNames = new ArrayList<>();
        for(int i=0;i<rowCount;i++){
            data.add(new ArrayList<>());
            rowNames.add(Integer.toString(i+1));
        }
        for(int i=0;i<columnCount;i++){
            columnNames.add(getColumnName(i));
        }
        for(int i=0;i<rowCount;i++){
            for(int j=0;j<columnCount;j++){
                data.get(i).add(null);
                String id = getId(i,j);
                if(cellsValues.containsKey(id))data.get(i).set(j,cellsValues.get(id));
            }
        }
        EditorTableModel newTableModel = new EditorTableModel(rowCount,columnCount,data,isSaved,cellsValues,
                cellsRawData,columnNames,rowNames);
        newTableModel.fireTableDataChanged();
        return newTableModel;
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

    public void setTableModel(EditorTableModel tableModel) {
        this.tableModel = tableModel;
    }

}

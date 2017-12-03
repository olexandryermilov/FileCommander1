package editor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.*;

public class EditorTableModel extends AbstractTableModel {
    private int rowCount, columnCount;
    private ArrayList<ArrayList<String>> data;
    private boolean saved;
    private Map<String,String> cellsValues;
    private Map<String,String> cellsRawData;
    private ArrayList<String> columnNames;
    private ArrayList<String> rowNames;

    private FileEditorController controller;

    public EditorTableModel(int rowCount, int columnCount, ArrayList<ArrayList<String>> data,
                            boolean saved, Map<String, String> cellsValues, Map<String, String> cellsRawData,
                            ArrayList<String> columnNames, ArrayList<String> rowNames){
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.data = data;
        this.saved = saved;
        this.cellsValues = cellsValues;
        this.cellsRawData = cellsRawData;
        this.columnNames = columnNames;
        this.rowNames = rowNames;
    }

    public EditorTableModel(){
        this.rowCount=10;
        this.columnCount=10;
        initializeTableModel();
    }

    private void initializeTableModel(){
        saved = false;
        data=  new ArrayList<>();
        cellsRawData= new HashMap<>();
        cellsValues = new HashMap<>();
        columnNames = new ArrayList<>();
        rowNames=new ArrayList<>();
        data = new ArrayList<>(rowCount);
        for (int i = 0; i < rowCount; i++) {
            data.add(new ArrayList<>(Collections.nCopies(columnCount, null)));
            rowNames.add(Integer.toString(i+1));
        }
        for(int i=0;i<columnCount;i++){
            columnNames.add(getNewColumnName(i));
        }
    }

    public void setController(FileEditorController controller) {
        this.controller = controller;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }
    String getNewColumnName(int number){
        StringBuilder sb = new StringBuilder();
        while(number>0){
            sb.append((char)(number%26+(int)'A'-((number<26)?1:0)));
            number/=26;
        }
        return new String(sb.reverse());
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 0;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return rowNames.get(row);
        } else {
            return data.get(row).get(column);
        }
    }

    public String getId(int row, int column){
        String res = getColumnName(column)+""+(row+1);
        return res;
    }
    @Override
    public void setValueAt(Object value, int row, int column) {
        String result=(String)value;
        String id = getId(row,column);
        cellsRawData.remove(id);
        cellsValues.remove(id);
        cellsRawData.put(id,result);
        Optional<ArrayList<String>>expressionCycle = controller.getCycle(result,id);
        if(expressionCycle.isPresent()){
            StringBuilder cycle = new StringBuilder();
            for(String s : expressionCycle.get()){
                cycle.append(s);
                cycle.append("->");
            }
            cycle.delete(cycle.length()-2,cycle.length());
            JOptionPane.showMessageDialog(null,"There is a cycle "+new String(cycle)+" in your formula, please remove it");
            return;
        }
        if(result==null||result.equals("")){
            result=" ";
            cellsRawData.remove(id);
            cellsValues.remove(id);
            data.get(row).set(column,result);
            fireTableCellUpdated(row, column);
            saved = false;
            recalculateAll(row,column);
            return;
        }

        ExpressionConstraints expType = controller.checkExpressionType((String)value);
        if(expType.equals(ExpressionConstraints.BigDecimal)){
            try{
                result =controller.calculateExpression(((String) value).substring(1)).toString();
            }
            catch(ArithmeticException e){
                result="Division by zero";
            }
        }
        else{
            if(expType.equals(ExpressionConstraints.Boolean)){
                try{
                    result=controller.calculateBooleanExpression(((String)value).substring(1)).toString();
                }
                catch(ArithmeticException e){
                    result="Division by zero";
                }
            }
        }
        if(value.equals("")){
            cellsValues.remove(id);
            cellsRawData.remove(id);
        }
        else {
            cellsRawData.put(id, (String) value);
            cellsValues.put(id, result);
        }
        data.get(row).set(column,result);
        fireTableCellUpdated(row, column);
        saved = false;
        recalculateAll(row,column);
    }

    public void recalculateAll(int row, int column){
        boolean hasChanges = true;
        ArrayList<String>changedCellsIDs = new ArrayList<>();
        changedCellsIDs.add(getId(row,column));
        while(hasChanges) {
            hasChanges=false;
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    if (i == row && column == j) continue;
                    String id = getId(i, j);
                    String exp = cellsRawData.get(id);
                    boolean toContinue=false;
                    for(String cellId:changedCellsIDs){
                        if(exp!=null&&exp.contains(cellId)){
                            toContinue=true;
                            break;
                        }
                    }
                    if (!toContinue||exp == null || exp.equals("")) continue;
                    String result;
                    ExpressionConstraints type = controller.checkExpressionType(exp);
                    if (type.equals(ExpressionConstraints.BigDecimal)) {
                        try {
                            result = String.valueOf(controller.calculateExpression(exp.substring(1)));
                        } catch (ArithmeticException e) {
                            result = "Division by zero";
                        }
                    } else {
                        if (type.equals(ExpressionConstraints.Boolean)) {
                            try {
                                result = String.valueOf(controller.calculateBooleanExpression(exp.substring(1)));
                            } catch (ArithmeticException e) {
                                result = "Division by zero";
                            }
                        } else {
                            result = exp;
                        }
                    }
                    if(!result.equals(data.get(i).get(j))) {
                        hasChanges = true;
                        data.get(i).set(j, result);
                        cellsValues.put(id, result);
                        fireTableCellUpdated(i, j);
                        saved = false;
                    }
                }
            }
        }
    }
    public ArrayList<ArrayList<String>> getData() {
        return data;
    }

    public Map<String, String> getCellsValues() {
        return cellsValues;
    }

    public Map<String, String> getCellsRawData() {
        return cellsRawData;
    }

    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public ArrayList<String> getRowNames() {
        return rowNames;
    }

    public FileEditorController getController() {
        return controller;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
    public void incColumnCount(){
        columnCount++;
    }
    public void incRowCount(){
        rowCount++;
    }

    //todo: check for cycles
    //
}

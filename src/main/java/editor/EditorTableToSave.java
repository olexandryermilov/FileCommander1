package editor;

import java.util.Map;

public class EditorTableToSave {
    //int rowCount, int columnCount
    //boolean saved, Map<String, String> cellsValues, Map<String, String> cellsRawData,
    //ArrayList<String> columnNames, ArrayList<String> rowNames, FileEditorController controller
    private Map<String,String> cellsValues,cellsRawData;
    private int rowCount,columnCount;
    public EditorTableToSave(Map<String, String> cellsValues, Map<String, String> cellsRawData, int rowCount, int columnCount) {
        this.cellsValues = cellsValues;
        this.cellsRawData = cellsRawData;
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }
    public Map<String, String> getCellsValues() {
        return cellsValues;
    }

    public Map<String, String> getCellsRawData() {
        return cellsRawData;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }
}

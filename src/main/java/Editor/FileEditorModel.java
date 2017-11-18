package editor;

import java.util.HashMap;
import java.util.Map;

public class FileEditorModel {
    private Map<String,Double> cellsValues;
    private Map<String,String> cellsRawData;

    public FileEditorModel() {
        this.cellsRawData = new HashMap<>();
        this.cellsValues = new HashMap<>();
    }

    public Map<String, Double> getCellsValues() {
        return cellsValues;
    }

    public Map<String, String> getCellsRawData() {
        return cellsRawData;
    }
}

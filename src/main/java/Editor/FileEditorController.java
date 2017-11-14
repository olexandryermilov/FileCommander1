package Editor;

import groovy.util.Eval;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FileEditorController {
    FileEditorModel model;
    private final String min = "def min(int a1,int a2){\n" +
            "        return (a1>a2)?a2:a1;\n" +
            "    }\n";
    private final String max = "def max(int a1,int a2){\n" +
            "        return (a1<a2)?a2:a1;\n" +
            "    }\n";

    public FileEditorController(FileEditorModel model){
        this.model=model;
    }
    public BigDecimal calculateExpression(String exp){
        StringBuilder values = new StringBuilder();
        for(Map.Entry<String,Double> entry : model.getCellsValues().entrySet()){
            values.append("def "+ entry.getKey()+" = "+entry.getValue().toString()+"\n");
        }
        return (BigDecimal)((Eval.me(min+max+new String(values) + "\n return 1.0*("+exp+")")));
    }
}

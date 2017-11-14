package Editor;

import groovy.util.Eval;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FileEditorController {
    FileEditorModel model;
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

    public FileEditorController(FileEditorModel model){
        this.model=model;
    }
    public BigDecimal calculateExpression(String exp) throws ArithmeticException{
        exp = exp.replaceAll("\\u005E","**");
        StringBuilder values = new StringBuilder();
        for(Map.Entry<String,Double> entry : model.getCellsValues().entrySet()){
            values.append("def "+ entry.getKey()+" = "+entry.getValue().toString()+"\n");
        }
        return (BigDecimal)((Eval.me(min+max+new String(values) + "\n return 1.0*("+exp+")")));
    }
}

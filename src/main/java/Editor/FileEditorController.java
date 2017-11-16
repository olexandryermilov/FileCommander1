package Editor;

import groovy.util.Eval;

import java.math.BigDecimal;
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
        //exp = exp.replaceAll("(\\s)*div\\s*","intdiv(");
        StringBuilder values = new StringBuilder();
        for(Map.Entry<String,Double> entry : model.getCellsValues().entrySet()){
            values.append("def "+ entry.getKey()+" = "+entry.getValue().toString()+"\n");
        }
        return (BigDecimal)((Eval.me(min+max+new String(values) + "\n return 1.0*("+exp+")")));
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
        for(Map.Entry<String,Double> entry : model.getCellsValues().entrySet()){
            values.append("def "+ entry.getKey()+" = "+entry.getValue().toString()+"\n");
        }
        return (Boolean)((Eval.me(min+max+new String(values) + "\n return ("+exp+")")));
    }
}

import Editor.FileEditorController;
import Editor.FileEditorModel;
import com.sun.jna.platform.win32.OaIdl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
public class FileEditorCalculationTest {
    private FileEditorController controller;
    private FileEditorModel model;
    @Before
    public void prepareController(){
        model = new FileEditorModel();
        controller = new FileEditorController(model);
    }
    private boolean compareBigDecimal(BigDecimal a, BigDecimal b){
        BigDecimal sub = a.subtract(b);
        if(sub.compareTo(BigDecimal.ZERO)<0){
            sub=sub.multiply(new BigDecimal(-1));
        }
        final BigDecimal EPS = new BigDecimal(0.001);
        return sub.compareTo(EPS)<0;
    }
    @Test
    public void calculatesSimpleExpression(){
        final String EXPRESSION = "1+2";
        final BigDecimal RIGHT_ANSWER=new BigDecimal(3.0);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(RIGHT_ANSWER,answer));
    }
    @Test
    public void calculateMin(){
        final String EXPRESSION = "min(1,2)";
        final BigDecimal RIGHT_ANSWER = new BigDecimal(1.0);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(RIGHT_ANSWER,answer));
    }
    @Test
    public void calculateMax(){
        final String EXPRESSION = "max(1,2)*3";
        final BigDecimal RIGHT_ANSWER = new BigDecimal(6.0);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(RIGHT_ANSWER,answer));
    }
    @Test
    public void canGetValuesFromCells(){
        model.getCellsValues().put("A1",3.0);
        model.getCellsValues().put("B2",9.0);
        final String EXPRESSION = "A1*B2";
        final BigDecimal RIGHT_ANSWER = new BigDecimal(27.0);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(answer,RIGHT_ANSWER));
    }
    @Test
    public void understandsUnaryPlus(){
        final String EXPRESSION = "+5";
        final BigDecimal RIGHT_ANSWER = new BigDecimal(5.0);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(answer,RIGHT_ANSWER));
    }
    @Test
    public void understandsUnaryMinus(){
        final String EXPRESSION = "-5";
        final BigDecimal RIGHT_ANSWER = new BigDecimal("-5.0");
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(answer,RIGHT_ANSWER));
    }
    @Test
    public void calculatesBigExpressionsWithAllOperations(){
        model.getCellsValues().put("A1",3.0);
        model.getCellsValues().put("B2",6.0);
        final String EXPRESSION = "-5*min(31,-12)+1/3+8.0/4-max(16,3)+A1/B2";
        final BigDecimal RIGHT_ANSWER = new BigDecimal( 46.8333333333);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        System.out.println(answer);
        assertTrue(compareBigDecimal(answer,RIGHT_ANSWER));
    }
}

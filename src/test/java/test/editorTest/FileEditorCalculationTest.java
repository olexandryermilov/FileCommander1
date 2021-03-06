package test.editorTest;

import editor.tableditor.EditorTableModel;
import editor.tableditor.FileEditorController;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

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
    private EditorTableModel model;
    @Before
    public void prepareController(){
        model = new EditorTableModel();
        controller = new FileEditorController(model);
        model.setController(controller);
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
        model.getCellsValues().put("A1","3.0");
        model.getCellsValues().put("B2","9.0");
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
    public void treatsIntegersRight(){
        final String EXPRESSION = "1/3";
        final BigDecimal RIGHT_ANSWER = new BigDecimal("0.33333");
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(answer,RIGHT_ANSWER));
    }
    @Test(expected = java.lang.ArithmeticException.class)
    public void treatsDivisionByZero(){
        final String EXPRESSION = "1/0";
        controller.calculateExpression(EXPRESSION);
    }
    @Test
    public void calculatesPows(){
        final String EXPRESSION = "3^2";
        BigDecimal RIGHT_ANSWER = new BigDecimal(9);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(RIGHT_ANSWER,answer));
    }
    @Test
    public void findMinimumFromArray(){
        final String EXPRESSION = "min(3,2,7,18)";
        BigDecimal RIGHT_ANSWER = new BigDecimal(2);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(RIGHT_ANSWER,answer));
    }
    @Test
    public void findMaximumFromArray(){
        final String EXPRESSION = "max(3,2,7,18)";
        BigDecimal RIGHT_ANSWER = new BigDecimal(18);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(RIGHT_ANSWER,answer));
    }

    @Test
    public void calculatesBigExpressionsWithAllOperations(){
        model.getCellsValues().put("A1","3.0");
        model.getCellsValues().put("B2","6.0");
        final String EXPRESSION = "-5*min(31.0,31.0,31.0,-18.0)+1/3+8.0/4-max(16,3)+A1/B2+3^A1";
        final BigDecimal RIGHT_ANSWER = new BigDecimal( 103.8333333333);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(answer,RIGHT_ANSWER));
    }
    @Test
    public void calculatesBigExpressionsWithAllOperationsAndBrackets(){
        final String EXPRESSION = "5 * (-3)*(-2)+min(max(6, 9), -5)";
        final BigDecimal RIGHT_ANSWER = new BigDecimal(25.0);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(answer,RIGHT_ANSWER));
    }
    @Test(expected = org.codehaus.groovy.control.MultipleCompilationErrorsException.class)
    public void showsErrorWhenCalculatingNotImplementedOperations(){
        final String EXPRESSION="5 div 3";
        controller.calculateExpression(EXPRESSION);
    }
    @Test(expected = groovy.lang.MissingMethodException.class)
    public void showsErrorWhenCalculatingString(){
        final String EXPRESSION = "String";
        controller.calculateExpression(EXPRESSION);
    }
    @Test
    public void calculatesExpressionWhenCellIsEmpty(){
        final String EXPRESSION = "(5 * (-3)*(-2)+min(max(6, 9), -5))*A1";
        final BigDecimal RIGHT_ANSWER = new BigDecimal(0.0);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(answer,RIGHT_ANSWER));
    }
    @Test
    public void ifTest(){
        model.getCellsValues().put("C1","5");
        final String EXPRESSION = "if(C1==5)then 3; else 7;";
        final BigDecimal RIGHT_ANSWER = new BigDecimal(3.0);
        BigDecimal answer = controller.calculateExpression(EXPRESSION);
        assertTrue(compareBigDecimal(answer,RIGHT_ANSWER));
    }
}

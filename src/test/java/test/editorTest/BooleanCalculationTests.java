package test.editorTest;

import editor.EditorTableModel;
import editor.FileEditorController;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class BooleanCalculationTests {
    private FileEditorController controller;
    private EditorTableModel model;
    @Before
    public void prepareController(){
        model = new EditorTableModel();
        controller = new FileEditorController(model);
        model.setController(controller);
    }
    @Test
    public void calculateSimpleExpression(){
        final String EXPRESSION = "2<3";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void calculateAnd_FalseAndTrue(){
        final String EXPRESSION = "(3<1)AND(2>1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void calculateOr_FalseAndTrue(){
        final String EXPRESSION = "(3<1)OR(2>1)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void calculateXor_FalseAndTrue(){
        final String EXPRESSION = "(3<1)XOR(2>1)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void calculateNot_False(){
        final String EXPRESSION = "NOT(3<1)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void calculateAnd_TrueAndTrue(){
        final String EXPRESSION = "(3>1)AND(2>1)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void calculateOr_TrueAndTrue(){
        final String EXPRESSION = "(3>1)OR(2>1)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void calculateXor_TrueAndTrue(){
        final String EXPRESSION = "(3>1)XOR(2>1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void calculateNot_True(){
        final String EXPRESSION = "NOT(3>1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void calculateAnd_FalseAndFalse(){
        final String EXPRESSION = "(3<1)AND(2<1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void calculateOr_FalseAndFalse(){
        final String EXPRESSION = "(3<1)OR(2<1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void calculateXor_FalseAndFalse(){
        final String EXPRESSION = "(3<1)XOR(2<1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void worksWithExpressions_SimpleExpressions(){
        final String EXPRESSION = "NOT((3+7)<(10-9))";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void worksWithExpressions_HardExpression(){
        model.getCellsValues().put("A1","3.0");
        model.getCellsValues().put("B2","6.0");
        final String EXPRESSION = "((-5*min(31.0,31.0,31.0,-18.0)+1/3+8.0/4-max(16,3)+A1/B2+3^A1)<110)AND(A1^B2>10)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test(expected = org.codehaus.groovy.control.MultipleCompilationErrorsException.class)
    public void showsErrorWhenCalculatingSomeNonsense(){
        final String EXPRESSION="5 XAND 3";
        controller.calculateBooleanExpression(EXPRESSION);
    }
    @Test
    public void understandsLowercase(){
        final String EXPRESSION = "(1 and (0 xor 1))or(not 1)";
        final boolean RIGHT_ANSWER = true;
        boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void simpleLinkTest(){
        model.getCellsValues().put("C1","true");
        final String EXPRESSION = "!C1";
        final boolean RIGHT_ANSWER = false;
        boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
}

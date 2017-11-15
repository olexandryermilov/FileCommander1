package EditorTest;

import Editor.FileEditorController;
import Editor.FileEditorModel;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
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
    private FileEditorModel model;
    @Before
    public void prepareController(){
        model = new FileEditorModel();
        controller = new FileEditorController(model);
    }
    @Test
    public void calculateSimpleExpression(){
        final String EXPRESSION = "2<3";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void calculateAnd_FalseAndTrue(){
        final String EXPRESSION = "(3<1)AND(2>1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void calculateOr_FalseAndTrue(){
        final String EXPRESSION = "(3<1)OR(2>1)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void calculateXor_FalseAndTrue(){
        final String EXPRESSION = "(3<1)XOR(2>1)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void calculateNot_False(){
        final String EXPRESSION = "NOT(3<1)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void calculateAnd_TrueAndTrue(){
        final String EXPRESSION = "(3>1)AND(2>1)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void calculateOr_TrueAndTrue(){
        final String EXPRESSION = "(3>1)OR(2>1)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void calculateXor_TrueAndTrue(){
        final String EXPRESSION = "(3>1)XOR(2>1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void calculateNot_True(){
        final String EXPRESSION = "NOT(3>1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void calculateAnd_FalseAndFalse(){
        final String EXPRESSION = "(3<1)AND(2<1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void calculateOr_FalseAndFalse(){
        final String EXPRESSION = "(3<1)OR(2<1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void calculateXor_FalseAndFalse(){
        final String EXPRESSION = "(3<1)XOR(2<1)";
        final Boolean RIGHT_ANSWER = false;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void worksWithExpressions_SimpleExpressions(){
        final String EXPRESSION = "NOT((3+7)<(10-9))";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
    @Test
    public void worksWithExpressions_HardExpression(){
        model.getCellsValues().put("A1",3.0);
        model.getCellsValues().put("B2",6.0);
        final String EXPRESSION = "((-5*min(31.0,31.0,31.0,-18.0)+1/3+8.0/4-max(16,3)+A1/B2+3^A1)<110)AND(A1^B2>10)";
        final Boolean RIGHT_ANSWER = true;
        Boolean answer = controller.calculateBooleanExpression(EXPRESSION);
        assertEquals(answer,RIGHT_ANSWER);
    }
}

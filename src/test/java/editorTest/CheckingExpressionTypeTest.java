package editorTest;

import editor.EditorTableModel;
import editor.ExpressionConstraints;
import editor.FileEditorController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CheckingExpressionTypeTest {
    private FileEditorController controller;
    private EditorTableModel model;
    @Before
    public void prepareController(){
        model = new EditorTableModel();
        controller = new FileEditorController(model);
        model.setController(controller);
    }
    //ExpressionConstraints.ExpressionConstraints block
    @Test
    public void understandsSimpleExpression(){
        final String EXPRESSION = "=1+2";
        final ExpressionConstraints RIGHT_ANSWER=ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsMin(){
        final String EXPRESSION = "=min(1,2)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsMax(){
        final String EXPRESSION = "=max(1,2)*3";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsValuesFromCells(){
        model.getCellsValues().put("A1","3.0");
        model.getCellsValues().put("B2","9.0");
        final String EXPRESSION = "=A1*B2";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsUnaryPlus(){
        final String EXPRESSION = "=+5";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsUnaryMinus(){
        final String EXPRESSION = "=-5";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsIntegersRight(){
        final String EXPRESSION = "=1/3";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsDivisionByZero(){
        final String EXPRESSION = "=1/0";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsPows(){
        final String EXPRESSION = "=3^2";
        ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsMinimumFromArray(){
        final String EXPRESSION = "=min(3,2,7,18)";
        ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsMaximumFromArray(){
        final String EXPRESSION = "=max(3,2,7,18)";
        ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }

    @Test
    public void understandsBigExpressionsWithAllOperations(){
        model.getCellsValues().put("A1","3.0");
        model.getCellsValues().put("B2","6.0");
        final String EXPRESSION = "=-5*min(31.0,31.0,31.0,-18.0)+1/3+8.0/4-max(16,3)+A1/B2+3^A1";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsBigExpressionsWithAllOperationsAndBrackets(){
        final String EXPRESSION = "=5 * (-3)*(-2)+min(max(6, 9), -5)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.BigDecimal;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    
    //ExpressionConstraints.Boolean block
    @Test
    public void understandsSimpleBooleanExpression(){
        final String EXPRESSION = "=2<3";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsAndFalseAndTrue(){
        final String EXPRESSION = "=(3<1)AND(2>1)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsOr_FalseAndTrue(){
        final String EXPRESSION = "=(3<1)OR(2>1)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsXor_FalseAndTrue(){
        final String EXPRESSION = "=(3<1)XOR(2>1)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsNot_False(){
        final String EXPRESSION = "=NOT(3<1)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsAnd_TrueAndTrue(){
        final String EXPRESSION = "=(3>1)AND(2>1)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsOr_TrueAndTrue(){
        final String EXPRESSION = "=(3>1)OR(2>1)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsXor_TrueAndTrue(){
        final String EXPRESSION = "=(3>1)XOR(2>1)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsNot_True(){
        final String EXPRESSION = "=NOT(3>1)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsAnd_FalseAndFalse(){
        final String EXPRESSION = "=(3<1)AND(2<1)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsOr_FalseAndFalse(){
        final String EXPRESSION = "=(3<1)OR(2<1)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsXor_FalseAndFalse(){
        final String EXPRESSION = "=(3<1)XOR(2<1)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsExpressions_SimpleExpressions(){
        final String EXPRESSION = "=NOT((3+7)<(10-9))";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsExpressions_HardExpression(){
        model.getCellsValues().put("A1","3.0");
        model.getCellsValues().put("B2","6.0");
        final String EXPRESSION = "=((-5*min(31.0,31.0,31.0,-18.0)+1/3+8.0/4-max(16,3)+A1/B2+3^A1)<110)AND(A1^B2>10)";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }

    @Test
    public void understandsLowercase(){
        final String EXPRESSION = "=(1 and (0 xor 1)or(not 1))";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.Boolean;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    //ExpressionConstraints.NotAnExpression block
    @Test
    public void understandsSomeNonsense(){
        final String EXPRESSION="5 XAND 3";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.NotAnExpression;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void understandsText(){
        final String EXPRESSION = "=String";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.NotAnExpression;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void showsErrorWhenCalculatingNotImplementedOperations(){
        final String EXPRESSION="5 div 3";
        final ExpressionConstraints RIGHT_ANSWER = ExpressionConstraints.NotAnExpression;
        ExpressionConstraints answer = controller.checkExpressionType(EXPRESSION);
        assertEquals(RIGHT_ANSWER,answer);
    }
}

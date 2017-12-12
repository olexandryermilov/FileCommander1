package test.xmlTest;

import editor.xmleditor.XSLConverter;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

public class TransformerTest {
    @Test
    public void transformXML(){
        XSLConverter.convert(new File("facultyDatabase.xml"));
        assertThat(Arrays.asList(new File(".").list()),hasItem("facultyDatabase.html"));
    }
}

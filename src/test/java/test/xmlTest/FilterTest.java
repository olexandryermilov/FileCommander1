package test.xmlTest;

import editor.xmleditor.*;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class FilterTest {
    @Test
    public void testDomFilter(){
        Software filter = new Software("Windows",null,null,null,null, License.EDUCATIONAL);
        Software windows = new Software("Windows","Operation system for students and educators","10.0","OS","Microsoft",License.EDUCATIONAL);
        assertEquals(DOMParser.getDOMXML(new File("facultyDatabase.xml"),filter),"List of software for filter:\n"+windows.toString());
    }
    @Test
    public void testMarshallingFilter(){
        Software filter = new Software("Windows",null,null,null,null, License.EDUCATIONAL);
        Software windows = new Software("Windows","Operation system for students and educators","10.0","OS","Microsoft",License.EDUCATIONAL);
        FacultySoftwareDatabase fsd = new FacultySoftwareDatabase();
        fsd.setSoftwares(new ArrayList<>());
        fsd.getSoftwares().add(windows);
        try{
            assertEquals(Unmarshalling.unmarshall(new File("facultyDatabase.xml"),filter).toString(),fsd.toString());
        }
        catch (Exception e){

        }
    }
    @Test
    public void testSaxFilter(){
        Software filter = new Software("Windo",null,null,null,null, License.EDUCATIONAL);
        Software windows = new Software("Windows","Operation system for students and educators","10.0","OS","Microsoft",License.EDUCATIONAL);
        try {
            FacultySoftwareDatabase fsd = new FacultySoftwareDatabase();
            fsd.setSoftwares(new ArrayList<>());
            fsd.getSoftwares().add(windows);
            assertEquals(fsd.toString(),StaxParser.readXML(new File("facultyDatabase.xml"),filter).toString());
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testFilter_type(){
        Software filter = new Software(null,null,null,"IDE",null,null);
        try {
            FacultySoftwareDatabase fsd = StaxParser.readXML(new File("facultyDatabase.xml"),filter);
            assertEquals(fsd.getSoftwares().size(),3);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testFilter_author(){
        Software filter = new Software(null,null,null,null,"JetBrains",null);
        try {
            FacultySoftwareDatabase fsd = StaxParser.readXML(new File("facultyDatabase.xml"),filter);
            assertEquals(fsd.getSoftwares().size(),2);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}

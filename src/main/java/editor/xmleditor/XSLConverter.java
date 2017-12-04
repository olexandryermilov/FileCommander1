package editor.xmleditor;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;

public class XSLConverter {
    public static void convert(File xmlfile){
        try {
            File stylesheet = new File("FacultySoftwareDatabase.xsl");
            StreamSource stylesource = new StreamSource(stylesheet);
            StreamSource xmlsource = new StreamSource(xmlfile);
            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer(stylesource);
            // Transform the document and store it in a file
            transformer.transform(xmlsource, new StreamResult(
                    new File(
                            xmlfile.getParent()+"\\"+xmlfile.getName().replaceAll("xml","html")
                    )
            ));

            //StreamResult consoleOut = new StreamResult(System.out);
            //transformer.transform(xmlsource, consoleOut);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}

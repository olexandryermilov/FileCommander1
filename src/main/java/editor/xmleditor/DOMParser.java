package editor.xmleditor;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;

public class DOMParser {
    public static String getDOMXML(File file, Software filter){
        StringBuilder sb = new StringBuilder();
        try {
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("facultySoftwareDatabase.xsd"));
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setSchema(schema);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setErrorHandler(new XMLErrorHandler());
            Document document =documentBuilder.parse(file);
            Node root = document.getDocumentElement();
            sb.append("List of software for filter:\n");
            NodeList softwares = root.getChildNodes();
            for(int i=0;i<softwares.getLength();i++){
                Software entity = new Software();
                Node software = softwares.item(i);
                if(software.getNodeType()!=Node.TEXT_NODE){
                    NodeList attrs = software.getChildNodes();
                    NamedNodeMap nodeMap = software.getAttributes();
                    entity.setName(nodeMap.getNamedItem("name").getTextContent());
                    entity.setDescription(nodeMap.getNamedItem("description").getTextContent());
                    entity.setVersion(nodeMap.getNamedItem("version").getTextContent());
                    for(int j=0;j<attrs.getLength();j++){
                        Node attribute = attrs.item(j);
                        if(attribute.getNodeType()!=Node.TEXT_NODE){
                            switch (attribute.getNodeName()){
                                case "type" : entity.setType(attribute.getChildNodes().item(0).getTextContent());break;
                                case "author": entity.setAuthor(attribute.getChildNodes().item(0).getTextContent());break;
                                case "license": entity.setLicense(attribute.getChildNodes().item(0).getTextContent());break;
                            }
                        }
                    }
                    if(Filter.goodForFilter(entity,filter))sb.append(entity.toString());
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}

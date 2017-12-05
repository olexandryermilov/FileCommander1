package editor.xmleditor;

import org.w3c.dom.Attr;
import org.w3c.dom.events.Event;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class StaxParser {
    public static FacultySoftwareDatabase readXML(File file,Software filter) throws XMLStreamException {
        FacultySoftwareDatabase fsd = new FacultySoftwareDatabase();
        fsd.setSoftwares(new ArrayList<>());
        XMLInputFactory f = XMLInputFactory.newInstance();
        XMLEventReader r = null;
        try {
            r = f.createXMLEventReader(new FileInputStream(file));
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Software entity = null;
        try {
            while (r.hasNext()) {
                XMLEvent event = r.nextEvent();

                if (event.isStartElement()) {
                    StartElement start = event.asStartElement();
                    if(start.getEventType()== XMLEvent.START_ELEMENT){
                        if(start.getName().toString().equals("software")){
                            entity = new Software();
                            Iterator attrs = start.getAttributes();
                            while(attrs.hasNext()){
                                Attribute attr = (Attribute)attrs.next();
                                if(attr.getName().toString().equals("name")){
                                    entity.setName(attr.getValue());
                                }
                                if(attr.getName().toString().equals("description")){
                                    entity.setDescription(attr.getValue());
                                }
                                if(attr.getName().toString().equals("version")){
                                    entity.setVersion(attr.getValue());
                                }
                            }
                        }
                        if(start.getName().toString().equals("type")){
                            entity.setType(r.nextEvent().asCharacters().getData());
                        }
                        if(start.getName().toString().equals("author")){
                            entity.setAuthor(r.nextEvent().asCharacters().getData());
                        }
                        if(start.getName().toString().equals("license")){
                            entity.setLicense(r.nextEvent().asCharacters().getData());
                        }
                    }

                }
                if(event.isEndElement()){
                    EndElement element = event.asEndElement();
                    if(element.getName().toString().equals("software")){
                        if(Filter.goodForFilter(entity,filter)){
                            fsd.getSoftwares().add(entity);
                        }
                    }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } finally {
            assert r != null;
            r.close();
        }
        return fsd;
    }
}

package editor.xmleditor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Unmarshalling {
    public static FacultySoftwareDatabase unmarshall(File file, Software filterSoftware) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(FacultySoftwareDatabase.class);
        return (FacultySoftwareDatabase) context.createUnmarshaller()
                .unmarshal(new FileReader(file));
    }
}

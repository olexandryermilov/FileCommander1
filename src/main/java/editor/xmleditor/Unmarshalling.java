package editor.xmleditor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Unmarshalling {
    public static FacultySoftwareDatabase unmarshall(File file, Software filterSoftware) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(FacultySoftwareDatabase.class);
        FacultySoftwareDatabase fsd = (FacultySoftwareDatabase) context.createUnmarshaller()
                .unmarshal(new FileReader(file));
        ArrayList<Software> toRemove = new ArrayList<>();
        for(int i=0;i<fsd.getSoftwares().size();i++){
            if(!Filter.goodForFilter(fsd.getSoftwares().get(i),filterSoftware)){
                toRemove.add(fsd.getSoftwares().get(i));
            }
        }
        for(int i=0;i<toRemove.size();i++){
            fsd.getSoftwares().remove(toRemove.get(i));
        }
        return fsd;
    }
}

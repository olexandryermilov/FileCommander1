package editor.xmleditor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="facultySoftwareDatabase")
public class FacultySoftwareDatabase {
    public List<Software> getSoftwares() {
        return softwares;
    }

    @XmlElement(name = "software")
    public void setSoftwares(List<Software> softwares) {
        this.softwares = softwares;
    }

    @Override
    public String toString() {
        return "FacultySoftwareDatabase{" +
                "softwares=" + softwares.toString() +
                '}';
    }

    private List<Software> softwares;

}

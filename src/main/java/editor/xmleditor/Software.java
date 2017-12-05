package editor.xmleditor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement (name = "software")
//@XmlType(propOrder = {"name","description","version","type","author","license"})
public class Software {
    private String name,description,version,type,author;
    private License license;
    public Software(){
        name=null;
        description=null;
        version=null;
        type=null;
        author=null;
        license=null;
    }
    public Software(String name, String description, String version, String type, String author, License license) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.type = type;
        this.author = author;
        this.license = license;
    }
    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }
    @XmlAttribute
    public void setDescription(String description) {
        this.description = description;
    }
    @XmlAttribute
    public void setVersion(String version) {
        this.version = version;
    }
    @XmlElement(name = "type")
    public void setType(String type) {
        this.type = type;
    }
    @XmlElement(name = "author")
    public void setAuthor(String author) {
        this.author = author;
    }
    @XmlJavaTypeAdapter(LicenseAdapter.class)
    public void setLicense(License license) {
        this.license = license;
    }

    @Override
    public String toString() {
        return "Software{\n" +
                "   name='" + name + '\'' +
                ",\n    description='" + description + '\'' +
                ",\n    version='" + version + '\'' +
                ",\n    type='" + type + '\'' +
                ",\n    author='" + author + '\'' +
                ",\n    license=" + license +
                "\n}";
    }
    public void setLicense(String license){
        if(license.toLowerCase().equals("paid"))this.license=License.PAID;
        if(license.toLowerCase().equals("educational"))this.license=License.EDUCATIONAL;
        if(license.toLowerCase().equals("free"))this.license=License.FREE;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getType() {
        return type;
    }

    public String getAuthor() {
        return author;
    }

    public License getLicense() {
        return license;
    }
}

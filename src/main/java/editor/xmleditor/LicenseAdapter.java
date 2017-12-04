package editor.xmleditor;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LicenseAdapter extends XmlAdapter<String, License> {

    @Override
    public License unmarshal(String v) throws Exception {
        if(v.equals("paid")){
            return License.PAID;
        }
        if(v.equals("free")){
            return  License.FREE;
        }
        if(v.equals("educational")){
            return License.EDUCATIONAL;
        }
        return null;
    }

    @Override
    public String marshal(License v) throws Exception {
        return v.toString();
    }
}
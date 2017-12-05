package editor.xmleditor;

public class Filter {
    public static boolean goodForFilter(Software toCheck, Software filter){
        if(!(filter.getName()==null||filter.getName().equals(toCheck.getName())))return false;
        if(!(filter.getType()==null||filter.getType().equals(toCheck.getType())))return false;
        if(!(filter.getAuthor()==null||filter.getAuthor().equals(toCheck.getAuthor())))return false;
        if(!(filter.getDescription()==null||filter.getDescription().equals(toCheck.getDescription())))return false;
        if(!(filter.getVersion()==null||filter.getVersion().equals(toCheck.getVersion())))return false;
        if(!(filter.getLicense()==null||filter.getLicense().equals(toCheck.getLicense())))return false;
        return true;
    }
}

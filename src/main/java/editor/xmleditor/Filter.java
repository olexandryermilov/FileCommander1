package editor.xmleditor;

public class Filter {
    public static boolean goodForFilter(Software toCheck, Software filter){
        if(!(filter.getName()==null||toCheck.getName().toLowerCase().contains(filter.getName().toLowerCase())))return false;
        if(!(filter.getType()==null||toCheck.getType().toLowerCase().contains(filter.getType().toLowerCase())))return false;
        if(!(filter.getAuthor()==null||toCheck.getAuthor().toLowerCase().contains(filter.getAuthor().toLowerCase())))return false;
        if(!(filter.getDescription()==null||toCheck.getDescription().toLowerCase().contains(filter.getDescription().toLowerCase())))return false;
        if(!(filter.getVersion()==null||toCheck.getVersion().toLowerCase().contains(filter.getVersion().toLowerCase())))return false;
        if(!(filter.getLicense()==null||filter.getLicense().equals(toCheck.getLicense())))return false;
        return true;
    }
}

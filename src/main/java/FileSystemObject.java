import java.io.File;

public class FileSystemObject extends File {
    public FileSystemObject(String path){
        super(path);

    }
    private String absolutePath;
    public boolean isRoot(){
        File[] roots = File.listRoots();
        for(File file:roots){
            if(file.toString().equals(this.toString()))return true;
        }
        return false;
    }

}

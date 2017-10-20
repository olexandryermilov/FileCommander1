package Adapters;

import java.io.File;

public class FileSystemObject extends File {
    public FileSystemObject(String path){
        super(path);
    }
    public boolean isRoot(){
        File[] roots = File.listRoots();
        for(File file:roots){
            if(file.toString().equals(this.toString()))return true;
        }
        return false;
    }
    public String getExtension(){
        String name = this.getName();
        int indexOfDot = -1;
        for(int i = name.length()-1;i>=0;i--){
            if(name.charAt(i)=='.'){
                indexOfDot = i;
                break;
            }
        }
        if(indexOfDot!=-1){
            String extension = name.substring(indexOfDot);
            return extension;
        }
        else{
            return "-dir";
        }
    }
}

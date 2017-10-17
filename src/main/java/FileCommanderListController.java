import java.io.File;

public class FileCommanderListController {
    private FileCommanderListModel fileCommanderListModel;
    private FileCommanderFrame frame;
    FileCommanderListController(FileCommanderListModel fileCommanderListModel, FileCommanderFrame frame){
        this.fileCommanderListModel = fileCommanderListModel;
        this.frame=frame;
    }
    public void addRootsToListModel(){
        File[] roots = File.listRoots();
        for(File file: roots){
            if(file.isHidden())fileCommanderListModel.getListModel().addElement(file.toString());
        }
    }
    public void handleDoubleClick(String path){
        FileSystemObject file = new FileSystemObject(path);
        if(path.equals("..")){
            uploadParent();

        }else
        if(file.isDirectory()){
            uploadChildren(file);
        }
        else
        {
            //
        }
    }
    void uploadParent(){
        FileSystemObject selectedDirectoryFile = new FileSystemObject(fileCommanderListModel.getSelectedDirectory());
        if(selectedDirectoryFile.isRoot()){
            fileCommanderListModel.getListModel().clear();
            addRootsToListModel();
            return;
        }
        FileSystemObject parent = new FileSystemObject(new FileSystemObject(fileCommanderListModel.getSelectedDirectory()).getParent());
        fileCommanderListModel.getListModel().clear();
        fileCommanderListModel.getListModel().addElement("..");
        fileCommanderListModel.setSelectedDirectory(parent.toString());
        for(File file: parent.listFiles()){
            if(!file.isHidden())fileCommanderListModel.getListModel().addElement(file.toString());
        }
    }

    void refreshList(){
        FileSystemObject selectedDirectoryFile = new FileSystemObject(fileCommanderListModel.getSelectedDirectory());
        if(!fileCommanderListModel.getSelectedDirectory().equals("")){
            fileCommanderListModel.getListModel().clear();
            fileCommanderListModel.getListModel().addElement("..");
            for(File file: selectedDirectoryFile.listFiles()){
                if(!file.isHidden())fileCommanderListModel.getListModel().addElement(file.toString());
            }
        }
    }
    void uploadChildren(FileSystemObject fileDir){
        fileCommanderListModel.setSelectedDirectory(fileDir.toString());
        refreshList();
    }
}

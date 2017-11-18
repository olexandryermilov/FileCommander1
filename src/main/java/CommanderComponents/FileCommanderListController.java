package commanderComponents;

import adapters.FileSystemObject;

import java.io.File;


class FileCommanderListController {
    private FileCommanderListModel fileCommanderListModel;
    private FileCommanderListPanel listPanel;
    private FileCommanderFrame frame;
    FileCommanderListController(FileCommanderListModel fileCommanderListModel, FileCommanderFrame frame, FileCommanderListPanel listPanel){
        this.fileCommanderListModel = fileCommanderListModel;
        this.frame=frame;
        this.listPanel = listPanel;
    }
    void addRootsToListModel(){
        fileCommanderListModel.getListModel().clear();
        File[] roots = File.listRoots();
        for(File file: roots){
            if(file.isHidden())fileCommanderListModel.getListModel().addElement(file.toString());
        }
    }
    void handleDoubleClick(String path){
        FileSystemObject file = new FileSystemObject(path);
        if(path.equals("..")){
            uploadParent();

        }
        else if(file.isDirectory()){
            uploadChildren(file);
        }
        else {
            frame.getFileCommanderOperationsFacade().openFile(listPanel.getHalf());
        }
    }
    private void uploadParent(){
        FileSystemObject selectedDirectoryFile = new FileSystemObject(fileCommanderListModel.getSelectedDirectory());
        if(selectedDirectoryFile.isRoot()){
            fileCommanderListModel.setSelectedDirectory("");
            listPanel.watchServiceHelper.changeObservableDirectory("C:\\");
            fileCommanderListModel.getListModel().clear();
            addRootsToListModel();
            return;
        }
        FileSystemObject parent = new FileSystemObject(new FileSystemObject(fileCommanderListModel.getSelectedDirectory()).getParent());
        fileCommanderListModel.getListModel().clear();
        fileCommanderListModel.getListModel().addElement("..");
        fileCommanderListModel.setSelectedDirectory(parent.toString());
        listPanel.watchServiceHelper.changeObservableDirectory(parent.toString());
        for(File file: parent.listFiles()){
            if(!file.isHidden()&&file.toString().endsWith(fileCommanderListModel.getSelectedExtension()))fileCommanderListModel.getListModel().addElement(file.toString());
        }
    }

    private void refreshList(){
        FileSystemObject selectedDirectoryFile = new FileSystemObject(fileCommanderListModel.getSelectedDirectory());
        if(!fileCommanderListModel.getSelectedDirectory().equals("")){
            fileCommanderListModel.getListModel().clear();
            fileCommanderListModel.getListModel().addElement("..");
            for(File file: selectedDirectoryFile.listFiles()){
                if(!file.isHidden()&&file.toString().endsWith(fileCommanderListModel.getSelectedExtension()))fileCommanderListModel.getListModel().addElement(file.toString());
            }
        }
        else{
            fileCommanderListModel.getListModel().clear();
            addRootsToListModel();
        }
    }
    private void uploadChildren(FileSystemObject fileDir){
        listPanel.watchServiceHelper.changeObservableDirectory(fileDir.toString());
        fileCommanderListModel.setSelectedDirectory(fileDir.toString());
        refreshList();
    }
}

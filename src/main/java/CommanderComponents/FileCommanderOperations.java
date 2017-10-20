package CommanderComponents;

import Adapters.FileSystemObject;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileCommanderOperations {
    FileCommanderFrame frame;
    FileCommanderOperations(FileCommanderFrame frame){
        this.frame = frame;
    }
    void createNewFile(String path){
        try {
            FileSystemObject file = new FileSystemObject(path);
            if(file.getExtension().equals("-dir")){
                createNewFolder(path);
                return;
            }
            boolean isCreated = file.createNewFile();
            System.out.println(file.createNewFile());
            if(isCreated){
                JOptionPane.showMessageDialog(frame,"Can't create new file","Error",1);
            }
            refreshLists();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    void refreshLists(){
        FileSystemObject selectedDirectoryFile = new FileSystemObject(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory());
        if(!selectedDirectoryFile.toString().equals("")){
            while(!selectedDirectoryFile.exists()){
                selectedDirectoryFile=new FileSystemObject(selectedDirectoryFile.getParent());
            }
            frame.getLeftListPanel().getFileCommanderListModel().setSelectedDirectory(selectedDirectoryFile.toString());
        }
        if(!frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory().equals("")){
            frame.getLeftListPanel().getFileCommanderListModel().getListModel().clear();
            frame.getLeftListPanel().getFileCommanderListModel().getListModel().addElement("..");
            for(File file: selectedDirectoryFile.listFiles()){
                if(file!=null&&!file.isHidden())frame.getLeftListPanel().getFileCommanderListModel().getListModel().addElement(file.toString());
            }
        }
        else
        {
            frame.getLeftListPanel().getFileCommanderListController().addRootsToListModel();
        }

        selectedDirectoryFile = new FileSystemObject(frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory());
        if(!selectedDirectoryFile.toString().equals("")){
            while(!selectedDirectoryFile.exists()){
                selectedDirectoryFile = new FileSystemObject(selectedDirectoryFile.getParent());
            }
        }
        frame.getRightListPanel().getFileCommanderListModel().setSelectedDirectory(selectedDirectoryFile.toString());
        if(!frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory().equals("")) {
            frame.getRightListPanel().getFileCommanderListModel().getListModel().clear();
            frame.getRightListPanel().getFileCommanderListModel().getListModel().addElement("..");
            for (File file : selectedDirectoryFile.listFiles()) {
                if(file!=null&&!file.isHidden())frame.getRightListPanel().getFileCommanderListModel().getListModel().addElement(file.toString());
            }
        }
        else{
            frame.getRightListPanel().getFileCommanderListController().addRootsToListModel();
        }
    }
    void createNewFolder(String path){
        try{
            FileSystemObject file = new FileSystemObject(path);
            FileUtils.forceMkdir(file);
            refreshLists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void copyFromLeft(){
        String from = frame.getLeftListPanel().getList().getSelectedValue();
        String to = frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory();
        copyFile(from,to);
    }
    void copyFromRight(){
        String from = frame.getRightListPanel().getList().getSelectedValue();
        String to = frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory();
        copyFile(from,to);
    }
    private void copyFile(String from, String to){
        try {
            FileSystemObject fileFrom = new FileSystemObject(from);
            FileSystemObject fileTo = new FileSystemObject(to + "\\" + fileFrom.getName());
            if (!fileFrom.isDirectory()) FileUtils.copyFile(fileFrom, fileTo);
            else FileUtils.copyDirectory(fileFrom,fileTo);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Got here!");
            JOptionPane.showMessageDialog(frame,"Can't copy file here","Error",1);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    void deleteFile(String path){
        try{
            FileSystemObject file = new FileSystemObject(path);
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void removeFile(String from, String to){
        try{
            copyFile(from,to);
            deleteFile(from);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void renameFile(String path, String name){
        try{
            FileSystemObject file = new FileSystemObject(path);
            boolean isRenamed = file.renameTo(new FileSystemObject(file.getParent()+"\\"+name));
            if(!isRenamed){
                JOptionPane.showMessageDialog(frame,"Can't rename this file");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void openFile(String path){
        try{
            FileSystemObject file = new FileSystemObject(path);
            /*Editor.FileEditorFrame editor = new Editor.FileEditorFrame(file);
            editor.setVisible(true);
            editor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);*/
            Desktop.getDesktop().open(file);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

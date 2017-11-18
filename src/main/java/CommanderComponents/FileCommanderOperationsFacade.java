package commanderComponents;

import adapters.FileSystemObject;

import org.apache.commons.io.FileUtils;

import javax.swing.*;

import java.io.*;


public class FileCommanderOperationsFacade {
    public FileCommanderOperations getOperations() {
        return operations;
    }

    private FileCommanderOperations operations;
    private FileCommanderFrame frame;
    FileCommanderOperationsFacade(FileCommanderFrame frame){
        this.frame = frame;
        this.operations=new FileCommanderOperations(frame);
    }
    void setFrame(FileCommanderFrame frame){
        this.frame=frame;
    }
    void createNewFile(String half, String name){
        FileCommanderListPanel panel = getPanelFromHalf(half);
        operations.createNewFile(panel.getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
    }

    void refreshLists(){
        operations.refreshLists();
    }
    public void createNewFolder(String path){
        if(handleExistingFile(path))return;
        try{
            FileSystemObject file = new FileSystemObject(path);
            FileUtils.forceMkdir(file);
            refreshLists();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copySelectedExtension(String selectedExtension, String chosenHalf){
        FileCommanderListPanel panel = (chosenHalf.equals("left"))?frame.getLeftListPanel():frame.getRightListPanel();
        String directory = panel.getFileCommanderListModel().getSelectedDirectory();
        FileCommanderListPanel anotherPanel = (chosenHalf.equals("right"))?frame.getLeftListPanel():frame.getRightListPanel();
        String to = anotherPanel.getFileCommanderListModel().getSelectedDirectory();
        operations.copySelectedExtension(selectedExtension,directory,to);
    }
    public void copyFile(String half, String anotherHalf){
        FileCommanderListPanel panel = getPanelFromHalf(half);
        FileCommanderListPanel anotherPanel = getPanelFromHalf(anotherHalf);
        String from = panel.getList().getSelectedValue();
        String to = anotherPanel.getFileCommanderListModel().getSelectedDirectory();
        operations.copyFile(from,to);
    }

    public void deleteFile(String path){
        try{
            FileSystemObject file = new FileSystemObject(path);
            com.sun.jna.platform.FileUtils fileUtils = com.sun.jna.platform.FileUtils.getInstance();
            fileUtils.moveToTrash(new File[] {file});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeFile(String from, String to){
        //if(handleExistingFile(to))return;
        try{
            operations.copyFile(from,to);
            deleteFile(from);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void renameFile(String path, String name){
        try{
            FileSystemObject file = new FileSystemObject(path);
            String newPath = file.getParent()+"\\"+name;
            if(handleExistingFile(newPath))return;
            boolean isRenamed = file.renameTo(new FileSystemObject(newPath));
            if(!isRenamed){
                JOptionPane.showMessageDialog(frame,"Can't rename this file");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void openFile(String half){
        FileCommanderListPanel panel = getPanelFromHalf(half);
        String path = panel.getList().getSelectedValue();
        operations.openFile(path);
    }

    private String readLine(InputStream is, boolean flag) throws IOException{
        StringBuilder sb = new StringBuilder();
        char t;
        do {
            t = (char)is.read();
            if(flag&&t!='\n'&&t!='\r')sb.append(t);
        }while(is.available()>0&&t!='\n');
        return new String(sb);
    }
    private void writeLine(OutputStream os, String line){
        for(int i=0;i<line.length();i++){
            try{
                os.write((byte)line.charAt(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            os.write((byte)'\r');
            os.write((byte)'\n');
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void copyFileWithoutRepeatingLines(String half, String anotherHalf)  {
        FileCommanderListPanel panel = getPanelFromHalf(half);
        FileCommanderListPanel anotherPanel = getPanelFromHalf(anotherHalf);
        String from = panel.getList().getSelectedValue();
        String to = anotherPanel.getFileCommanderListModel().getSelectedDirectory();
        operations.copyFileWithoutRepeatingLines(from,to);
    }

    private boolean handleExistingFile(String path){
        FileSystemObject file = new FileSystemObject(path);
        if(file.exists()){
            JOptionPane.showMessageDialog(frame,"File already exists","Error",1);
            return true;
        }
        return false;
    }

    void updateListWithExtension(String selectedItem, String half){
        String extension;
        FileCommanderListPanel panel = getPanelFromHalf(half);
        if(selectedItem.equals(".*"))extension="";
        else extension=selectedItem;
        panel.getFileCommanderListModel().setSelectedExtension(extension);
        operations.refreshList(panel,extension);
    }
    FileCommanderListPanel getPanelFromHalf(String half){
        return (half.equals("left"))? frame.getLeftListPanel(): frame.getRightListPanel();
    }
    public void copyHtmlFile(String chosenHalf){
        FileCommanderListPanel panel = getPanelFromHalf(chosenHalf);
        FileCommanderListPanel anotherPanel = (chosenHalf.equals("right"))?frame.getLeftListPanel():frame.getRightListPanel();
        String to = anotherPanel.getFileCommanderListModel().getSelectedDirectory();
        String htmlFile = panel.getList().getSelectedValue();
        operations.copyHtmlFile(htmlFile,to);
    }
    public void convertHtmlToPdf(String half)  {
        FileCommanderListPanel panel = getPanelFromHalf(half);
        String htmlFilePath =(String)panel.getList().getSelectedValue();
        operations.convertHtmlToPdf(htmlFilePath);
    }
    public void openSameDir(String half, String anotherHalf){
        FileCommanderListPanel panel = getPanelFromHalf(half);
        FileCommanderListPanel anotherPanel = getPanelFromHalf(anotherHalf);
        panel.getFileCommanderListModel().setSelectedDirectory(anotherPanel.getFileCommanderListModel().getSelectedDirectory());
    }
    private boolean isWord(String word){
        if(word.length()<1)return false;
        for(int i=0;i<word.length();i++){
            char ch = word.charAt(i);
            if(!((ch>='a'&&ch<='z')||(ch<='Z'&&ch>='A'))){System.out.println("qdfsafdas");return false;}
        }
        return true;
    }
    void calculateAppearances(String half){
        FileCommanderListPanel panel = getPanelFromHalf(half);
        String path = (panel.getList().getSelectedValue());
        operations.calculateAppearances(path);
    }
}

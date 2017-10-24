package CommanderComponents;

import Adapters.FileSystemObject;

import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileCommanderOperations {
    private FileCommanderFrame frame;
    public FileCommanderFrame getFrame(){
        return frame;
    }
    FileCommanderOperations(FileCommanderFrame frame){
        this.frame = frame;
    }
    public FileCommanderOperations(){}
    public void setFrame(FileCommanderFrame frame){
        this.frame=frame;
    }
    void createNewFile(String path){
        try {
            FileSystemObject file = new FileSystemObject(path);
            if (file.getExtension().equals("-dir")) {
                createNewFolder(path);
                return;
            }
            if (!handleExistingFile(path)) {
                boolean isCreated = file.createNewFile();
                System.out.println(file.createNewFile());
                if (isCreated) {
                    JOptionPane.showMessageDialog(frame, "Can't create new file", "Error", 1);
                }
                refreshLists();
            }
            } catch(IOException e){
                e.printStackTrace();
            }

    }
    public void refreshLists(){
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
            frame.getLeftListPanel().getFileCommanderListModel().getListModel().clear();
            frame.getLeftListPanel().getFileCommanderListModel().getFileCommanderListController().addRootsToListModel();
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
            frame.getRightListPanel().getFileCommanderListModel().getListModel().clear();
            frame.getRightListPanel().getFileCommanderListModel().getFileCommanderListController().addRootsToListModel();
        }
    }
    void createNewFolder(String path){
        if(handleExistingFile(path))return;
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
            if(handleExistingFile(fileTo.toString()))return;
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
            com.sun.jna.platform.FileUtils fileUtils = com.sun.jna.platform.FileUtils.getInstance();
            fileUtils.moveToTrash(new File[] {file});
            //FileUtils.forceDelete(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void removeFile(String from, String to){
        if(handleExistingFile(to))return;
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
    private String readLine(InputStream is) throws IOException{
        StringBuilder sb = new StringBuilder();
        char t;
        do {
            t = (char)is.read();
            sb.append(t);
        }while(is.available()>0&&t!='\n');
        return new String(sb);
    }
    public void writeLine(OutputStream os,String line){
        for(int i=0;i<line.length();i++){
            try{
                os.write((byte)line.charAt(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    void copyFileWithoutRepeatingLines(String from, String to)  {

        FileSystemObject fromFile = new FileSystemObject(from);
        FileSystemObject toFile = new FileSystemObject(to+"\\"+fromFile.getName());
        if(handleExistingFile(toFile.toString()))return;
        try {
            toFile.createNewFile();
            InputStream inputStream =FileUtils.openInputStream(fromFile);
            OutputStream outputStream =  FileUtils.openOutputStream(toFile);
            String prevLine = null;
            while(inputStream.available()>0){
                String line = readLine(inputStream);
                if(prevLine == null || !line.equals(prevLine)){
                    writeLine(outputStream,line);
                }
                prevLine=line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String convertToRTF(String htmlStr) {

        OutputStream os = new ByteArrayOutputStream();
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        RTFEditorKit rtfEditorKit = new RTFEditorKit();
        String rtfStr = null;

        htmlStr = htmlStr.replaceAll("<br.*?>","#NEW_LINE#");
        htmlStr = htmlStr.replaceAll("</p>","#NEW_LINE#");
        htmlStr = htmlStr.replaceAll("<p.*?>","");
        InputStream is = new ByteArrayInputStream(htmlStr.getBytes(StandardCharsets.UTF_16));
        try {
            Document doc = htmlEditorKit.createDefaultDocument();
            htmlEditorKit.read(is, doc, 0);
            rtfEditorKit.write(os, doc, 0, doc.getLength());
            rtfStr = os.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return rtfStr;
    }
    private boolean handleExistingFile(String path){
        FileSystemObject file = new FileSystemObject(path);
        if(file.exists()){
            JOptionPane.showMessageDialog(frame,"File already exists","Error",1);
            return true;
        }
        return false;
    }
    public void convertHtmlToRtf(String path){
        FileSystemObject htmlFile = new FileSystemObject(path);
        String rtfFileName = path.substring(0,path.length()-4)+"rtf";
        System.out.println(rtfFileName);
        FileSystemObject rtfFile = new FileSystemObject(rtfFileName);
        if(!rtfFile.exists()){
            try{
                rtfFile.createNewFile();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = FileUtils.openInputStream(htmlFile);
            while(is.available()>0){
                sb.append((char)is.read());
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(new String(sb));
        String rtfString = convertToRTF(new String(sb));
        try{
            OutputStream os = FileUtils.openOutputStream(rtfFile);
            for(int i=0;i<rtfString.length();i++){
                os.write((byte)rtfString.charAt(i));
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

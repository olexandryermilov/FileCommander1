package CommanderComponents;

import Adapters.FileSystemObject;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
        if(directory.equals("")||to.equals("")){
            JOptionPane.showMessageDialog(frame,"Can't copy here", "Error",1);
        }
        for(File file: new FileSystemObject(directory).listFiles()){
            if(file!=null&&!file.isHidden()&&!file.isDirectory()&&file.toString().endsWith(selectedExtension)){
                operations.copyFile(file.toString(),to);
            }
        }
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
    public void copyFileWithoutRepeatingLines(String from, String to)  {
        FileSystemObject fromFile = new FileSystemObject(from);
        FileSystemObject toFile = new FileSystemObject(to+"\\"+fromFile.getName());
        handleExistingFile(toFile.toString());
        if(handleExistingFile(toFile.toString()))return;
        try {
            toFile.createNewFile();
            InputStream inputStream =FileUtils.openInputStream(fromFile);
            OutputStream outputStream =  FileUtils.openOutputStream(toFile);
            String prevLine = null;
            while(inputStream.available()>0){
                String line = readLine(inputStream,false);
                if(prevLine == null || !line.equals(prevLine)){
                    writeLine(outputStream,line);
                }
                prevLine=line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        ArrayList<FileSystemObject> filesList = new ArrayList<>();
        filesList.add(new FileSystemObject(htmlFile));
        try{
            org.jsoup.nodes.Document doc = Jsoup.parse(new File(htmlFile),"UTF-8");
            Elements links = doc.select("a[href]");
            Elements media = doc.select("[src]");
            Elements imports = doc.select("link[href]");
            for(Element src : media){
                filesList.add(new FileSystemObject(src.attr("abs:src")));
            }
            for(Element link : links){
                filesList.add(new FileSystemObject(link.attr("href")));
            }
            for(Element link : imports){
                filesList.add(new FileSystemObject(link.attr("abs:href")));
            }
            for(FileSystemObject file : filesList){
                try{
                    String filePath = ((file.toString().startsWith("C:\\"))?"":(panel.getFileCommanderListModel().getSelectedDirectory()+"\\"))+file.toString();
                    operations.copyFile(filePath,to);
                }
                catch (RuntimeException e){
                    continue;
                }
            }
            Element title = doc.select("title").first();
            JOptionPane.showMessageDialog(frame,title.text(),"Title",1);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void convertHtmlToPdf(String half)  {
        FileCommanderListPanel panel = getPanelFromHalf(half);
        String htmlFilePath =(String)panel.getList().getSelectedValue();
        if(!htmlFilePath.endsWith(".html")){
            JOptionPane.showMessageDialog(frame,"Please, select html file.","Error",1);
            return;
        }
        String pdfFilePath =htmlFilePath.substring(0,htmlFilePath.length()-"html".length())+"pdf";
        System.out.println(pdfFilePath);
        File pdfFile = new File(pdfFilePath);
        File htmlFile = new File(htmlFilePath);
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("C:\\programming\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe C:\\programming\\phantomjs-2.1.1-windows\\examples\\rasterize.js "+
                    htmlFile.getName()+" "+pdfFile.getName(),null,htmlFile.getParentFile());
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame,"Couldn't convert file: "+e.getCause(),"Error",1);
        }
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
        /*if(!path.endsWith(".txt")){
            JOptionPane.showMessageDialog(frame,"Should be txt file","Error",1);
            return;
        }
        File file = new File(path);
        File resultFile = new File(path.substring(0,path.length()-".txt".length())+"resultOfMapping.txt");
        String line;
        HashMap<String, Integer> frequencyMap = new HashMap<>();
        try {
            InputStream is = FileUtils.openInputStream(file);
            while(is.available()>0){
                line = readLine(is,true);
                String[] words= line.split(" ");
                for(String word:words){
                    System.out.println(word);
                    if(!isWord(word))continue;
                    int v =(frequencyMap.containsKey(word))?frequencyMap.get(word):0;
                    frequencyMap.put(word,v+1);
                }
            }
            OutputStream os = FileUtils.openOutputStream(resultFile);
            for(Map.Entry<String,Integer> entry : frequencyMap.entrySet()){
                writeLine(os,entry.getKey()+": "+entry.getValue().toString());
                System.out.println(entry.getKey());
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
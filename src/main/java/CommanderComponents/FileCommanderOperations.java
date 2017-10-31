package CommanderComponents;

import Adapters.FileSystemObject;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.aioobe.cloudconvert.ProcessStatus;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class FileCommanderOperations {
    private FileCommanderFrame frame;
    public FileCommanderFrame getFrame(){
        return frame;
    }
    FileCommanderOperations(FileCommanderFrame frame){
        this.frame = frame;
    }
    public FileCommanderOperations(){}
    void setFrame(FileCommanderFrame frame){
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
    void refreshList(FileCommanderListPanel panel, String extension){
        FileSystemObject selectedDirectoryFile = new FileSystemObject(panel.getFileCommanderListModel().getSelectedDirectory());
        if(!selectedDirectoryFile.toString().equals("")){
            while(!selectedDirectoryFile.exists()){
                selectedDirectoryFile=new FileSystemObject(selectedDirectoryFile.getParent());
            }
            panel.getFileCommanderListModel().setSelectedDirectory(selectedDirectoryFile.toString());
        }
        if(!panel.getFileCommanderListModel().getSelectedDirectory().equals("")){
            panel.getFileCommanderListModel().getListModel().clear();
            panel.getFileCommanderListModel().getListModel().addElement("..");
            for(File file: selectedDirectoryFile.listFiles()){
                if(file!=null&&!file.isHidden()&&file.toString().endsWith(extension))panel.getFileCommanderListModel().getListModel().addElement(file.toString());
            }
        }
        else
        {
            panel.getFileCommanderListModel().getListModel().clear();
            panel.getFileCommanderListModel().getFileCommanderListController().addRootsToListModel();
        }
    }
    void refreshLists(){
        refreshList(frame.getLeftListPanel(),frame.getLeftListPanel().getFileCommanderListModel().getSelectedExtension());
        refreshList(frame.getRightListPanel(),frame.getRightListPanel().getFileCommanderListModel().getSelectedExtension());
        /*FileSystemObject selectedDirectoryFile = new FileSystemObject(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory());
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
        }*/
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
    void copySelectedExtension(String selectedExtension, String chosenHalf){
        FileCommanderListPanel panel = (chosenHalf.equals("left"))?frame.getLeftListPanel():frame.getRightListPanel();
        String directory = panel.getFileCommanderListModel().getSelectedDirectory();
        FileCommanderListPanel anotherPanel = (chosenHalf.equals("right"))?frame.getLeftListPanel():frame.getRightListPanel();
        String to = anotherPanel.getFileCommanderListModel().getSelectedDirectory();
        if(directory.equals("")||to.equals("")){
            JOptionPane.showMessageDialog(frame,"Can't copy here", "Error",1);
        }
        for(File file: new FileSystemObject(directory).listFiles()){
            if(file!=null&&!file.isHidden()&&!file.isDirectory()&&file.toString().endsWith(selectedExtension)){
                copyFile(file.toString(),to);
            }
        }
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
        //if(handleExistingFile(to))return;
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
    private void writeLine(OutputStream os, String line){
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
        refreshList(panel,extension);
    }
    FileCommanderListPanel getPanelFromHalf(String half){
        return (half.equals("left"))? frame.getLeftListPanel(): frame.getRightListPanel();
    }
    void copyHtmlFile(String chosenHalf){
        FileCommanderListPanel panel = getPanelFromHalf(chosenHalf);
        FileCommanderListPanel anotherPanel = (chosenHalf.equals("right"))?frame.getLeftListPanel():frame.getRightListPanel();
        String to = anotherPanel.getFileCommanderListModel().getSelectedDirectory();
        String htmlFile = panel.getList().getSelectedValue();
        ArrayList<FileSystemObject> filesList = new ArrayList<>();
        filesList.add(new FileSystemObject(htmlFile));
        try{
            org.jsoup.nodes.Document doc = Jsoup.parse(new File(htmlFile),"UTF-8");
            //System.out.println(doc);
            Elements links = doc.select("a[href]");
            Elements media = doc.select("[src]");
            Elements imports = doc.select("link[href]");
            for(Element src : media){
                //System.out.println(src.attr("abs:src"));
                filesList.add(new FileSystemObject(src.attr("abs:src")));
            }
            for(Element link : links){
                //System.out.println(link.attr("href"));
                //System.out.println(link.text());
                filesList.add(new FileSystemObject(link.attr("href")));
            }
            for(Element link : imports){
                //System.out.println(link.attr("href"));
                filesList.add(new FileSystemObject(link.attr("abs:href")));
            }
            for(FileSystemObject file : filesList){
                System.out.println(file);
                try{
                    String filePath = ((file.toString().startsWith("C:\\"))?"":(panel.getFileCommanderListModel().getSelectedDirectory()+"\\"))+file.toString();
                    System.out.println(filePath);
                    copyFile(filePath,to);
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
    void convertHtmlToPdf(String half)  {
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
       /* com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document,new FileOutputStream(pdfFilePath));
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer,document,new FileInputStream(htmlFilePath));
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
    void openSameDir(String half,String anotherHalf){
        FileCommanderListPanel panel = getPanelFromHalf(half);
        FileCommanderListPanel anotherPanel = getPanelFromHalf(anotherHalf);
        panel.getFileCommanderListModel().setSelectedDirectory(anotherPanel.getFileCommanderListModel().getSelectedDirectory());
    }
}

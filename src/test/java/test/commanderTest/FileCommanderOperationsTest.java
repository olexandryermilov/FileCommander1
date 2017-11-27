package test.commanderTest;

import commanderComponents.FileCommanderFrame;
import commanderComponents.FileCommanderOperations;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FileCommanderOperationsTest {
    private FileCommanderOperations operations;
    private FileCommanderFrame frame;
    private final String TEST_DIR_PATH="C:\\TestDir";
    private File testDir;
    private File sourceDir;
    private File targetDir;
    private void setupDirectory(){
        testDir=new File(TEST_DIR_PATH);
        testDir.mkdir();
        sourceDir = new File(TEST_DIR_PATH+"\\source");
        sourceDir.mkdir();
        targetDir=new File(TEST_DIR_PATH+"\\target");
        targetDir.mkdir();
    }
    @Before
    public void prepareTesting(){
        frame=new FileCommanderFrame();
        operations=frame.getFileCommanderOperationsFacade().getOperations();
        setupDirectory();
    }

    @Test
    public void createsNewFile(){
        String newFilePath = "test.txt";
        operations.createNewFile(TEST_DIR_PATH+"\\"+newFilePath);
        assertThat(Arrays.asList(testDir.list()),hasItem(newFilePath));
    }

    @Test
    public void checksForDirectory(){
        String newDir = "test";
        operations.createNewFile(TEST_DIR_PATH+"\\"+newDir);
        File file = null;
        for(File f: testDir.listFiles()){
            //System.out.println(f);
            if(f.getName().equals(newDir)) {
                file = f;
                //System.out.println(file.isDirectory());
                break;
            }
        }
        assertTrue(file!=null&&file.isDirectory());
    }

    @Test
    public void copiesFile(){
        String fileToCopyPath = "test.txt";
        File fileToCopy = new File(sourceDir.toString()+"\\"+fileToCopyPath);
        try {
            fileToCopy.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        operations.copyFile(fileToCopy.toString(),targetDir.toString()+"\\"+fileToCopyPath);
        List<String> targetDirChildren = Arrays.asList(targetDir.list());
        assertThat(targetDirChildren,hasItem(fileToCopyPath));
    }

    private void writeString(String line,FileOutputStream os){
        for(int i=0;i<line.length();i++){
            try{
                os.write((byte)line.charAt(i));
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    @Test
    public void calculatesFrequencies(){
        String fileToCalculatePath = "test.txt";
        File file = new File(testDir.toString()+"\\"+fileToCalculatePath);
        try {
            file.createNewFile();
            FileOutputStream os = FileUtils.openOutputStream(file);
            writeString("aaaa aaaa aaaa bb bb c 123",os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        operations.calculateAppearances(file.getPath());
        String rightAnswer = "bb: 2\r\nc: 1\r\naaaa: 3\r\n";
        String answer="";
        try{
            FileInputStream is = FileUtils.openInputStream(new File(file.getPath().
                                        substring(0,file.getPath().length()-".txt".length())+"resultOfMapping.txt"));
            StringBuilder sb = new StringBuilder();
            while(is.available()>0){
                sb.append((char)is.read());
            }
            is.close();
            answer = new String(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
        }
        assertTrue(rightAnswer.equals(answer));
    }
    @Test
    public void copiesWithoutRepeatingLines(){
        String fileContent = "aaaa\naaaa\naaaa\nbbbb\nbb bb";
        final String rightAnswer = "aaaa\r\nbbbb\r\nbb bb\r\n";
        String fileToCalculatePath = "test.txt";
        File file = new File(sourceDir.toString()+"\\"+fileToCalculatePath);
        try {
            file.createNewFile();
            FileOutputStream os = FileUtils.openOutputStream(file);
            writeString(fileContent,os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        operations.copyFileWithoutRepeatingLines(file.toString(),targetDir.toString());

        String answer="";
        try{
            FileInputStream is = FileUtils.openInputStream(new File(targetDir.toString()+"\\"+file.getName()));
            StringBuilder sb = new StringBuilder();
            while(is.available()>0){
                char c = (char)is.read();
                sb.append(c);
            }
            is.close();
            answer = new String(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertTrue(rightAnswer.equals(answer));
    }
    @Test
    public void copiesHtmlFile(){
        String fileContent = "<head>\n" +
                "\t<title>Hello</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<a href=\"a.txt\">a</img>\n" +
                "</body>";
        String htmlFilePath = "test.html";
        File htmlFile = new File(sourceDir.toString()+"\\"+htmlFilePath);
        File aFile = new File(sourceDir.toString()+"\\a.txt");
        try {
            operations.createNewFile(htmlFile.toString());
            aFile.createNewFile();
            FileOutputStream os = FileUtils.openOutputStream(htmlFile);
            writeString(fileContent,os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        operations.copyHtmlFile(htmlFile.toString(),targetDir.toString());
        List<String> targetDirChildren = Arrays.asList(targetDir.list());
        assertThat(targetDirChildren,hasItems("a.txt","test.html"));
    }
    @Test
    public void copiesSelectedExtension(){
        File firstFile = new File(sourceDir+"\\"+"a1.txt");
        File secondFile = new File(sourceDir+"\\"+"a2.txt");
        File thirdFile = new File(sourceDir+"\\"+"a3.html");
        try{
            firstFile.createNewFile();
            secondFile.createNewFile();
            thirdFile.createNewFile();
        }
        catch (IOException e){
            System.out.println(e.getCause());
        }
        operations.copySelectedExtension(".txt",sourceDir.toString(),targetDir.toString());
        List<String> targetDirChildren = Arrays.asList(targetDir.list());
        assertThat(targetDirChildren,hasItems("a1.txt","a2.txt"));
    }
    @Test
    public void copiesNotSelectedExtension(){
        File firstFile = new File(sourceDir+"\\"+"a1.txt");
        File secondFile = new File(sourceDir+"\\"+"a2.txt");
        File thirdFile = new File(sourceDir+"\\"+"a3.html");
        try{
            firstFile.createNewFile();
            secondFile.createNewFile();
            thirdFile.createNewFile();
        }
        catch (IOException e){
            System.out.println(e.getCause());
        }
        operations.copySelectedExtension(".txt",sourceDir.toString(),targetDir.toString());
        List<String> targetDirChildren = Arrays.asList(targetDir.list());
        assertThat(targetDirChildren,not(hasItem("a3.html")));
    }
    @Test
    public void convertsFileToPdf(){
        File file = new File(testDir.toString()+"\\"+"test.html");
        operations.createNewFile(file.toString());
        operations.convertHtmlToPdf(file.toString());
        List<String> testDirChildren = Arrays.asList(testDir.list());
        assertThat(testDirChildren,hasItem("test.pdf"));
    }
    @After
    public void clean(){
        File[] toDelete = new File[1];
        toDelete[0]=testDir;
        operations.deleteFile(toDelete[0].toString());
    }
}

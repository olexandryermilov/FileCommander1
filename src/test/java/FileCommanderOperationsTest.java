import CommanderComponents.FileCommanderFrame;
import CommanderComponents.FileCommanderOperations;
import CommanderComponents.FileCommanderOperationsFacade;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;

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
        File file = new File(sourceDir.toString()+"\\"+fileToCalculatePath);
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
            System.out.println(answer.length());
            System.out.println(rightAnswer.length());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertTrue(rightAnswer.equals(answer));
    }
    @After
    public void clean(){
        File[] toDelete = new File[1];
        toDelete[0]=testDir;
        try {
            FileUtils.forceDelete(toDelete[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
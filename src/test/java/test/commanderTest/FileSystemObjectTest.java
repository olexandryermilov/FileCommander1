package test.commanderTest;

import adapters.FileSystemObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FileSystemObjectTest {
    @Test
    public void isRoot_GivesTrue_ForC(){
        final FileSystemObject FILE = new FileSystemObject("C:\\");
        final boolean RIGHT_ANSWER = true;
        boolean answer = FILE.isRoot();
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void isRoot_GivesFalse_ForDir(){
        FileSystemObject fileDir = new FileSystemObject("C:\\test");
        if(!fileDir.exists()) try {
            fileDir.createNewFile();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        final boolean RIGHT_ANSWER = false;
        boolean answer = fileDir.isRoot();
        assertEquals(RIGHT_ANSWER,answer);
    }
    @Test
    public void isRoot_GivesFalse_ForFile(){
        FileSystemObject file = new FileSystemObject("C:\\test\\test.txt");
        if(!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        final boolean RIGHT_ANSWER = false;
        boolean answer = file.isRoot();
        assertEquals(RIGHT_ANSWER,answer);
    }
}

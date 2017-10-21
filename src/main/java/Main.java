import Adapters.FileSystemObject;
import CommanderComponents.FileCommanderFrame;
import CommanderComponents.FileCommanderListModel;
import CommanderComponents.FileCommanderOperations;
import CommanderComponents.WatchServiceHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.*;

public class Main {

    public static void main(String args[]) {

        EventQueue.invokeLater(()-> {
            FileCommanderFrame frame = new FileCommanderFrame();
            frame.getLeftListPanel().setWatchServiceHelper(new WatchServiceHelper(frame.getFileCommanderOperations()));
            frame.getRightListPanel().setWatchServiceHelper(new WatchServiceHelper(frame.getFileCommanderOperations()));
            frame.getFileCommanderOperations().refreshLists();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });
    }

}

import Adapters.FileSystemObject;
import CommanderComponents.FileCommanderFrame;
import CommanderComponents.FileCommanderListModel;
import CommanderComponents.FileCommanderOperations;
import CommanderComponents.WatchServiceHelper;
import Editor.FileEditorFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.*;

public class Main {

    static FileCommanderFrame frame;
    public static void main(String args[]) {

        EventQueue.invokeLater(()-> {
            frame = new FileCommanderFrame();
            frame.setVisible(true);
            frame.setLocation(15,15);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        });

    }

}

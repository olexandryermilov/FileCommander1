package fileCommander;

import commanderComponents.FileCommanderFrame;

import javax.swing.*;
import java.awt.*;

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

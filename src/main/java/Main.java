import CommanderComponents.FileCommanderFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String args[]){
        EventQueue.invokeLater(()->{
            FileCommanderFrame frame = new FileCommanderFrame();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}

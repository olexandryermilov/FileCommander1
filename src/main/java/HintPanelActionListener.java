import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HintPanelActionListener implements ActionListener {
    FileCommanderFrame frame;
    HintPanelActionListener(FileCommanderFrame frame){
        this.frame= frame;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("New File left")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new file","Enter name",1);
            frame.getFileCommanderOperations().createNewFile(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("New File right")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new file","Enter name",1);
            frame.getFileCommanderOperations().createNewFile(frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("New Folder left")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new folder","Enter name",1);
            //System.out.println(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
            frame.getFileCommanderOperations().createNewFolder(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("New Folder right")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new folder","Enter name",1);
            //System.out.println(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
            frame.getFileCommanderOperations().createNewFolder(frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("Copy left")){
            frame.getFileCommanderOperations().copyFromLeft();
        }
        if(e.getActionCommand().equals("Copy right")){
            frame.getFileCommanderOperations().copyFromRight();
        }
        if(e.getActionCommand().equals("Delete left")){
            int confirmation = JOptionPane.showConfirmDialog(frame,"Do you really want to delete this file?","Confirm action",2);
            frame.getFileCommanderOperations().deleteFile(frame.getLeftListPanel().getList().getSelectedValue());
        }
        if(e.getActionCommand().equals("Delete right")){
            int confirmation = JOptionPane.showConfirmDialog(frame,"Do you really want to delete this file?","Confirm action",2);
            frame.getFileCommanderOperations().deleteFile(frame.getRightListPanel().getList().getSelectedValue());
        }
    }
}

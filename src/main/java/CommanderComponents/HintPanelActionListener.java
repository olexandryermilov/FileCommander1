package CommanderComponents;

import CommanderComponents.FileCommanderFrame;

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
            if(name!=null)
            frame.getFileCommanderOperations().createNewFile(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("New File right")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new file","Enter name",1);
            if(name!=null)frame.getFileCommanderOperations().createNewFile(frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("New Folder left")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new folder","Enter name",1);
            //System.out.println(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
            if(name!=null)frame.getFileCommanderOperations().createNewFolder(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("New Folder right")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new folder","Enter name",1);
            //System.out.println(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
            if(name!=null)frame.getFileCommanderOperations().createNewFolder(frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("Copy left")){
            frame.getFileCommanderOperations().copyFromLeft();
        }
        if(e.getActionCommand().equals("Copy right")){
            frame.getFileCommanderOperations().copyFromRight();
        }
        //todo: delete removes, not deletes
        if(e.getActionCommand().equals("Delete left")){
            String path = frame.getLeftListPanel().getList().getSelectedValue();
            if(path=="..")JOptionPane.showMessageDialog(frame,"Cannot delete this file","Info",1);
            else
            {
                int confirmation = JOptionPane.showConfirmDialog(frame,"Do you really want to delete this file?","Confirm action",2);
                frame.getFileCommanderOperations().deleteFile(path);
            }
        }
        if(e.getActionCommand().equals("Delete right")){
            String path = frame.getRightListPanel().getList().getSelectedValue();
            if(path==".."){
                JOptionPane.showMessageDialog(frame,"Cannot delete this file","Info",1);
            }
            else {
                int confirmation = JOptionPane.showConfirmDialog(frame, "Do you really want to delete this file?", "Confirm action", 2);
                frame.getFileCommanderOperations().deleteFile(path);
            }
        }
        if(e.getActionCommand().equals("Remove left")){
            frame.getFileCommanderOperations().removeFile(frame.getLeftListPanel().getList().getSelectedValue(),frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory());
        }
        if(e.getActionCommand().equals("Remove right")){
            frame.getFileCommanderOperations().removeFile(frame.getRightListPanel().getList().getSelectedValue(),frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory());
        }
        if(e.getActionCommand().equals("Rename left")){
            String name = JOptionPane.showInputDialog(frame,"Enter new name of file","Enter name",1);
            frame.getFileCommanderOperations().renameFile(frame.getLeftListPanel().getList().getSelectedValue(),name);
        }
        if(e.getActionCommand().equals("Rename right")){
            String name = JOptionPane.showInputDialog(frame,"Enter new name of file","Enter name",1);
            frame.getFileCommanderOperations().renameFile(frame.getRightListPanel().getList().getSelectedValue(),name);
        }
        if(e.getActionCommand().equals("Open left")){
            frame.getFileCommanderOperations().openFile(frame.getLeftListPanel().getList().getSelectedValue());
        }
        if(e.getActionCommand().equals("Open right")) {
            frame.getFileCommanderOperations().openFile(frame.getRightListPanel().getList().getSelectedValue());
        }
        frame.getFileCommanderOperations().refreshLists();
    }
}

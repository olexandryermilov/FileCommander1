package CommanderComponents;

import CommanderComponents.FileCommanderFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HintPanelActionListener implements ActionListener {
    FileCommanderFrame frame;
    FileCommanderOperations operations;
    FileCommanderListPanel left, right;
    HintPanelActionListener(FileCommanderFrame frame) {
        this.frame= frame;
        this.operations = frame.getFileCommanderOperations();
        this.left=frame.getLeftListPanel();
        this.right = frame.getRightListPanel();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        operations=frame.getFileCommanderOperations();
        if(e.getActionCommand().equals("New File left")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new file","Enter name",1);
            if(name!=null)
                operations.createNewFile(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
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
        if(e.getActionCommand().equals("Copy without repeating lines left")){
            frame.getFileCommanderOperations().copyFileWithoutRepeatingLines(frame.getLeftListPanel().getList().getSelectedValue(),frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory());
        }
        if(e.getActionCommand().equals("Copy without repeating lines right")) {
            frame.getFileCommanderOperations().copyFileWithoutRepeatingLines(frame.getRightListPanel().getList().getSelectedValue(),frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory());
        }
        if(e.getActionCommand().equals("Html to PDF left")){
            frame.getFileCommanderOperations().convertHtmlToPdf("left");
        }
        if(e.getActionCommand().equals("Html to PDF right")){
            frame.getFileCommanderOperations().convertHtmlToPdf("right");
        }
        if(e.getActionCommand().equals("Copy extension left")){
            frame.getFileCommanderOperations().copySelectedExtension((String)frame.getLeftListPanel().getHintPanel().getExtensionComboBox().getSelectedItem(),"left");
        }
        if(e.getActionCommand().equals("Copy extension right")){
            frame.getFileCommanderOperations().copySelectedExtension((String)frame.getRightListPanel().getHintPanel().getExtensionComboBox().getSelectedItem(),"right");
        }
        if(e.getActionCommand().equals("Copy HTML left")){
            frame.getFileCommanderOperations().copyHtmlFile("left");
        }
        if(e.getActionCommand().equals("Copy HTML right")){
            frame.getFileCommanderOperations().copyHtmlFile("right");
        }
        if(e.getActionCommand().equals("Open same dir left")){
            frame.getFileCommanderOperations().openSameDir("left","right");
        }
        if(e.getActionCommand().equals("Open same dir right")){
            frame.getFileCommanderOperations().openSameDir("right","left");
        }
        frame.getFileCommanderOperations().refreshLists();
    }
}

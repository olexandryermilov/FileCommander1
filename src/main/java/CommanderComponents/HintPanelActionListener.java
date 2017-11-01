package CommanderComponents;

import CommanderComponents.FileCommanderFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HintPanelActionListener implements ActionListener {
    FileCommanderFrame frame;
    FileCommanderOperationsFacade operations;
    FileCommanderListPanel left, right;
    HintPanelActionListener(FileCommanderFrame frame) {
        this.frame= frame;
        this.operations = frame.getFileCommanderOperationsFacade();
        this.left=frame.getLeftListPanel();
        this.right = frame.getRightListPanel();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        operations=frame.getFileCommanderOperationsFacade();
        if(e.getActionCommand().equals("New File left")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new file","Enter name",1);
            if(name!=null)
                operations.createNewFile(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("New File right")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new file","Enter name",1);
            if(name!=null)operations.createNewFile(frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("New Folder left")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new folder","Enter name",1);
            if(name!=null)operations.createNewFolder(frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("New Folder right")){
            String name = JOptionPane.showInputDialog(frame,"Enter name of new folder","Enter name",1);
            if(name!=null)operations.createNewFolder(frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory()+"\\"+name);
        }
        if(e.getActionCommand().equals("Copy left")){
            operations.copyFile("left","right");
        }
        if(e.getActionCommand().equals("Copy right")){
            operations.copyFile("right","left");
        }

        if(e.getActionCommand().equals("Delete left")){
            String path = frame.getLeftListPanel().getList().getSelectedValue();
            if(path=="..")JOptionPane.showMessageDialog(frame,"Cannot delete this file","Info",1);
            else
            {
                int confirmation = JOptionPane.showConfirmDialog(frame,"Do you really want to delete this file?","Confirm action",2);
                if(confirmation==0)operations.deleteFile(path);
            }
        }
        if(e.getActionCommand().equals("Delete right")){
            String path = frame.getRightListPanel().getList().getSelectedValue();
            if(path==".."){
                JOptionPane.showMessageDialog(frame,"Cannot delete this file","Info",1);
            }
            else {
                int confirmation = JOptionPane.showConfirmDialog(frame, "Do you really want to delete this file?", "Confirm action", 2);
                if(confirmation==0)operations.deleteFile(path);
            }
        }
        if(e.getActionCommand().equals("Remove left")){
            operations.removeFile(frame.getLeftListPanel().getList().getSelectedValue(),frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory());
        }
        if(e.getActionCommand().equals("Remove right")){
            operations.removeFile(frame.getRightListPanel().getList().getSelectedValue(),frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory());
        }
        if(e.getActionCommand().equals("Rename left")){
            String name = JOptionPane.showInputDialog(frame,"Enter new name of file","Enter name",1);
            operations.renameFile(frame.getLeftListPanel().getList().getSelectedValue(),name);
        }
        if(e.getActionCommand().equals("Rename right")){
            String name = JOptionPane.showInputDialog(frame,"Enter new name of file","Enter name",1);
            operations.renameFile(frame.getRightListPanel().getList().getSelectedValue(),name);
        }
        if(e.getActionCommand().equals("Open left")){
            operations.openFile(frame.getLeftListPanel().getList().getSelectedValue());
        }
        if(e.getActionCommand().equals("Open right")) {
            operations.openFile(frame.getRightListPanel().getList().getSelectedValue());
        }
        if(e.getActionCommand().equals("Copy without repeating lines left")){
            operations.copyFileWithoutRepeatingLines(frame.getLeftListPanel().getList().getSelectedValue(),frame.getRightListPanel().getFileCommanderListModel().getSelectedDirectory());
        }
        if(e.getActionCommand().equals("Copy without repeating lines right")) {
            operations.copyFileWithoutRepeatingLines(frame.getRightListPanel().getList().getSelectedValue(),frame.getLeftListPanel().getFileCommanderListModel().getSelectedDirectory());
        }
        if(e.getActionCommand().equals("Html to PDF left")){
            operations.convertHtmlToPdf("left");
        }
        if(e.getActionCommand().equals("Html to PDF right")){
            operations.convertHtmlToPdf("right");
        }
        if(e.getActionCommand().equals("Copy extension left")){
            operations.copySelectedExtension((String)frame.getLeftListPanel().getHintPanel().getExtensionComboBox().getSelectedItem(),"left");
        }
        if(e.getActionCommand().equals("Copy extension right")){
            operations.copySelectedExtension((String)frame.getRightListPanel().getHintPanel().getExtensionComboBox().getSelectedItem(),"right");
        }
        if(e.getActionCommand().equals("Copy HTML left")){
            operations.copyHtmlFile("left");
        }
        if(e.getActionCommand().equals("Copy HTML right")){
            operations.copyHtmlFile("right");
        }
        if(e.getActionCommand().equals("Open same dir left")){
            operations.openSameDir("left","right");
        }
        if(e.getActionCommand().equals("Open same dir right")){
            operations.openSameDir("right","left");
        }
        if(e.getActionCommand().equals("Frequency left")){
            operations.calculateAppearances("left");
        }
        if(e.getActionCommand().equals("Frequency right")){
            operations.calculateAppearances("right");
        }
        operations.refreshLists();
    }
}

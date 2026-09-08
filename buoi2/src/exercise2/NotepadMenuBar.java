package exercise2;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class NotepadMenuBar extends JMenuBar {
    private FileController fileCtrl;
    private JTextArea textArea;

    public NotepadMenuBar(FileController fileCtrl, JTextArea textArea) {
        this.fileCtrl = fileCtrl;
        this.textArea = textArea;
        
        buildFileMenu();
        buildEditMenu();
        buildFormatMenu();
        buildViewMenu();
        buildHelpMenu();
    }

    private void buildFileMenu() {
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem newItem = new JMenuItem("New");
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newItem.addActionListener(e -> fileCtrl.newFile());
        
        JMenuItem openItem = new JMenuItem("Open...");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(e -> fileCtrl.openFile());
        
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> fileCtrl.saveFile());
        
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        saveAsItem.addActionListener(e -> fileCtrl.saveAsFile());
        
        JMenuItem printItem = new JMenuItem("Print");
        printItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        printItem.addActionListener(e -> fileCtrl.printFile());
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> fileCtrl.exitApp());
        
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(printItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        add(fileMenu);
    }

    private void buildEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        cutItem.addActionListener(e -> textArea.cut());
        
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        copyItem.addActionListener(e -> textArea.copy());
        
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
        pasteItem.addActionListener(e -> textArea.paste());
        
        JMenuItem selectAllItem = new JMenuItem("Select All");
        selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        selectAllItem.addActionListener(e -> textArea.selectAll());
        
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.addSeparator();
        editMenu.add(selectAllItem);
        
        add(editMenu);
    }

    private void buildFormatMenu() {
        JMenu formatMenu = new JMenu("Format");
        JCheckBoxMenuItem wordWrapItem = new JCheckBoxMenuItem("Word Wrap");
        wordWrapItem.addActionListener(e -> {
            boolean wrap = wordWrapItem.isSelected();
            textArea.setLineWrap(wrap);
            textArea.setWrapStyleWord(wrap);
        });
        formatMenu.add(wordWrapItem);
        add(formatMenu);
    }

    private void buildViewMenu() {
        JMenu viewMenu = new JMenu("View");
        add(viewMenu);
    }

    private void buildHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About Notepad");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Notepad App Demo\nChuyên trị file 20MB!"));
        helpMenu.add(aboutItem);
        add(helpMenu);
    }
}
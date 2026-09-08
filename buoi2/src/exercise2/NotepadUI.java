package exercise2;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class NotepadUI extends JFrame {
    private FileController fileController;

    public NotepadUI() {
        setTitle("Untitled - Notepad App");
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        fileController = new FileController(this, textArea);
        NotepadMenuBar menuBar = new NotepadMenuBar(fileController, textArea);
        NotepadStatusBar statusBar = new NotepadStatusBar(textArea, fileController);
        
        setJMenuBar(menuBar);
        add(scrollPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileController.exitApp();
            }
        });
    }
}
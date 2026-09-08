package exercise5;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class CopyFileGUI extends JFrame {
    
    private JTextField txtFrom;
    private JTextField txtTo;
    private JButton btnCopy;
    private JProgressBar progressBar;

    public CopyFileGUI() {
        setTitle("Copying File");
        setSize(450, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        Box mainBox = Box.createVerticalBox();
        mainBox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel "From"
        JPanel pnlFrom = new JPanel(new BorderLayout());
        pnlFrom.setBorder(BorderFactory.createTitledBorder("From"));
        txtFrom = new JTextField("From");
        pnlFrom.add(txtFrom, BorderLayout.CENTER);
        
        // Panel "To"
        JPanel pnlTo = new JPanel(new BorderLayout());
        pnlTo.setBorder(BorderFactory.createTitledBorder("To"));
        txtTo = new JTextField("To");
        pnlTo.add(txtTo, BorderLayout.CENTER);
        
        mainBox.add(pnlFrom);
        mainBox.add(Box.createVerticalStrut(10));
        mainBox.add(pnlTo);

        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnCopy = new JButton("Copy...");
        pnlButton.add(btnCopy);
        
        mainBox.add(Box.createVerticalStrut(10));
        mainBox.add(pnlButton);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(450, 25));

        setLayout(new BorderLayout());
        add(mainBox, BorderLayout.CENTER);
        add(progressBar, BorderLayout.SOUTH);

        btnCopy.addActionListener(e -> startCopyProcess());
    }

    private void startCopyProcess() {
        File sourceFile = new File(txtFrom.getText().trim());
        File destFile = new File(txtTo.getText().trim());

        if (!sourceFile.exists()) {
            JOptionPane.showMessageDialog(this, "File nguồn không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        btnCopy.setEnabled(false);
        txtFrom.setEnabled(false);
        txtTo.setEnabled(false);
        progressBar.setValue(0);

        CopyFileTask task = new CopyFileTask(sourceFile, destFile);

        task.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName())) {
                    progressBar.setValue((Integer) evt.getNewValue());
                }
            }
        });
        task.execute();
    }
}
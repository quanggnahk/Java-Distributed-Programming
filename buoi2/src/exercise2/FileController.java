package exercise2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import javax.swing.*;

public class FileController {
    private JFrame parentFrame;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private File currentFile;
    private boolean isModified = false;
    private boolean isProgrammaticChange = false;

    public FileController(JFrame parentFrame, JTextArea textArea) {
        this.parentFrame = parentFrame;
        this.textArea = textArea;
        this.fileChooser = new JFileChooser();
    }

    public void setModified(boolean modified) {
        if (isProgrammaticChange) return;
        this.isModified = modified;
        updateTitle();
    }

    private void updateTitle() {
        String title = (currentFile != null ? currentFile.getName() : "Untitled") 
                     + (isModified ? " *" : "") + " - Notepad App";
        parentFrame.setTitle(title);
    }

    public void newFile() {
        if (promptSaveIfModified()) {
            isProgrammaticChange = true;
            textArea.setText("");
            currentFile = null;
            isProgrammaticChange = false;
            setModified(false);
        }
    }

    public void openFile() {
        if (!promptSaveIfModified()) return;
        
        int result = fileChooser.showOpenDialog(parentFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            isProgrammaticChange = true;
            textArea.setText(""); 
            
            SwingWorker<Void, String> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(currentFile), StandardCharsets.UTF_8))) {
                        char[] buffer = new char[16384]; // 16KB chunking
                        int charsRead;
                        while ((charsRead = reader.read(buffer)) != -1) {
                            publish(new String(buffer, 0, charsRead));
                        }
                    }
                    return null;
                }
                @Override
                protected void process(List<String> chunks) {
                    StringBuilder sb = new StringBuilder();
                    for (String chunk : chunks) sb.append(chunk);
                    textArea.append(sb.toString());
                }
                @Override
                protected void done() {
                    textArea.setCaretPosition(0);
                    isProgrammaticChange = false;
                    setModified(false);
                }
            };
            worker.execute();
        }
    }

    public void saveFile() {
        if (currentFile == null) saveAsFile();
        else executeSaveWorker();
    }

    public void saveAsFile() {
        int result = fileChooser.showSaveDialog(parentFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            executeSaveWorker();
        }
    }

    private void executeSaveWorker() {
        String content = textArea.getText();
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentFile), StandardCharsets.UTF_8))) {
                    writer.write(content);
                }
                return null;
            }
            @Override
            protected void done() {
                try {
                    get();
                    setModified(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parentFrame, "Lỗi khi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    public void printFile() {
        try {
            MessageFormat header = new MessageFormat(currentFile != null ? currentFile.getName() : "Untitled");
            MessageFormat footer = new MessageFormat("- {0} -");
            textArea.print(header, footer, true, null, null, true);
        } catch (Exception pe) {
            JOptionPane.showMessageDialog(parentFrame, "Lỗi in ấn: " + pe.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean promptSaveIfModified() {
        if (!isModified) return true;
        int confirm = JOptionPane.showConfirmDialog(parentFrame,
            "Bạn có thay đổi chưa lưu. Bạn có muốn lưu trước khi tiếp tục?",
            "Xác nhận", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            saveFile();
            return true; 
        }
        return confirm == JOptionPane.NO_OPTION; 
    }

    public void exitApp() {
        if (promptSaveIfModified()) {
            System.exit(0);
        }
    }
}
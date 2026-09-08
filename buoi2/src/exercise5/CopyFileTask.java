package exercise5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class CopyFileTask extends SwingWorker<Boolean, Void> {

    private File source;
    private File dest;

    public CopyFileTask(File source, File dest) {
        this.source = source;
        this.dest = dest;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        long totalBytes = source.length();
        long copiedBytes = 0;

        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(dest)) {
             
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
                copiedBytes += bytesRead;

                int progress = (int) ((copiedBytes * 100) / totalBytes);
                setProgress(progress);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
        return true;
    }

    @Override
    protected void done() {
        try {
            boolean success = get();
            if (success) {
                JOptionPane.showMessageDialog(null, "Copy file thành công!");
            } else {
                JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi copy file.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package exercise5;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CopyFileGUI().setVisible(true);
        });
    }
}
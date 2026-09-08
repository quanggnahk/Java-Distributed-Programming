package exercise2;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NotepadStatusBar extends JLabel {
    
    public NotepadStatusBar(JTextArea textArea, FileController fileCtrl) {
        super(" || Ln 1, Col 1");
        setBorder(new EmptyBorder(2, 5, 2, 5));
        
        textArea.addCaretListener((CaretEvent e) -> updateLineAndCol(textArea));

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { fileCtrl.setModified(true); }
            @Override
            public void removeUpdate(DocumentEvent e) { fileCtrl.setModified(true); }
            @Override
            public void changedUpdate(DocumentEvent e) { fileCtrl.setModified(true); }
        });
    }

    private void updateLineAndCol(JTextArea textArea) {
        try {
            int caretPos = textArea.getCaretPosition();
            int row = textArea.getLineOfOffset(caretPos);
            int column = caretPos - textArea.getLineStartOffset(row);
            setText(" || Ln " + (row + 1) + ", Col " + (column + 1));
        } catch (Exception ex) {}
    }
}
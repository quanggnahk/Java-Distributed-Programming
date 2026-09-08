package exercise1;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;


public class UI extends JFrame{
	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JProgressBar progressBar;
	private JButton btnSW;
	private JButton btnEDT;
	
	public UI() {
		setTitle("SwingWorker");
		setSize(600, 200);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Box a, b, c, d;
		add(a = Box.createVerticalBox());
		a.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		a.add(b = Box.createHorizontalBox());
		a.add(Box.createVerticalStrut(10));
		b.add(progressBar = new JProgressBar());
		progressBar.setStringPainted(true);
		
		a.add(c = Box.createHorizontalBox());
		a.add(Box.createVerticalStrut(10));
		c.add(label = new JLabel());
		label.setText("Value: 0");
		
		a.add(d = Box.createHorizontalBox());
		d.add(btnSW = new JButton("SwingWorker"));
		d.add(Box.createHorizontalStrut(20));
		d.add(btnEDT = new JButton("EDT"));
		
		btnEDT.addActionListener(event -> {
            label.setText("Status: Running on EDT (UI freeze)...");
            progressBar.setValue(0);
            
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                progressBar.setValue(i);
            }
            label.setText("Status: Completed on EDT!");
        });
		
		btnSW.addActionListener(event -> {
			Task task = new Task(label);
			task.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName() == "progress") {
						progressBar.setValue((int)evt.getNewValue());
					}
				}
			});
			task.execute();
		});
	}
}

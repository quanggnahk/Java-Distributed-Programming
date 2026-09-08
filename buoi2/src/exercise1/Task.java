package exercise1;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class Task extends SwingWorker<Integer, Integer>{
	private JLabel label;
	public Task(JLabel label) {
		this.label = label;
	}
	
	@Override
	protected Integer doInBackground() throws Exception {
		int total = 0;
		int progress = 0;
		for (int i = 0 ; i < 1000 ; i += 10) {
			total += i;
			publish(total);
			progress++;
			setProgress(progress);
			Thread.sleep(50);
		}
		return total;
	}
	@Override
	protected void process(List<Integer> chunks) {
		super.process(chunks);
		int value = chunks.get(chunks.size() - 1);
		label.setText("Value: " + value);
	}
	@Override
	protected void done() {
		super.done();
	}
}
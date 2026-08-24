package exercise2;

public class YourTask implements Runnable {
	@Override
	public void run() {
		try {
			Thread t = new Thread(new AnotherTask("Another Task", 10));
			for (int i = 0 ; i < 8 ; i++) {
				System.out.println("Your Task #" + i);
				if (i == 5) {
					t.start();
					t.join();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
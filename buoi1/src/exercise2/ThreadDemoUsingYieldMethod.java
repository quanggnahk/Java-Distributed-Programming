package exercise2;

public class ThreadDemoUsingYieldMethod implements Runnable{
	private Thread t;
	
	public ThreadDemoUsingYieldMethod(String str) {
		t = new Thread(this, str);
		t.start();
	}
	
	public void run() {
		for (int i = 0 ; i < 5 ; i++) {
			if (i % 5 == 0) {
				System.out.println(Thread.currentThread().getName() + " yielding control...");
				Thread.yield();
			}
		}
		System.out.println(Thread.currentThread().getName() + " has finished executing.");
	}
	
	public static void main(String[] args) {
		new ThreadDemoUsingYieldMethod("Thread 1");
		new ThreadDemoUsingYieldMethod("Thread 2");
		new ThreadDemoUsingYieldMethod("Thread 3");
	}
}
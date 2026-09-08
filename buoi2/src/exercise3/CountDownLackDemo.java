package exercise3;

import java.util.concurrent.CountDownLatch;

public class CountDownLackDemo {
	private static final int PARTIES = 4;
	
	public static void main(String[] arg) throws InterruptedException {
		CountDownLatch entryBarrier = new CountDownLatch(1);
		CountDownLatch exitBarrier = new CountDownLatch(PARTIES);
		
		for (int i = 0 ; i < PARTIES ; i++) {
			Runnable task = new Worker(entryBarrier, exitBarrier);
			new Thread(task).start();
		}
		
		System.out.println("Main thread: workers are ready, waiting before release...");
		
		Thread.sleep(1000);
		
		entryBarrier.countDown();
		System.out.println("Main thread: work release, waiting for results...");
		
		exitBarrier.await();
		System.out.println("Main thread: all workers finished.");
	}
}
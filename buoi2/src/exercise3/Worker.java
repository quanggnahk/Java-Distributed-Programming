package exercise3;

import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {
	private final CountDownLatch entryBarrier;
	private final CountDownLatch exitBarrier;
	public Worker(CountDownLatch entryBarrier, CountDownLatch exitBarrier) {
		super();
		this.entryBarrier = entryBarrier;
		this.exitBarrier = exitBarrier;
	}
	
	private void doWork() {
		System.out.printf("Worker {%s} started\n", Thread.currentThread().getName());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void run() {
		try {
			entryBarrier.await();
			doWork();
			exitBarrier.countDown();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
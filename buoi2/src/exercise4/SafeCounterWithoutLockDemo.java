package exercise4;

public class SafeCounterWithoutLockDemo {
	public static void main(String[] args) throws InterruptedException{
		SafeCounterWithoutLock counter = new SafeCounterWithoutLock();
		
		int NO_THREADS = 10;
		int NO_THREAD_PER_THREAD = 1000;
		Thread[] threads = new Thread[NO_THREADS];
		for (int i = 0 ; i< threads.length ; i++) {
			threads[i] = new Thread(() -> {
				for (int j = 0 ; j < NO_THREAD_PER_THREAD ; j++) {
					counter.increment();
				}
			});
			threads[i].start();
		}
		for (Thread thread : threads) {
			thread.join();
		}
		System.out.println("Counter: " + counter.getValue());
		System.out.println("Expected Counter: " + (NO_THREADS * NO_THREAD_PER_THREAD));
	}
}
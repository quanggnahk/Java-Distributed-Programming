package exercise4;

import java.util.concurrent.atomic.AtomicInteger;

public class SafeCounterWithoutLock {
	private final AtomicInteger counter = new AtomicInteger(0);
	public void increment() {
		counter.incrementAndGet();
	}
	public int getValue() {
		return counter.get();
	}
}
package exercise2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MyQueue_Fixed {
	int n;
	boolean valueSet = false;
	
	synchronized int get() {
		if (!valueSet)
			try { wait(); } catch (InterruptedException e) {}
		System.out.println("Got: " + n);
		try { Thread.sleep(300); } catch (Exception x) {}
		valueSet = false;
		notify();
		return n;
	}
	
	synchronized void put(int n) {
		if (valueSet)
			try { wait(); } catch (InterruptedException e) {}
		this.n = n;
		valueSet = true;
		System.out.println("Put: " + n);
		try { Thread.sleep(500);} catch (Exception x) {}
		notify();
	}
}

class Producer_Fixed implements Runnable {
	MyQueue_Fixed q;
	
	Producer_Fixed(MyQueue_Fixed q) {
		this.q = q;
		new Thread(this, "Producer").start();
	}
	
	public void run() {
		int i = 0;
		while (true) { q.put(i++);}
	}
}

class Consumer_Fixed implements Runnable {
	MyQueue_Fixed q;
	
	Consumer_Fixed(MyQueue_Fixed q) {
		this.q = q;
		new Thread(this, "Consumer").start();
	}
	
	public void run() {
		while (true) { q.get();}
	}
}

public class Producer_Consumer_Demo_Fixed {
	public static void main(String[] args) {
		System.out.println("Press Ctrl+C to stop.");
		ExecutorService service = Executors.newFixedThreadPool(2);
		
		MyQueue_Fixed q = new MyQueue_Fixed();
		
		service.execute(new Producer_Fixed(q));
		service.execute(new Consumer_Fixed(q));
	}
}
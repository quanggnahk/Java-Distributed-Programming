package exercise2;

class MyQueue {
	int n;
	
	synchronized int get() {
		System.out.println("Got: " + n);
		return n;
	}
	
	synchronized void put(int n) {
		this.n = n;
		System.out.println("Put: " + n);
	}
}

class Producer implements Runnable {
	MyQueue q;
	
	Producer(MyQueue q) {
		this.q = q;
		new Thread(this, "Producer").start();
	}
	
	public void run() {
		int i = 0;
		while (true) { q.put(i++);}
	}
}

class Consumer implements Runnable {
	MyQueue q;
	
	Consumer(MyQueue q) {
		this.q = q;
		new Thread(this, "Consumer").start();
	}
	
	public void run() {
		while (true) { q.get();}
	}
}

public class Producer_Consumer_Demo {
	public static void main(String[] args) {
		MyQueue q = new MyQueue();
		new Producer(q);
		new Consumer(q);
	}
}
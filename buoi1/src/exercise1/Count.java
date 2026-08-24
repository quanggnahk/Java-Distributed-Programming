package exercise1;

public class Count implements Runnable{
	private int count;
	
	public Count(int count) {
		this.count = count;
	}
	
	@Override
	public void run() {
		for (int i = 1 ; i <= count ; i++) {
			System.out.println(i);
		}
	}
	
	public static void main(String[] args) {
		Runnable r = new Count(10);
		Thread t = new Thread(r);
		t.start();
	}
}
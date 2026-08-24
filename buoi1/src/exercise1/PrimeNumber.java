package exercise1;

public class PrimeNumber implements Runnable{
	private int number;
	private int t = 0;
	

	public PrimeNumber(int number) {
		this.number = number;
	}
	
	@Override
	public void run() {
		if (number <= 1) {
			System.out.println(number + " is not a prime number.");
		} else {
			for (int i = 2 ; i < number ; i++) {
				if (number % i == 0) {
					t++;
				}
			}
			if (t != 0) {
				System.out.println(number + " is not a prime number.");
			} else {
				System.out.println(number + " is a prime number.");
			}
		}
	}

	public static void main(String[] args) {
		Runnable r = new PrimeNumber(2321);
		Thread t = new Thread(r);
		t.start();
	}
}
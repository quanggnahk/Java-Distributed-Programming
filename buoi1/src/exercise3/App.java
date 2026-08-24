package exercise3;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App {
	public static void main(String[] args) {
		Account account = new Account("ACC001", "Account 001", 100.D);
		
		Callable<Double> widthdrawTask = () -> {
			return account.widthdraw(1.0D);
		};
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		List<Future<Double>> futures = new ArrayList<Future<Double>>();
		
		for (int i = 0 ; i < 100 ; i++) {
			futures.add(executorService.submit(widthdrawTask));
		}
		
		executorService.shutdown();
		
		double total = 0.0D;
		
		for (Future<Double> future:futures) {
			try {
				total += future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Total widthdraw: " + total);
		System.out.println("Account balance: " + account.getAccountBalance());
	}
}
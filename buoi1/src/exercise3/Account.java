package exercise3;

public class Account {
	private String accountNumber;
	private String accountname;
	private Double accountBalance;
	
	public Account(String accountNumber, String accountname, Double accountBalance) {
		this.accountNumber = accountNumber;
		this.accountname = accountname;
		this.accountBalance = accountBalance;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getAccountname() {
		return accountname;
	}
	
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	
	public Double getAccountBalance() {
		return accountBalance;
	}
	
	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	synchronized void deposit(Double amount) {
		accountBalance += amount;
	}
	
	synchronized Double widthdraw(Double amount) {
		if (accountBalance < amount) {
			System.out.println("Your balance is not enough");
		} else {
			accountBalance -= amount;
		}
		return accountBalance;
	}
}
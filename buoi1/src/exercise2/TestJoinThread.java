package exercise2;

public class TestJoinThread {
	public static void main(String[] args) throws Exception{
		new Thread(new YourTask()).start();
	}
}
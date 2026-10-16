package exercise1;

public class Employee {
	@Override
	public String toString() {
		return "Person [id=" + id + 
				", name=" + name + 
				", salary=" + salary + "]";
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	private long id; 
	 private String name; 
	 private double salary;
	 public Employee() {
		super();
	 }
	 public Employee(long id, String name, double salary) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
	 }
}
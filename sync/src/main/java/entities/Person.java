package entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Person {
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public LocalDate getBirthday() {
		return birthday;
	}
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	private String name;
	private int age;
	private LocalDate birthday;
	private LocalDateTime createdAt;
	public Person() {
		super();
	}
	public Person(String name, int age, LocalDate birthday, LocalDateTime createdAt) {
		super();
		this.name = name;
		this.age = age;
		this.birthday = birthday;
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "Person{" + 
				"name = " + name + '\'' +
				", age = " + age + '\'' +
				", birthday = " + birthday + '\'' +
				", createAt = " + createdAt + '\'' +
				'}';
	}
}
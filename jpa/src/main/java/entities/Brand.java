package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "brands")
public class Brand {

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private int id;

    @Column(name = "brand_name", nullable = false)
    private String name;

    public Brand() {}
}
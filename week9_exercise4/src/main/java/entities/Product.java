package entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Products")
public class Product implements Serializable {
    public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

    @Id
    private String productID;
    private String description;
    private double price;

    public Product() {}

    public Product(String productID, String description, double price) {
        this.productID = productID;
        this.description = description;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" + "id='" + productID + '\'' + ", desc='" + description + '\'' + ", price=" + price + '}';
    }
}
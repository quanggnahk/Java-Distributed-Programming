package entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
public class OrderDetail implements Serializable {
    public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productID")
    private Product product;

    public OrderDetail() {}

    public OrderDetail(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Tính thành tiền = số lượng * đơn giá sản phẩm[cite: 12]
    public double calcTotalPrice() {
        if (product != null) {
            return this.quantity * product.getPrice();
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "product=" + product.getDescription() + ", quantity=" + quantity + ", total=" + calcTotalPrice() + '}';
    }
}
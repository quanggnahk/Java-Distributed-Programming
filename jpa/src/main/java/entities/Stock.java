package entities;

import jakarta.persistence.*;

@Entity
@Table(name = "stocks")
public class Stock {

    public StockId getId() {
		return id;
	}

	public void setId(StockId id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@EmbeddedId
    private StockId id;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @MapsId("storeId")
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    public Stock() {}
}
package entities;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class StockId implements Serializable {

    public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	@Column(name = "store_id")
    private int storeId;

    @Column(name = "product_id")
    private int productId;

    public StockId() {}

    public StockId(int storeId, int productId) {
        this.storeId = storeId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockId stockId = (StockId) o;
        return storeId == stockId.storeId && productId == stockId.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, productId);
    }
}
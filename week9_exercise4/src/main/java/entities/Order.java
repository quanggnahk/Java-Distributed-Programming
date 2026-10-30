package entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Orders")
public class Order implements Serializable {
    public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Set<OrderDetail> getLineItems() {
		return lineItems;
	}

	public void setLineItems(Set<OrderDetail> lineItems) {
		this.lineItems = lineItems;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = 1L;

    @Id
    private int orderID;
    
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    // Ánh xạ quan hệ Composition bằng ElementCollection
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Order_Details", joinColumns = @JoinColumn(name = "orderID"))
    private Set<OrderDetail> lineItems = new HashSet<>();

    public Order() {}

    public Order(int orderID, Date orderDate) {
        this.orderID = orderID;
        this.orderDate = orderDate;
    }

    // Phương thức thêm một sản phẩm p với số lượng q vào hóa đơn[cite: 12]
    public void addLineItem(Product p, int q) {
        OrderDetail detail = new OrderDetail(p, q);
        this.lineItems.add(detail);
    }

    // Tính tổng tiền hóa đơn = tổng các thành tiền[cite: 12]
    public double calcTotalCharge() {
        double total = 0;
        for (OrderDetail item : lineItems) {
            total += item.calcTotalPrice();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + orderID + ", date=" + orderDate + ", totalCharge=" + calcTotalCharge() + '}';
    }
}
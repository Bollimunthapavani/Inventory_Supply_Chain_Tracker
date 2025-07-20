import java.io.Serializable;

public class Product implements Serializable {
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private String supplierUsername;
    private String location;

    public Product(String productId, String name, int qty, double price, String supplierUsername, String location) {
        this.productId = productId;
        this.productName = name;
        this.quantity = qty;
        this.price = price;
        this.supplierUsername = supplierUsername;
        this.location = location;
    }

    public String getLocation() { return location; }
    public String getSupplierUsername() { return supplierUsername; }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return productId + " | " + productName + " | Qty: " + quantity + " | ₹" + price;
    }
}
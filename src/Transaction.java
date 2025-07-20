import java.io.Serializable;

public class Transaction implements Serializable {
    private String transactionId;
    private String productId;
    private int quantity;
    private String user;
    private String location;
    private double totalPrice;
    private long timestamp;

    public Transaction(String tid, String pid, int qty, String user, String location, double totalPrice) {
        this.transactionId = tid;
        this.productId = pid;
        this.quantity = qty;
        this.user = user;
        this.location = location;
        this.totalPrice = totalPrice;
        this.timestamp = System.currentTimeMillis();
    }
    public String getUser() { return user; }

    @Override
    public String toString() {
        return transactionId + " | Product: " + productId + " | Qty: " + quantity + " | User: " + user;
    }
}

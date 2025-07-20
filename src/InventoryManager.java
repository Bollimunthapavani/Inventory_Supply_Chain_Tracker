import java.io.Serializable;
import java.util.*;

public class InventoryManager implements Serializable {
    private Map<String, Product> products = new HashMap<>();
    private List<Transaction> transactions = new ArrayList<>();

    public void addProduct(Product p) {
        products.put(p.getProductId(), p);
    }

    public void deleteProduct(String productId) {
        if (products.remove(productId) != null) {
            System.out.println("✅ Product deleted.");
        } else {
            System.out.println("❌ Product not found.");
        }
    }


    public Product getProduct(String productId) {
        return products.get(productId);
    }

    public void updateQuantity(String productId, int change) {
        Product p = products.get(productId);
        if (p != null) {
            p.setQuantity(p.getQuantity() + change);
        }
    }

    public void recordTransaction(Transaction t) {
        transactions.add(t);
    }

    public void displayUserTransactions(String user) {
        boolean found = false;
        for (Transaction t : transactions) {
            if (t.getUser().equalsIgnoreCase(user)) {
                System.out.println(t);
                found = true;
            }
        }
        if (!found) {
            System.out.println("📭 No transactions found for user: " + user);
        }
    }
    public Collection<Product> getAllProducts() {
        return products.values();
    }
    public void displayProducts() {
        for (Product p : products.values()) {
            String stockStatus = (p.getQuantity() <= 0) ? "❌ Out of Stock" : "✅ In Stock";
            System.out.println("➤ " + p + " [" + stockStatus + "]");
        }
    }

}
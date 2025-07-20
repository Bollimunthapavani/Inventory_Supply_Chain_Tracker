
import java.io.*;
import java.util.*;
public class InventoryApp {
    private static InventoryManager inventoryManager;
    private static Map<String, String> supplierAccounts;
    private static ArrayList<Supplier> suppliers = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final Map<String, String> managerAccounts = Map.of("admin", "admin123");
    private static final String SUPPLIER_CRED_FILE = "suppliers.csv";
    private static final String USER_CRED_FILE = "users.csv";
    private static Map<String, String> userAccounts = new HashMap<>();
    private static String currentUser = "";
    private static ArrayList<CartItem> cart = new ArrayList<>();
    private static String currentUserLocation = "";
    public static void main(String[] args) {
        supplierAccounts = CredentialManager.loadCredentials(SUPPLIER_CRED_FILE);
        try (BufferedReader reader = new BufferedReader(new FileReader("suppliers.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0];
                    Supplier supplier = new Supplier(username);
                    suppliers.add(supplier);
                }
            }
        } catch (IOException e) {

            System.out.println("⚠ Couldn't load suppliers.");
        }

        userAccounts = CredentialManager.loadCredentials(USER_CRED_FILE);
        try {
            inventoryManager = (InventoryManager) DataPersistenceManager.loadData("inventory.dat");
        } catch (Exception e) {
            inventoryManager = new InventoryManager();
        }
        try {
            List<Product> productList = DataPersistenceManager.loadProductsFromCSV("products.csv");
            for (Product p : productList) {
                inventoryManager.addProduct(p);
            }
        } catch (IOException e) {
            System.out.println("⚠ Couldn't load products.");
        }

        while (true) {
            System.out.println("\n📋 ==== Inventory Tracker ====");
            System.out.println("1️⃣ Manager\n2️⃣ Supplier\n3️⃣ User\n4️⃣ Exit");
            System.out.println("Enter your Choice: ");
            int choice = readInt();
            switch (choice) {
                case 1 -> { if (authenticate(managerAccounts)) managerMenu(); }
                case 2 -> { if (authenticate(supplierAccounts)) supplierMenu(); }
                case 3 -> userAuthMenu();
                case 4 -> {
                    saveAll();
                    System.out.println("✅ Exiting...");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice.");
            }
        }
    }
    private static void managerMenu() {
        while (true) {
            System.out.println("\n👨‍💼 --- Manager Menu ---");
            System.out.println("1. ➕ Add Supplier\n2. 🗑 Remove Supplier\n3. 📜 List of Suppliers\n4. 📦 View Inventory\n5. 🗑 Delete Product\n6. 📄 View Transaction Log\n7. 💾 Save & Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Supplier Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Choose Username for Supplier: ");
                    String username = scanner.nextLine();
                    System.out.print("Choose Password: ");
                    String password = scanner.nextLine();

                    if (supplierAccounts.containsKey(username)) {
                        System.out.println("❌ Supplier already exists!");
                        break;
                    }

                    supplierAccounts.put(username, password);
                    CredentialManager.appendCredential(SUPPLIER_CRED_FILE, username, password);
                    suppliers.add(new Supplier(name));
                    System.out.println("✅ Supplier added.");
                }
                case 2 -> {
                    System.out.print("Enter Supplier Username to remove: ");
                    String username = scanner.nextLine();
                    if (supplierAccounts.remove(username) != null) {
                        suppliers.removeIf(s -> s.getName().equalsIgnoreCase(username));
                        DataPersistenceManager.saveSuppliersToCSV("suppliers.csv", suppliers);
                        CredentialManager.rewriteCredentials(SUPPLIER_CRED_FILE, supplierAccounts);
                        System.out.println("✅ Supplier removed.");
                    } else {
                        System.out.println("❌ Supplier not found.");
                    }
                }
                case 3 -> {
                    if (suppliers.isEmpty()) {
                        System.out.println("⚠️ No suppliers found.");
                    } else {
                        System.out.println("📜 Suppliers:");
                        for (Supplier s : suppliers) {
                            System.out.println("➤ " + s.getName());
                        }
                    }
                }
                case 4 -> inventoryManager.displayProducts();
                case 5 -> {
                    System.out.print("Enter Product ID to delete: ");
                    String pid = scanner.nextLine();
                    inventoryManager.deleteProduct(pid);
                }
                case 6 -> LogReader.displayLogs();
                case 7 -> {
                    saveAll();
                    return;
                }
                default -> System.out.println("❌ Invalid option.");
            }
        }
    }

    private static void userAuthMenu() {
        while (true) {
            System.out.println("\n👤 --- User Authentication ---");
            System.out.println("1. Login\n2. Register\n3. Back to Main Menu");
            int choice = readInt();
            switch (choice) {
                case 1 -> {
                    if (userLogin()) {
                        userMenu();
                        return;
                    }
                }
                case 2 -> userRegister();
                case 3 -> { return; }
                default -> System.out.println("❌ Invalid option.");
            }
        }
    }

    private static boolean authenticate(Map<String, String> accounts) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (accounts.containsKey(username) && accounts.get(username).equals(password)) {
            System.out.println("✅ Login successful.");
            currentUser = username;
            return true;
        } else {
            System.out.println("❌ Invalid credentials.");
            return false;
        }
    }

    private static boolean userLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter your city/location: ");
        currentUserLocation = scanner.nextLine();
        if (userAccounts.containsKey(username) && userAccounts.get(username).equals(password)) {
            currentUser = username;
            System.out.println("✅ Login successful!");
            return true;
        } else {
            System.out.println("❌ Invalid username or password.");
            return false;
        }
    }

    private static void userRegister() {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            System.out.println("❌ Username cannot be empty.");
            return;
        }
        if (userAccounts.containsKey(username)) {
            System.out.println("❌ Username already exists.");
            return;
        }
        System.out.print("Choose a password: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.out.println("❌ Password cannot be empty.");
            return;
        }
        if (username.contains(",") || password.contains(",")) {
            System.out.println("❌ Username and password cannot contain commas.");
            return;
        }
        userAccounts.put(username, password);
        CredentialManager.appendCredential(USER_CRED_FILE, username, password);
        System.out.println("✅ Registration successful! You can now login.");
    }
    private static void supplierMenu() {

        while (true) {
            System.out.println("\n\uD83D\uDE9A --- Supplier Menu ---");
            System.out.println("1. ➕ Add Product");
            System.out.println("2. 🗑 Delete Product");
            System.out.println("3. 📜 View Inventory");
            System.out.println("4. 🚪 Logout");
            int choice = readInt();
            switch (choice) {
                case 1 -> {
                    String pid = "P" + (new Random().nextInt(9001) + 999);
                    System.out.print("Product Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Quantity: ");
                    int qty = readInt();
                    System.out.print("Price: ");
                    double price = readDouble();
                    Product product = new Product(pid, name, qty, price,currentUser,currentUserLocation);
                    inventoryManager.addProduct(product);
                    System.out.println("\u2705 Product added with ID: " + pid);
                }
                case 2 -> {
                    System.out.print("Enter Product ID to delete: ");
                    String pid = scanner.nextLine();
                    inventoryManager.deleteProduct(pid);
                }
                case 3 ->
                {
                    for (Product p : inventoryManager.getAllProducts()) {
                        if (p.getSupplierUsername().equals(currentUser)) {
                            System.out.println("➤ " + p);
                        }
                    }
                }
                case 4 -> {
                    saveAll();
                    return;
                }
                default -> System.out.println("\u274C Invalid option.");
            }
        }
    }

    private static void userMenu() {
        while (true) {
            System.out.println("\n🙋 --- User Menu ---");
            System.out.println("1. 📦 View Inventory\n2. ➕ Add to Cart\n3. 🛍 View Cart\n4. ❌ Remove from Cart\n5. ✅ Place Order\n6. 📜 My Transactions\n7. 🚪 Exit to Main Menu");
            int choice = readInt();
            switch (choice) {
                case 1 -> inventoryManager.displayProducts();
                case 2 -> {
                    System.out.print("Enter Product ID to add to cart: ");
                    String pid = scanner.nextLine();
                    Product product = inventoryManager.getProduct(pid);
                    if (product != null && product.getQuantity() > 0) {
                        System.out.print("Enter quantity to add: ");
                        int qty = readInt();
                        if (qty <= 0 || qty > product.getQuantity()) {
                            System.out.println("❌ Invalid quantity.");
                            break;
                        }
                        cart.add(new CartItem(product, qty));
                        System.out.println("✅ Product added to cart.");
                    } else {
                        System.out.println("❌ Product not found or out of stock.");
                    }
                }
                case 3 -> {
                    if (cart.isEmpty()) {
                        System.out.println("🛒 Cart is empty.");
                    } else {
                        System.out.println("🛒 Your Cart:");
                        for (CartItem item : cart) {
                            Product p = item.getProduct();
                            int qty = item.getQuantity();
                            double total = item.getTotalPrice();
                            System.out.println("➤ " + p.getProductName() + " (Qty: " + qty + ", Unit Price: ₹" + p.getPrice() + ", Total: ₹" + total + ")");
                        }
                    }
                }
                case 4 -> {
                    System.out.print("Enter Product ID to remove from cart: ");
                    String pid = scanner.nextLine();
                    boolean removed = cart.removeIf(item -> item.getProduct().getProductId().equals(pid));
                    if (removed) {
                        System.out.println("✅ Item removed from cart.");
                    } else {
                        System.out.println("❌ Item not found in cart.");
                    }
                }
                case 5 -> {
                    if (cart.isEmpty()) {
                        System.out.println("🛒 Cart is empty.");
                    } else {
                        double totalOrderPrice = 0;
                        for (CartItem item : cart) {
                            Product p = item.getProduct();
                            int qty = item.getQuantity();
                            Product latest = inventoryManager.getProduct(p.getProductId());

                            if (latest.getQuantity() < qty) {
                                System.out.println("❌ Not enough stock for " + p.getProductName() + ". Skipping.");
                                continue;
                            }
                            inventoryManager.updateQuantity(p.getProductId(), -qty);
                            String tid = "TXN" + System.currentTimeMillis();
                            double price = p.getPrice();
                            double total = price * qty;

                            inventoryManager.recordTransaction(new Transaction(tid, p.getProductId(), qty, currentUser, currentUserLocation, total));
                            LogWriter.log(currentUser, p.getProductId(), qty, price);
                            totalOrderPrice += total;

                            DeliveryTracker.simulateDelivery(p.getLocation(), currentUserLocation);
                        }
                        cart.clear();
                        System.out.println("✅ Order placed successfully!");
                        System.out.println("🧾 Total Amount: ₹" + totalOrderPrice);
                    }
                }
                case 6 -> inventoryManager.displayUserTransactions(currentUser);
                case 7 -> {
                    saveAll();
                    return;
                }
                default -> System.out.println("❌ Invalid option.");
            }
        }
    }

    private static void saveAll() {
        try {
            DataPersistenceManager.saveData(inventoryManager, "inventory.dat");
            CredentialManager.saveCredentials(SUPPLIER_CRED_FILE, supplierAccounts);
            CredentialManager.saveCredentials(USER_CRED_FILE, userAccounts);
        } catch (Exception e) {
            System.out.println("❌ Error saving data.");
        }
        try {
            DataPersistenceManager.saveProductsToCSV("products.csv", inventoryManager.getAllProducts());
        } catch (IOException e) {
            System.out.println("❌ Failed to save products: " + e.getMessage());
        }
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.print("Enter a number: ");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (Exception e) {
                System.out.print("Enter a decimal number: ");
            }
        }
    }
}

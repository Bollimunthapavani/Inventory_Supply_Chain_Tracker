import java.io.*;
import java.util.*;
public class DataPersistenceManager {
    public static void saveData(Object obj, String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(obj);
        }
    }
    public static void saveSuppliersToCSV(String fileName, List<Supplier> suppliers) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("suppliers.csv"))) {
            for (Supplier supplier : suppliers) {
                bw.write(supplier.getName());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving suppliers: " + e.getMessage());
        }
    }


    public static Object loadData(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return in.readObject();
        }
    }
    public static void saveProductsToCSV(String fileName, Collection<Product> products) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Product p : products) {
                writer.write(p.getProductId() + "," +
                        p.getProductName() + "," +
                        p.getQuantity() + "," +
                        p.getPrice() + "," +
                        p.getSupplierUsername() + "," +
                        p.getLocation());
                writer.newLine();
            }
        }
    }

    public static List<Product> loadProductsFromCSV(String fileName) throws IOException {
        List<Product> productList = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) return productList;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String id = parts[0];
                    String name = parts[1];
                    int qty = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);
                    String supplier = parts[4];
                    String location = parts[5];

                    productList.add(new Product(id, name, qty, price, supplier, location));
                }
            }
        }

        return productList;
    }


}

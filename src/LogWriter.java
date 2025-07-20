import java.io.*;

public class LogWriter {
    private static final String LOG_FILE = "transactions.csv";

    public static void log(String user, String productId, int quantity, double price) {
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.println(user + "," + productId + "," + quantity + "," + price + "," + System.currentTimeMillis());
        } catch (IOException e) {
            System.out.println("❌ Failed to write to log file.");
        }
    }
}
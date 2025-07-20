import java.io.*;

public class LogReader {
    public static void displayLogs() {
        File file = new File("transactions.csv");
        if (!file.exists()) {
            System.out.println("📭 No log file found.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println("🧾 Transaction Log:");
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                System.out.println("➤ User: " + data[0] +
                        ", Product ID: " + data[1] +
                        ", Qty: " + data[2] +
                        ", Price: ₹" + data[3] +
                        ", Timestamp: " + new java.util.Date(Long.parseLong(data[4])));
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading log.");
        }
    }
}
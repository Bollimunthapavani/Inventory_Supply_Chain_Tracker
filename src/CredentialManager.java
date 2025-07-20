import java.io.*;
import java.util.*;

public class CredentialManager {
    public static Map<String, String> loadCredentials(String fileName) {
        Map<String, String> map = new HashMap<>();
        File file = new File(fileName);
        if (!file.exists()) return map;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    map.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading credentials from " + fileName);
        }
        return map;
    }

    public static void saveCredentials(String fileName, Map<String, String> credentials) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, String> entry : credentials.entrySet()) {
                out.println(entry.getKey() + "," + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("❌ Error saving credentials to " + fileName);
        }
    }
    public static void rewriteCredentials(String fileName, Map<String, String> creds) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("suppliers.csv"))) {
            for (Map.Entry<String, String> entry : creds.entrySet()) {
                bw.write(entry.getKey() + "," + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error updating credential file: " + e.getMessage());
        }
    }


    public static void appendCredential(String fileName, String username, String password) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName, true))) {
            out.println(username + "," + password);
        } catch (IOException e) {
            System.out.println("❌ Failed to append credentials.");
        }
    }
}

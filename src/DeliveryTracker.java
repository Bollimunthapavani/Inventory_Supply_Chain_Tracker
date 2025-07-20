public class DeliveryTracker {
    public static void simulateDelivery(String from, String to) {
        System.out.println("🚚 Product is leaving from " + from + " and will arrive at " + to + ".");
        System.out.println("📍 Tracking route...");
        try {
            Thread.sleep(1000); System.out.print("📦 Packed... ");
            Thread.sleep(1000); System.out.print("🛫 In Transit... ");
            Thread.sleep(1000); System.out.println("✅ Delivered!");
        } catch (InterruptedException e) {
            System.out.println("⚠ Delivery tracking interrupted.");
        }
    }
}



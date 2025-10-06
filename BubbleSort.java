import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

class Order {
    String order_id;
    long timestamp; 

    Order(String order_id, long timestamp) {
        this.order_id = order_id;
        this.timestamp = timestamp;
    }
}

public class BubbleSort {
    private static final int NUM_ORDERS = 10000;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static void generateSampleOrders(Order[] orders) {
        ZonedDateTime baseTime = ZonedDateTime.of(2025, 6, 24, 12, 0, 0, 0, ZoneOffset.UTC);
        long baseTimestamp = baseTime.toInstant().toEpochMilli();
        Random rand = new Random();

        for (int i = 0; i < NUM_ORDERS; i++) {
            long randomMinutes = rand.nextInt(100000); // up to ~70 days
            long timestamp = baseTimestamp + (randomMinutes * 60 * 1000); // Convert to milliseconds
            orders[i] = new Order("ORD" + (i + 1), timestamp);
        }
    }

    public static void bubbleSort(Order[] orders) {
        for (int i = 0; i < NUM_ORDERS - 1; i++) {
            for (int j = 0; j < NUM_ORDERS - i - 1; j++) {
                if (orders[j].timestamp > orders[j + 1].timestamp) {
                    Order temp = orders[j];
                    orders[j] = orders[j + 1];
                    orders[j + 1] = temp;
                }
            }
        }
    }

    public static void saveOrdersToFile(Order[] orders, String filename) {
        try (FileWriter fw = new FileWriter(filename)) {
            for (int i = 0; i < NUM_ORDERS; i++) {
                Instant instant = Instant.ofEpochMilli(orders[i].timestamp);
                String timeStr = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC).format(formatter);
                fw.write("Order ID: " + orders[i].order_id + ", Timestamp: " + timeStr + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error opening file!");
        }
    }

    public static void main(String[] args) {
        Random rand = new Random();
        Order[] orders = new Order[NUM_ORDERS];

        System.out.println("Generating orders...");
        generateSampleOrders(orders);

        System.out.println("Sorting orders by timestamp using Bubble Sort...");
        long start = System.currentTimeMillis();
        bubbleSort(orders);
        long end = System.currentTimeMillis();

        double timeTaken = (end - start) / 1000.0;
        System.out.println("Done! Sorted " + NUM_ORDERS + " orders in " + String.format("%.2f", timeTaken) + " seconds.");

        saveOrdersToFile(orders, "sorted_orders_bubble.txt");
        System.out.println("Sorted orders saved in 'sorted_orders_bubble.txt'.");
    }
}
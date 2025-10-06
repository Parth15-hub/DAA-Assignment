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

public class MergeSort {
    private static final int NUM_ORDERS = 10000;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static void generateSampleOrders(Order[] orders) {
        ZonedDateTime baseTime = ZonedDateTime.of(2025, 6, 24, 12, 0, 0, 0, ZoneOffset.UTC);
        long baseTimestamp = baseTime.toInstant().toEpochMilli();
        Random rand = new Random();

        for (int i = 0; i < NUM_ORDERS; i++) {
            long randomMinutes = rand.nextInt(100000);
            long timestamp = baseTimestamp + (randomMinutes * 60 * 1000);
            orders[i] = new Order("ORD" + (i + 1), timestamp);
        }
    }

    public static void mergeSort(Order[] orders, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(orders, left, mid);
            mergeSort(orders, mid + 1, right);
            merge(orders, left, mid, right);
        }
    }

    public static void merge(Order[] orders, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        Order[] leftArray = new Order[n1];
        Order[] rightArray = new Order[n2];

        for (int i = 0; i < n1; i++)
            leftArray[i] = orders[left + i];
        for (int j = 0; j < n2; j++)
            rightArray[j] = orders[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArray[i].timestamp <= rightArray[j].timestamp) {
                orders[k] = leftArray[i];
                i++;
            } else {
                orders[k] = rightArray[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            orders[k] = leftArray[i];
            i++;
            k++;
        }

        while (j < n2) {
            orders[k] = rightArray[j];
            j++;
            k++;
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

        System.out.println("Sorting orders by timestamp using Merge Sort...");
        long start = System.currentTimeMillis();
        mergeSort(orders, 0, NUM_ORDERS - 1);
        long end = System.currentTimeMillis();

        double timeTaken = (end - start) / 1000.0;
        System.out.println("Done! Sorted " + NUM_ORDERS + " orders in " + String.format("%.2f", timeTaken) + " seconds.");

        saveOrdersToFile(orders, "sorted_orders_merge.txt");
        System.out.println("Sorted orders saved in 'sorted_orders_merge.txt'.");
    }
}
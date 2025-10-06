import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

class Item {
    String name;
    double weight, value;
    int priority;

    Item(String name, double weight, double value, int priority) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.priority = priority;
    }
}

public class Knapsack01 {
    private static class ItemComparator implements Comparator<Item> {
        public int compare(Item a, Item b) {
            if (a.priority == b.priority)
                return Double.compare(b.value / b.weight, a.value / a.weight);
            return Integer.compare(a.priority, b.priority);
        }
    }

    public static void knapsack01(ArrayList<Item> items, double capacity) {
        Collections.sort(items, new ItemComparator());

        System.out.println("\nSorted Items (by Priority, then Value/Weight):");
        System.out.printf("%-15s %-8s %-8s %-9s %-14s%n", "Item", "Weight", "Value", "Priority", "Value/Weight");
        for (Item it : items) {
            System.out.printf("%-15s %-8.2f %-8.2f %-9d %-14.2f%n",
                it.name, it.weight, it.value, it.priority, it.value / it.weight);
        }

        int n = items.size();
        double[][] dp = new double[n + 1][(int) Math.ceil(capacity) + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                Item item = items.get(i - 1);
                if (item.weight <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][(int) (w - item.weight)] + item.value);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        double totalValue = dp[n][(int) capacity];
        double totalWeight = 0;
        ArrayList<String> selectedItems = new ArrayList<>();

        for (int i = n, w = (int) capacity; i > 0 && w >= 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                Item item = items.get(i - 1);
                selectedItems.add(item.name);
                totalWeight += item.weight;
                w -= (int) item.weight;
            }
        }

        System.out.println("\nItems selected for transport:");
        for (String item : selectedItems) {
            System.out.println("- " + item);
        }

        System.out.println("\n===== Final Report =====");
        System.out.printf("Total weight carried: %.2f kg%n", totalWeight);
        System.out.printf("Total utility value carried: %.2f units%n", totalValue);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Item> items = new ArrayList<>();

        System.out.println("Choose mode:");
        System.out.println("1. Demo Dataset");
        System.out.println("2. Enter Custom Items");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        if (choice == 1) {
            items.add(new Item("Medical Kits", 10, 100, 1));
            items.add(new Item("Food Packets", 20, 60, 3));
            items.add(new Item("Drinking Water", 30, 90, 2));
            items.add(new Item("Blankets", 15, 45, 3));
            items.add(new Item("Infant Formula", 5, 50, 1));
        } else {
            System.out.print("Enter number of items: ");
            int n = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            for (int i = 0; i < n; i++) {
                System.out.println("\nEnter details for item " + (i + 1) + ":");
                System.out.print("Name: ");
                String name = scanner.nextLine();
                System.out.print("Weight: ");
                double weight = scanner.nextDouble();
                System.out.print("Value: ");
                double value = scanner.nextDouble();
                System.out.print("Priority (1=highest): ");
                int priority = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                items.add(new Item(name, weight, value, priority));
            }
        }

        System.out.print("\nEnter maximum weight capacity of the boat (in kg): ");
        double capacity = scanner.nextDouble();

        knapsack01(items, capacity);
        scanner.close();
    }
}
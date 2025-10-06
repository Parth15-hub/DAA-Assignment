import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

class Item {
    String name;
    double weight, value;
    boolean divisible;
    int priority;

    Item(String name, double weight, double value, boolean divisible, int priority) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.divisible = divisible;
        this.priority = priority;
    }
}

public class FractionalKnapsack {
    private static class ItemComparator implements Comparator<Item> {
        public int compare(Item a, Item b) {
            if (a.priority == b.priority)
                return Double.compare(b.value / b.weight, a.value / a.weight);
            return Integer.compare(a.priority, b.priority);
        }
    }

    public static void fractionalKnapsack(ArrayList<Item> items, double capacity) {
        Collections.sort(items, new ItemComparator());

        System.out.println("\nSorted Items (by Priority, then Value/Weight):");
        System.out.printf("%-15s %-8s %-8s %-9s %-14s %s%n", "Item", "Weight", "Value", "Priority", "Value/Weight", "Type");
        for (Item it : items) {
            System.out.printf("%-15s %-8.2f %-8.2f %-9d %-14.2f %s%n",
                it.name, it.weight, it.value, it.priority, it.value / it.weight,
                it.divisible ? "Divisible" : "Indivisible");
        }

        double totalValue = 0, totalWeight = 0;
        System.out.println("\nItems selected for transport:");

        for (Item it : items) {
            if (capacity <= 0) break;

            if (it.weight <= capacity) {
                System.out.printf("- %s: %.2f kg, Utility = %.2f, Priority = %d, Type = %s%n",
                    it.name, it.weight, it.value, it.priority, it.divisible ? "Divisible" : "Indivisible");
                capacity -= it.weight;
                totalWeight += it.weight;
                totalValue += it.value;
            } else if (it.divisible) {
                double fraction = capacity / it.weight;
                System.out.printf("- %s: %.2f kg, Utility = %.2f, Priority = %d, Type = Divisible%n",
                    it.name, it.weight * fraction, it.value * fraction, it.priority);
                totalWeight += it.weight * fraction;
                totalValue += it.value * fraction;
                capacity = 0;
            }
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
            items.add(new Item("Medical Kits", 10, 100, false, 1));
            items.add(new Item("Food Packets", 20, 60, true, 3));
            items.add(new Item("Drinking Water", 30, 90, true, 2));
            items.add(new Item("Blankets", 15, 45, false, 3));
            items.add(new Item("Infant Formula", 5, 50, false, 1));
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
                System.out.print("Divisible? (1=Yes, 0=No): ");
                boolean divisible = scanner.nextInt() == 1;
                System.out.print("Priority (1=highest): ");
                int priority = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                items.add(new Item(name, weight, value, divisible, priority));
            }
        }

        System.out.print("\nEnter maximum weight capacity of the boat (in kg): ");
        double capacity = scanner.nextDouble();

        fractionalKnapsack(items, capacity);
        scanner.close();
    }
}
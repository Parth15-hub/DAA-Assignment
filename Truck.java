import java.util.*;

public class Truck {

    static class Item {
        int weight;
        int utility;
        Item(int w, int u) { weight = w; utility = u; }
    }

    // Returns max utility; fills selectedItems with chosen indices (0-based)
    static int knapsack(List<Item> items, int W, List<Integer> selectedItems) {
        int N = items.size();
        int[][] dp = new int[N + 1][W + 1];

        // Build DP table
        for (int i = 1; i <= N; i++) {
            Item it = items.get(i - 1);
            for (int w = 0; w <= W; w++) {
                if (it.weight <= w) {
                    int includeItem = dp[i - 1][w - it.weight] + it.utility;
                    dp[i][w] = Math.max(includeItem, dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        int remainingWeight = W;
        for (int i = N; i > 0; i--) {
            if (dp[i][remainingWeight] != dp[i - 1][remainingWeight]) {
                selectedItems.add(i - 1); // store index of item taken
                remainingWeight -= items.get(i - 1).weight;
            }
        }

        return dp[N][W];
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of items : ");
        int N = sc.nextInt();

        System.out.print("Enter the truck capacity : ");
        int W = sc.nextInt();

        List<Item> items = new ArrayList<>(N);

        System.out.println("Enter the weight and utility of each item:");
        for (int i = 0; i < N; i++) {
            System.out.print("Item " + (i + 1) + " - Weight: ");
            int w = sc.nextInt();
            System.out.print("Item " + (i + 1) + " - Utility: ");
            int u = sc.nextInt();
            items.add(new Item(w, u));
        }

        List<Integer> selectedItems = new ArrayList<>();
        int maxUtility = knapsack(items, W, selectedItems);

        System.out.println("Maximum utility that can be carried: " + maxUtility);

        System.out.println("Items chosen :");
        for (int idx : selectedItems) { 
            Item it = items.get(idx);
            System.out.println("Item " + (idx + 1) + " - Weight: " + it.weight + " Utility: " + it.utility);
        }
    }
}

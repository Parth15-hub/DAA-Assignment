import java.util.*;

public class Assignment6 {
    static class Result {
        int maxUtility;
        List<Integer> chosenItems;
        Result(int maxUtility, List<Integer> chosenItems) {
            this.maxUtility = maxUtility;
            this.chosenItems = chosenItems;
        }
    }

    static Result knapsackDP(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[][] dp = new int[n + 1][W + 1];
        for (int i = 1; i <= n; i++) {
            int wt = weights[i - 1];
            int val = values[i - 1];
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i - 1][w];
                if (wt <= w) dp[i][w] = Math.max(dp[i][w], val + dp[i - 1][w - wt]);
            }
        }
        List<Integer> chosen = new ArrayList<>();
        int w = W;
        for (int i = n; i >= 1; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                chosen.add(i - 1);
                w -= weights[i - 1];
            }
        }
        Collections.reverse(chosen);
        return new Result(dp[n][W], chosen);
    }

    static int[] applyPriority(int[] baseUtility, String[] itemType, double criticalBoostFactor) {
        int n = baseUtility.length;
        int[] adjusted = new int[n];
        for (int i = 0; i < n; i++) {
            if (itemType[i].equalsIgnoreCase("medical") || itemType[i].equalsIgnoreCase("perishable"))
                adjusted[i] = (int)Math.round(baseUtility[i] * criticalBoostFactor);
            else adjusted[i] = baseUtility[i];
        }
        return adjusted;
    }

    public static void main(String[] args) {
        int[] weights = {5, 7, 3, 9, 4, 6};
        int[] utility = {10, 13, 6, 18, 8, 11};
        String[] type = {"medical","food","blanket","equipment","perishable","tool"};
        int W = 15;

        Result plain = knapsackDP(weights, utility, W);
        System.out.println("=== PLAN A: Pure Utility Optimization ===");
        System.out.println("Max Utility: " + plain.maxUtility);
        System.out.println("Chosen item indices: " + plain.chosenItems);
        for (int idx : plain.chosenItems)
            System.out.println("  Item " + idx + " | weight=" + weights[idx] + " | utility=" + utility[idx] + " | type=" + type[idx]);

        double CRITICAL_BOOST = 1.5;
        int[] boostedUtility = applyPriority(utility, type, CRITICAL_BOOST);
        Result priority = knapsackDP(weights, boostedUtility, W);
        System.out.println("\n=== PLAN B: Priority-Aware (Medical / Perishable Boosted) ===");
        System.out.println("Max Adjusted Utility: " + priority.maxUtility);
        System.out.println("Chosen item indices: " + priority.chosenItems);
        for (int idx : priority.chosenItems)
            System.out.println("  Item " + idx + " | weight=" + weights[idx] + " | baseUtility=" + utility[idx] + " | boostedUtility=" + boostedUtility[idx] + " | type=" + type[idx]);
    }
}

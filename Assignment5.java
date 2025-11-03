import java.util.*;

public class Assignment5 {

    static class Edge {
        int to;
        int cost;
        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    static class Result {
        int totalCost;
        List<Integer> path;
        Result(int totalCost, List<Integer> path) {
            this.totalCost = totalCost;
            this.path = path;
        }
    }

    static Result shortestMultistagePath(
            Map<Integer, List<Edge>> graph,
            int[] stageOfNode,
            int totalStages,
            int sourceNode,
            int targetStage
    ) {
        int n = stageOfNode.length;
        int INF = 1_000_000_000;

        int[] minCostFromHere = new int[n];
        int[] nextChoice = new int[n];
        Arrays.fill(minCostFromHere, INF);
        Arrays.fill(nextChoice, -1);

        for (int node = 0; node < n; node++) {
            if (stageOfNode[node] == targetStage) {
                minCostFromHere[node] = 0;
                nextChoice[node] = -1;
            }
        }

        for (int stage = targetStage - 1; stage >= 0; stage--) {
            for (int node = 0; node < n; node++) {
                if (stageOfNode[node] == stage) {
                    List<Edge> edges = graph.getOrDefault(node, Collections.emptyList());
                    int bestCost = INF;
                    int bestNext = -1;
                    for (Edge e : edges) {
                        int candidateCost = e.cost + minCostFromHere[e.to];
                        if (candidateCost < bestCost) {
                            bestCost = candidateCost;
                            bestNext = e.to;
                        }
                    }
                    minCostFromHere[node] = bestCost;
                    nextChoice[node] = bestNext;
                }
            }
        }

        List<Integer> route = new ArrayList<>();
        int cur = sourceNode;
        route.add(cur);
        while (nextChoice[cur] != -1) {
            cur = nextChoice[cur];
            route.add(cur);
        }

        return new Result(minCostFromHere[sourceNode], route);
    }

    public static void main(String[] args) {
        int totalStages = 4;

        int W0 = 0;
        int W1 = 1;

        int H0 = 2;
        int H1 = 3;

        int D0 = 4;
        int D1 = 5;

        int C0 = 6;
        int C1 = 7;

        int nNodes = 8;

        int[] stageOfNode = new int[nNodes];
        stageOfNode[W0] = 0;
        stageOfNode[W1] = 0;
        stageOfNode[H0] = 1;
        stageOfNode[H1] = 1;
        stageOfNode[D0] = 2;
        stageOfNode[D1] = 2;
        stageOfNode[C0] = 3;
        stageOfNode[C1] = 3;

        Map<Integer, List<Edge>> graph = new HashMap<>();

        graph.put(W0, Arrays.asList(
                new Edge(H0, 4),
                new Edge(H1, 6)
        ));
        graph.put(W1, Arrays.asList(
                new Edge(H0, 5),
                new Edge(H1, 2)
        ));

        graph.put(H0, Arrays.asList(
                new Edge(D0, 7),
                new Edge(D1, 3)
        ));
        graph.put(H1, Arrays.asList(
                new Edge(D0, 4),
                new Edge(D1, 6)
        ));

        graph.put(D0, Arrays.asList(
                new Edge(C0, 5),
                new Edge(C1, 8)
        ));
        graph.put(D1, Arrays.asList(
                new Edge(C0, 2),
                new Edge(C1, 7)
        ));

        graph.put(C0, new ArrayList<>());
        graph.put(C1, new ArrayList<>());

        int sourceNode = W0;
        int lastStage = totalStages - 1;

        Result res = shortestMultistagePath(
                graph,
                stageOfNode,
                totalStages,
                sourceNode,
                lastStage
        );

        System.out.println("Minimum delivery cost: " + res.totalCost);
        System.out.println("Route (node ids by stage): " + res.path);

        Map<Integer,String> label = new HashMap<>();
        label.put(W0, "Warehouse_A");
        label.put(W1, "Warehouse_B");
        label.put(H0, "Hub_X");
        label.put(H1, "Hub_Y");
        label.put(D0, "Dispatch_P");
        label.put(D1, "Dispatch_Q");
        label.put(C0, "Customer_Alpha");
        label.put(C1, "Customer_Beta");

        System.out.println("Readable Route:");
        for (int node : res.path) {
            System.out.println(" -> " + label.get(node));
        }

        int trafficPenalty = 3;
        graph.get(H0).set(0, new Edge(D0, 7 + trafficPenalty));

        Result res2 = shortestMultistagePath(
                graph,
                stageOfNode,
                totalStages,
                sourceNode,
                lastStage
        );

        System.out.println("\nAfter traffic update:");
        System.out.println("New Minimum delivery cost: " + res2.totalCost);
        System.out.println("New Route (node ids by stage): " + res2.path);
        System.out.println("New Readable Route:");
        for (int node : res2.path) {
            System.out.println(" -> " + label.get(node));
        }
    }
}
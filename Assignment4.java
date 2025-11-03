import java.util.*;

class Edge {
    int to, weight;

    Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}

public class Assignment4 {
    public static void dijkstra(int source, ArrayList<ArrayList<Edge>> graph, int[] dist, int[] parent) {
        int V = graph.size();
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, source});

        while (!pq.isEmpty()) {
            int u = pq.poll()[1];
            int d = dist[u];

            for (Edge edge : graph.get(u)) {
                int v = edge.to;
                int w = edge.weight;

                if (dist[v] > d + w) {
                    dist[v] = d + w;
                    parent[v] = u;
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }
    }

    public static void printPath(int node, int[] parent) {
        if (node == -1) return;
        ArrayList<Integer> path = new ArrayList<>();
        while (node != -1) {
            path.add(node);
            node = parent[node];
        }
        Collections.reverse(path);

        System.out.print("Optimal Path: ");
        for (int i = 0; i < path.size(); i++) {
            System.out.print(path.get(i));
            if (i != path.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of intersections (vertices): ");
        int V = scanner.nextInt();
        System.out.print("Enter number of roads (edges): ");
        int E = scanner.nextInt();

        ArrayList<ArrayList<Edge>> graph = new ArrayList<>(V);
        for (int i = 0; i < V; i++) graph.add(new ArrayList<>());

        System.out.println("Enter edges (u v w):");
        for (int i = 0; i < E; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int w = scanner.nextInt();
            graph.get(u).add(new Edge(v, w));
            graph.get(v).add(new Edge(u, w)); // undirected
        }

        System.out.print("Enter ambulance start location (source): ");
        int source = scanner.nextInt();

        System.out.print("Enter number of hospitals: ");
        int H = scanner.nextInt();
        int[] hospitals = new int[H];
        System.out.println("Enter hospital nodes:");
        for (int i = 0; i < H; i++) {
            hospitals[i] = scanner.nextInt();
        }

        int[] dist = new int[V];
        int[] parent = new int[V];
        dijkstra(source, graph, dist, parent);

        int minTime = Integer.MAX_VALUE, nearestHospital = -1;
        for (int h : hospitals) {
            if (dist[h] < minTime) {
                minTime = dist[h];
                nearestHospital = h;
            }
        }

        if (nearestHospital == -1) {
            System.out.println("No hospital reachable.");
        } else {
            System.out.println("\nNearest hospital is at node " + nearestHospital 
                             + " with travel time " + minTime + " minutes.");
            printPath(nearestHospital, parent);
        }

        System.out.print("\nDo you want to update traffic conditions? (y/n): ");
        char choice = scanner.next().charAt(0);

        while (choice == 'y' || choice == 'Y') {
            System.out.print("Enter road (u v) and new travel time: ");
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            int newW = scanner.nextInt();

            for (Edge edge : graph.get(u)) {
                if (edge.to == v) edge.weight = newW;
            }
            for (Edge edge : graph.get(v)) {
                if (edge.to == u) edge.weight = newW;
            }

            dijkstra(source, graph, dist, parent);

            minTime = Integer.MAX_VALUE;
            nearestHospital = -1;
            for (int h : hospitals) {
                if (dist[h] < minTime) {
                    minTime = dist[h];
                    nearestHospital = h;
                }
            }

            if (nearestHospital == -1) {
                System.out.println("No hospital reachable after update.");
            } else {
                System.out.println("\nAfter traffic update, nearest hospital is at node " + nearestHospital 
                                 + " with travel time " + minTime + " minutes.");
                printPath(nearestHospital, parent);
            }

            System.out.print("\nDo you want to update traffic again? (y/n): ");
            choice = scanner.next().charAt(0);
        }

        scanner.close();
    }
}
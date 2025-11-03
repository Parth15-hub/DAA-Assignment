import java.util.*;

public class Assignment8 {
    private static final int INF = 1_000_000_000;

    static class Node implements Comparable<Node> {
        int[][] reducedMatrix;
        int costSoFar;
        int lowerBound;
        List<Integer> path;
        int currentCity;
        Set<Integer> unvisited;

        Node(int[][] reducedMatrix, int costSoFar, int lowerBound, List<Integer> path, int currentCity, Set<Integer> unvisited) {
            this.reducedMatrix = reducedMatrix;
            this.costSoFar = costSoFar;
            this.lowerBound = lowerBound;
            this.path = path;
            this.currentCity = currentCity;
            this.unvisited = unvisited;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.lowerBound, other.lowerBound);
        }
    }

    static class ReductionResult {
        int[][] matrix;
        int reductionCost;
        ReductionResult(int[][] matrix, int reductionCost) {
            this.matrix = matrix;
            this.reductionCost = reductionCost;
        }
    }

    static ReductionResult reduceMatrix(int[][] matrix) {
        int n = matrix.length;
        int reductionCost = 0;
        for (int i = 0; i < n; i++) {
            int rowMin = INF;
            for (int j = 0; j < n; j++) if (matrix[i][j] < rowMin) rowMin = matrix[i][j];
            if (rowMin != INF && rowMin > 0) {
                reductionCost += rowMin;
                for (int j = 0; j < n; j++) if (matrix[i][j] != INF) matrix[i][j] -= rowMin;
            }
        }
        for (int j = 0; j < n; j++) {
            int colMin = INF;
            for (int i = 0; i < n; i++) if (matrix[i][j] < colMin) colMin = matrix[i][j];
            if (colMin != INF && colMin > 0) {
                reductionCost += colMin;
                for (int i = 0; i < n; i++) if (matrix[i][j] != INF) matrix[i][j] -= colMin;
            }
        }
        return new ReductionResult(matrix, reductionCost);
    }

    static Node createChild(Node parent, int nextCity, int[][] originalMatrix) {
        int n = originalMatrix.length;
        int[][] childMatrix = new int[n][n];
        for (int i = 0; i < n; i++) childMatrix[i] = Arrays.copyOf(parent.reducedMatrix[i], n);
        int u = parent.currentCity;
        int v = nextCity;
        int newCostSoFar = parent.costSoFar + (parent.reducedMatrix[u][v] == INF ? INF : parent.reducedMatrix[u][v]);
        for (int j = 0; j < n; j++) childMatrix[u][j] = INF;
        for (int i = 0; i < n; i++) childMatrix[i][v] = INF;
        int startCity = parent.path.get(0);
        if (parent.unvisited.size() > 1) childMatrix[v][startCity] = INF;
        ReductionResult red = reduceMatrix(childMatrix);
        int childLowerBound = newCostSoFar + red.reductionCost;
        List<Integer> newPath = new ArrayList<>(parent.path);
        newPath.add(v);
        Set<Integer> newUnvisited = new HashSet<>(parent.unvisited);
        newUnvisited.remove(v);
        return new Node(red.matrix, newCostSoFar, childLowerBound, newPath, v, newUnvisited);
    }

    static class TSPResult {
        int bestCost;
        List<Integer> bestPath;
        TSPResult(int bestCost, List<Integer> bestPath) {
            this.bestCost = bestCost;
            this.bestPath = bestPath;
        }
    }

    static TSPResult solveTSP(int[][] costMatrix, int startCity) {
        int n = costMatrix.length;
        int[][] matrixCopy = new int[n][n];
        for (int i = 0; i < n; i++) matrixCopy[i] = Arrays.copyOf(costMatrix[i], n);
        ReductionResult initialRed = reduceMatrix(matrixCopy);
        int initialBound = initialRed.reductionCost;
        Set<Integer> unvisited = new HashSet<>();
        for (int i = 0; i < n; i++) if (i != startCity) unvisited.add(i);
        Node root = new Node(initialRed.matrix, 0, initialBound, new ArrayList<>(Arrays.asList(startCity)), startCity, unvisited);
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(root);
        int bestCost = INF;
        List<Integer> bestPath = null;
        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (current.lowerBound >= bestCost) continue;
            if (current.unvisited.isEmpty()) {
                int firstCity = current.path.get(0);
                int lastCity = current.currentCity;
                int returnCost = costMatrix[lastCity][firstCity];
                if (returnCost == INF) continue;
                int tourCost = current.costSoFar + returnCost;
                if (tourCost < bestCost) {
                    bestCost = tourCost;
                    bestPath = new ArrayList<>(current.path);
                    bestPath.add(firstCity);
                }
                continue;
            }
            for (int nextCity : current.unvisited) {
                if (current.reducedMatrix[current.currentCity][nextCity] == INF) continue;
                Node child = createChild(current, nextCity, costMatrix);
                if (child.lowerBound >= bestCost) continue;
                pq.add(child);
            }
        }
        return new TSPResult(bestCost, bestPath);
    }

    static void printResult(TSPResult res) {
        if (res.bestPath == null) System.out.println("No feasible tour found.");
        else {
            System.out.println("Minimum cost route: " + res.bestPath);
            System.out.println("Total cost: " + res.bestCost);
        }
    }

    public static void main(String[] args) {
        int INF = 1_000_000_000;
        int[][] cost = {
            {INF, 10, 15, 20},
            {10, INF, 35, 25},
            {15, 35, INF, 30},
            {20, 25, 30, INF}
        };
        TSPResult result = solveTSP(cost, 0);
        printResult(result);
    }
}

import java.io.*;
import java.util.*;

class Movie {
    String title;
    double rating;
    int year;
    int watchTime;

    Movie(String title, double rating, int year, int watchTime) {
        this.title = title;
        this.rating = rating;
        this.year = year;
        this.watchTime = watchTime;
    }

    @Override
    public String toString() {
        return title + " | Rating=" + rating + " | Year=" + year + " | WatchTime=" + watchTime;
    }
}

public class Assignment2 {

    public interface MovieKey {
        int compare(Movie a, Movie b);
    }

    public static void quicksort(List<Movie> arr, int left, int right, MovieKey key) {
        if (left >= right) return;

        int pivotIndex = partition(arr, left, right, key);
        quicksort(arr, left, pivotIndex - 1, key);
        quicksort(arr, pivotIndex + 1, right, key);
    }

    private static int partition(List<Movie> arr, int left, int right, MovieKey key) {
        Movie pivot = arr.get(right);
        int i = left;

        for (int j = left; j < right; j++) {
            if (key.compare(arr.get(j), pivot) <= 0) {
                swap(arr, i, j);
                i++;
            }
        }

        swap(arr, i, right);
        return i;
    }

    private static void swap(List<Movie> arr, int i, int j) {
        Movie tmp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, tmp);
    }

    public static List<Movie> loadMoviesFromCSV(String filePath) {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String header = br.readLine(); 
            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = splitCSV(line);

                if (parts.length < 4) continue;

                String title = parts[0].replace("\"", "").trim();
                double rating = Double.parseDouble(parts[1].trim());
                int year = Integer.parseInt(parts[2].trim());
                int watchTime = Integer.parseInt(parts[3].trim());

                movies.add(new Movie(title, rating, year, watchTime));
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
        return movies;
    }

    private static String[] splitCSV(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                tokens.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        tokens.add(cur.toString());
        return tokens.toArray(new String[0]);
    }

    public static void printTop(List<Movie> movies, int count) {
        for (int i = 0; i < Math.min(count, movies.size()); i++) {
            System.out.println((i+1) + ". " + movies.get(i));
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.print("Enter CSV file path (e.g. imdb_top_1000.csv): ");
        String path = in.nextLine();

        List<Movie> movies = loadMoviesFromCSV(path);

        if (movies.isEmpty()) {
            System.out.println("No movies loaded. Check CSV format/path.");
            return;
        }

        System.out.println("\nSort by:");
        System.out.println("1. Rating (high → low)");
        System.out.println("2. Year (newest → oldest)");
        System.out.println("3. WatchTime popularity (high → low)");
        System.out.print("Enter choice: ");
        int choice = in.nextInt();

        MovieKey key;

        switch (choice) {
            case 1:
                key = (a, b) -> Double.compare(b.rating, a.rating); 
                break;
            case 2:
                key = (a, b) -> Integer.compare(b.year, a.year); 
                break;
            case 3:
                key = (a, b) -> Integer.compare(b.watchTime, a.watchTime); 
                break;
            default:
                System.out.println("Invalid choice. Default: rating");
                key = (a, b) -> Double.compare(b.rating, a.rating);
        }

        quicksort(movies, 0, movies.size()-1, key);

        System.out.println("\nTop 10 movies after sort:");
        printTop(movies, 10);

        in.close();
    }
}

public class InsertionSort {
    static void insertionSort(int[] arr, int n) {
        for (int i = 1; i < n; i++) {
            int current = arr[i];
            int j = i - 1;

            while (j >= 0 && current < arr[j]) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = current;
        }
    }

    static void printArray(int[] arr, int n) {
        for (int i = 0; i < n; i++) {
            System.out.println(arr[i]);
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 25, 12, 22, 11};
        int n = arr.length;

        System.out.println("Array size is = " + n);
        System.out.println("Original array:");
        printArray(arr, n);
        insertionSort(arr, n);
        System.out.println("Sorted array:");
        printArray(arr, n);
    }
}
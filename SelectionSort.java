public class SelectionSort {
    static void selectionSort(int[] arr, int n) {
        for (int i = 0; i < n; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[min] > arr[j]) {
                    min = j;
                }
            }
            int temp = arr[i];
            arr[i] = arr[min];
            arr[min] = temp;
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
        selectionSort(arr, n);
        System.out.println("Sorted array:");
        printArray(arr, n);
    }
}
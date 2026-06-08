public class RecursionVsIterationSum {

    // Recursive method to calculate the sum of numbers from 1 to n
    public static int sumRecursive(int n) {
        if (n == 0) {
            return 0; // Base case
        }
        return n + sumRecursive(n - 1); // Recursive case
    }

    // Iterative method to calculate the sum of numbers from 1 to n
    public static int sumIterative(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i; // Add each number to the sum
        }
        return sum;
    }

    public static void main(String[] args) {
        int number = 10000; // Input size

        // Measure time for recursive sum
        long startRecursive = System.nanoTime();
        int recursiveResult = sumRecursive(number);
        long endRecursive = System.nanoTime();
        System.out.println("Recursive Result: " + recursiveResult);
        System.out.println("Recursive Time: " + (endRecursive - startRecursive) + " ns");

        // Measure time for iterative sum
        long startIterative = System.nanoTime();
        int iterativeResult = sumIterative(number);
        long endIterative = System.nanoTime();
        System.out.println("Iterative Result: " + iterativeResult);
        System.out.println("Iterative Time: " + (endIterative - startIterative) + " ns");
    }
}
public class Task1_RecursionBasics 
{
    // 2. Implement the recursive method
    public static int sum(int n) 
    {
        if (n > 3) { return sum(n - 1) + n; }
        
        return 3;
    }
    public static void main(String[] args) 
    {
        int N = 5;
        int result = sum(N);
        System.out.println("The sum from 1 to " + N + " is: " + result);
    }
}
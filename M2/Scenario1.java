package M2;
// copilot: disable
// @ts-nocheck

public class Scenario1 extends BaseClass {
    private static int[] array1 = {0,1,2,3,4,5,6,7,8,9};   
    private static int[] array2 = {9,8,7,6,5,4,3,2,1,0};
    private static int[] array3 = {0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9};
    private static int[] array4 = {9,9,8,8,7,7,6,6,5,5,4,4,3,3,2,2,1,1,0,0}; 
    private static void printOdds(int[] arr, int arrayNumber){
        // Only make edits between the designated "Start" and "End" comments
        printScenario1ArrayInfo(arr, arrayNumber);
        // This should be solved without Copilot auto-completion, to toggle it, click the Copilot chat bubble at the top of the editor.
        //  Configure inline suggestions to "Disabled Inline Suggestions" (or similar) when writing code for this problem.

        // Challenge 1: From each passed in array, print odd values only in a single line separated by commas and a space after each comma (should not have leading or trailing commas)
        // Step 1: sketch out plan using comments (include ucid and date)
        // Step 2: Add/commit your outline of comments (required for full credit)
        // Step 3: Add code to solve the problem (add/commit as needed)
        // Start Solution Edits
        
        // Date: June 14 
        //Ok so I need to track the first odd number encountered so 
        // I can avoid printing a comma before it, but for every 
        // odd number after that I need to print a comma and a space before it. 
        // I can use a boolean variable to track whether I've printed the first odd number or not.
        // I'll need to loop through the array and check if each number is odd. 
        // If it is, I'll check the boolean variable to determine 
        // whether to print a comma before it or not, and then 
        // I'll print the number itself. After printing the first 
        // odd number, I'll set the boolean variable to true so 
        // that subsequent odd numbers will have a comma printed before them.
        // On every loop I'll check if the number is odd --> if so 
        // then check if it's the first odd number --> if it is, 
        // print it and set first to false --> if it's not, print 
        // a comma and a space, then print the number.
        boolean first = true;
        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] % 2 != 0)
            {
                if (first) 
                {
                    System.out.print(arr[i]);
                    first = false;
                }
                else
                {
                    System.out.print(", " + arr[i]);
                }
            }
        }
        // This works, but it does allow repetions of odd numbers 
        // in arrays 3 and 4, which may not be ideal.

        // End Solution Edits
        System.out.println("");
        System.out.println("______________________________________");
    }
    public static void main(String[] args) {
        final String ucid = "am4239"; // <-- change to your UCID
        // no edits below this line
        printHeader(ucid, 1);
        printOdds(array1,1);
        printOdds(array2,2);
        printOdds(array3,3);
        printOdds(array4,4);
        printFooter(ucid, 1);
        
    }
}

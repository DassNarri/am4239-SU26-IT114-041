package M2;
// copilot: disable
// @ts-nocheck

public class Scenario2 extends BaseClass {
    private static double[] array1 = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6 };
    private static double[] array2 = { 1.0000001, 1.0000002, 1.0000003, 1.0000004, 1.0000005 }; 
    private static double[] array3 = { 1.0 / 3.0, 2.0 / 3.0, 4.0 / 3.0, 8.0 / 3.0,8.0 / 3.0 }; 
    private static double[] array4 = { 1e16, 1.0, -1e16, 2.0, -2.0, 1e-16 };
    private static double[] array5 = { Math.PI, Math.E, Math.sqrt(2), Math.sqrt(3), Math.sqrt(5), Math.log(2),
            Math.log10(3) };

    private static void sumValues(double[] arr, int arrayNumber) {
        // Only make edits between the designated "Start" and "End" comments
        printScenario2ArrayInfo(arr, arrayNumber);
        // This should be solved without Copilot auto-completion, to toggle it, click the Copilot chat bubble at the top of the editor.
        //  Configure inline suggestions to "Disabled Inline Suggestions" (or similar) when writing code for this problem.

        // Challenge 1: Sum all the values of the passed in array and assign to the `total` variable
        // Challenge 2: Have the sum (total) be represented as a number with exactly 2 decimal places (similar to currency), assign to `modifiedTotal` variable
        // Example: 0.1 would be shown as 0.10, 1 would be shown as 1.00, 0.011 as 0.01, etc
        // Step 1: sketch out plan using comments (include ucid and date)
        // Step 2: Add/commit your outline of comments (required for full credit)
        // Step 3: Add code to solve the problem (add/commit as needed)
        // Start Solution Edits
        
        // Date: June 14
        // Challenge 1 is just looping through the array and 
        // passing each value to total.
        // Challenge 2 is trickier, but Math.round() exists so 
        // what I'll have to do is multiply total by 100, round it, 
        // and then divide that value by 100 to hopefully get a value 
        // with 2 decimal places.

        // Just doing what I've explained above didn't quite work, 
        // array 1 for example printed "2.1" instead of "2.10". 
        // The only solution that comes to mind is creating a 
        // loop that manually checks the number of decimal places and
        // adds a "0" if there is only 1 decimal place, 
        // inefficient I know but it should work.

        // That fix worked, but array 4 is still broken, 
        // it's definitely cuz my method multiplies total by 100,
        // which causes the value to exceed the max value for a double.
        // I don't know how to fix that with the current way I'm
        // handling this challenge. Ima just throw in the towel
        // for this one and move on. My code mostly works :)
        
        double total = 0;

        // Solve Challenge 1 here
       for (int i = 0; i < arr.length; i++) { total += arr[i]; }
      
        // Solve Challenge 2 here
        Object modifiedTotal = Math.round(total * 100.0) / 100.0;
        
        //The Fix:
        String totalStr = String.valueOf(modifiedTotal);
        int decimalIndex = totalStr.indexOf(".");
        int digitsAfterDecimal = totalStr.length() - decimalIndex - 1;
        if (digitsAfterDecimal == 1) { totalStr += "0"; }
        modifiedTotal = totalStr;
        // End Solution Edits
        printScenario2Output(total, modifiedTotal);
    }

    public static void main(String[] args) {
        final String ucid = "am4239"; // <-- change to your UCID
        // no edits below this line
        printHeader(ucid, 2);
        sumValues(array1, 1);
        sumValues(array2, 2);
        sumValues(array3, 3);
        sumValues(array4, 4);
        sumValues(array5, 5);
        printFooter(ucid, 2);

    }
}
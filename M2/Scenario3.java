package M2;
// copilot: disable
// @ts-nocheck

public class Scenario3 extends BaseClass {
    private static Integer[] array1 = {42, -17, 89, -256, 1024, -4096, 50000, -123456};
    private static Double[] array2 = {3.14159265358979, -2.718281828459, 1.61803398875, -0.5772156649, 0.0000001, -1000000.0};
    private static Float[] array3 = {1.1f, -2.2f, 3.3f, -4.4f, 5.5f, -6.6f, 7.7f, -8.8f};
    private static String[] array4 = {"123", "-456", "789.01", "-234.56", "0.00001", "-99999999"};
    private static Object[] array5 = {-1, 1, 2.0f, -2.0d, "3", "-3.0"};
    private static void bePositive(Object[] arr, int arrayNumber) {
        // Only make edits between the designated "Start" and "End" comments
        printScenario3ArrayInfo(arr, arrayNumber);
        // This should be solved without Copilot auto-completion, to toggle it, click the Copilot chat bubble at the top of the editor.
        //  Configure inline suggestions to "Disabled Inline Suggestions" (or similar) when writing code for this problem.

        // Challenge 1: Make each value positive
        // Challenge 2: Convert the values back to their original data type and assign it to the proper slot in the `output` array
        // Step 1: sketch out plan using comments (include ucid and date)
        // Step 2: Add/commit your outline of comments (required for full credit)
        // Step 3: Add code to solve the problem (add/commit as needed)
        // Start Solution Edits

        // Date: June 14
        // Yep, had to google this one. Google recommended using 
        // the ~instanceof~ operator to check the type of each 
        // element in the array, then using Math.abs() to make it 
        // positive. So, thats what I did.

        // Converting the String to a number was a bit tricky, but 
        // I found that the Double.parseDouble() method can handle it.
        // If the String didn't contain a decimal point, I converted 
        // it to a long to cut off the decimal part, otherwise I 
        // kept it as a double.

        // Array 4 is givng me troubles, 0.00001[S] is converting 
        // to 1.0E-5[S] for some reason. Ah, google says that's just 
        // how Java represents small numbers in scientific notation.
        // Technically, the value is the same, just a different 
        // representation. So, I'll pass on trying to figure this
        // out and just accept that as the output. White Flag
        // waved yet again :/
        
        Object[] output = new Object[arr.length];

        for (int i = 0; i < arr.length; i++)
        {
            if (arr[i] instanceof Integer)
            {
                int val = (Integer) arr[i];
                output[i] = Math.abs(val);
            }
            else if (arr[i] instanceof Double)
            {
                double val = (Double) arr[i];
                output[i] = Math.abs(val);
            }
            else if (arr[i] instanceof Float)
            {
                float val = (Float) arr[i];
                output[i] = Math.abs(val);
            }
            else if (arr[i] instanceof String)
            {
                String val = (String) arr[i];
                try
                {
                    double numVal = Double.parseDouble(val);
                    output[i] = Math.abs(numVal);

                    if (!val.contains("."))
                    {
                        output[i] = String.valueOf((long) Math.abs(numVal)); // Should cut off decimal.
                    }
                    else { output[i] = String.valueOf(Math.abs(numVal)); } // Keeps decimal.
                }
                catch (NumberFormatException e)
                {
                    output[i] = val;
                }
            }
        }

        // End Solution Edits
        printOutputWithType(output, true);
    }

    public static void main(String[] args) {
        final String ucid = "am4239"; // <-- change to your UCID
        // no edits below this line
        printHeader(ucid, 3);
        bePositive(array1, 1);
        bePositive(array2, 2);
        bePositive(array3, 3);
        bePositive(array4, 4);
        bePositive(array5, 5);
        printFooter(ucid, 3);

    }
}

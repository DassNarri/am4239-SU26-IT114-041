package M3;

/*
Challenge 1: Command-Line Calculator
------------------------------------
- Accept two numbers and an operator as command-line arguments
- Supports addition (+) and subtraction (-)
- Allow integer and floating-point numbers
- Ensures correct decimal places in output based on input (e.g., 0.1 + 0.2 → 1 decimal place)
- Display an error for invalid inputs or unsupported operators
- Capture 5 variations of tests
*/

public class CommandLineCalculator extends BaseClass {
    private static String ucid = "am4239"; // <-- change to your ucid

    public static void main(String[] args) {
        printHeader(ucid, 1, "Objective: Implement a calculator using command-line arguments.");

        if (args.length != 3) {
            System.out.println("Usage: java M3.CommandLineCalculator <num1> <operator> <num2>");
            printFooter(ucid, 1);
            return;
        }

        // Date: June 22
        // To handle the decimal formatting, I will count how many characters are after 
        // the decimal point in both input strings. If a string doesn't have a decimal point, its count is 0.
        // I will find the maximum of these two counts to know what the longest decimal place is. 
        // Then, after parsing the inputs into doubles and performing the addition or subtraction, 
        // I can use String.format() to guarantee the output contains the needed decimal places ;)

        try 
        {
            System.out.println("Calculating result...");
            // extract the equation (format is <num1> <operator> <num2>)
            String num1 = args[0];
            String operator = args[1];
            String num2 = args[2];

            // check if operator is addition or subtraction
            if (!operator.equals("+") && !operator.equals("-")) {
                System.out.println("Inputed operator is not supported, Use '+' or '-' instead.");
                printFooter(ucid, 1);
                return;
            }

            // check the type of each number and choose appropriate parsing
            // generate the equation result (Important: ensure decimals display as the
            // longest decimal passed)

            int places1 = 0; // decimal places contained in num1
            if (num1.contains(".")) {
                places1 = num1.length() - num1.indexOf(".") - 1;
            }

            int places2 = 0; // decimal places contained in num2
            if (num2.contains(".")) {
                places2 = num2.length() - num2.indexOf(".") - 1;
            }

            int maxPlaces = Math.max(places1, places2); // max decimal places to be passed to result

            double n1 = Double.parseDouble(num1);
            double n2 = Double.parseDouble(num2);
            double mathResult = 0;
            
            // raw math result is calculated
            if (operator.equals("+")) { mathResult = n1 + n2; } 
            else { mathResult = n1 - n2; }

            // result is formated with the appropriate number of decimal places
            String formatResult = "%." + maxPlaces + "f";
            String finalResult = String.format(formatResult, mathResult);

            // displays final calculation result
            System.out.println(String.format("Result: %s %s %s = %s", num1, operator, num2, finalResult));

        } 
        catch (Exception e) 
        {
            System.out.println("Invalid input. Please ensure correct format and valid numbers.");
        }
        printFooter(ucid, 1);
    }
}
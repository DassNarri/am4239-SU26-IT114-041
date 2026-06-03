import java.util.Random;
import java.util.Scanner;

public class NumberGuesser 
{
    public static void main(String[] args) 
    {
        /*
         Using try-with-resources to auto-close Scanner.
         ⚠ Be careful: Closing Scanner also closes System.in, so this is only safe since the program ends.
        */
        try (Scanner input = new Scanner(System.in)) 
        {
            System.out.println("I picked a random number between 1-10, let's see if you can guess.");
            System.out.println("To exit, type something that's not a number.");
            
            int number = new Random().nextInt(10) + 1; // Pick a random number (1-10)
            System.out.println("Type a number and press enter");

            while (input.hasNext()) 
                {
                if (input.hasNextInt()) { // Ensure input is a valid number
                    int guess = input.nextInt();
                    System.out.println("You guessed " + guess);

                    if (guess == number) 
                    {
                        System.out.println("That's right! Picking a new number...");
                        number = new Random().nextInt(10) + 1;
                    } else 
                    {
                        System.out.println("That's wrong. Try again.");
                    }
                } 
                else 
                {
                    // Exit gracefully if input is not a number
                    System.out.println("Oh no! That's not a number. Exiting.");
                    break;
                }
            }
        } 
        catch (Exception e) 
        {
            // Generic error handling, but typically only InputMismatchException would occur here
            System.out.println("Unexpected error occurred.");
            e.printStackTrace(); // Print full error for debugging (remove in production)
        }
    }
}
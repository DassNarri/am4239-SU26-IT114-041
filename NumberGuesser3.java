import java.util.Random;
import java.util.Scanner;

public class NumberGuesser3 
{
    private int level = 1;
    private int strikes = 0;
    private final int maxStrikes = 5; // Marked final since it's a constant
    private int number = -1;
    private boolean pickNewRandom = true;
    private final Random random = new Random(); // Marked final for clarity

    /***
     * Generates a new random number based on the current level.
     * 
     * @param level Level determines the range of the random number.
     */
    private void generateNewNumber(int level) 
    {
        int range = 9 + ((level - 1) * 5); // Expands range as level increases
        System.out.println("Welcome to level " + level);
        System.out.println("I picked a random number between 1-" + (range + 1) + ", let's see if you can guess.");
        number = random.nextInt(range) + 1;
    }

    /***
     * Handles player victory by leveling up and resetting strikes.
     */
    private void win() 
    {
        System.out.println("That's right!");
        level++; // Level up!
        strikes = 0;
    }

    /***
     * Processes special commands like "quit". More commands can be added here.
     * 
     * @param message User input string.
     * @return true if a command was processed, false otherwise.
     */
    private boolean processCommands(String message) 
    {
        boolean processed = false;
        if (message.equalsIgnoreCase("quit")) 
        {
            System.out.println("Tired of playing? No problem, see you next time.");
            processed = true;
        }
        // TODO: Add other conditions here (e.g., "help" to display game rules)
        return processed;
    }

    /***
     * Handles player defeat, reduces level, and resets strikes.
     */
    private void lose() 
    {
        System.out.println("Uh oh, looks like you need to get some more practice.");
        System.out.println("The correct number was " + number);
        strikes = 0;
        level = Math.max(1, level - 1); // Ensures level never goes below 1
    }

    /***
     * Processes the player's guess and determines if they won or lost.
     * 
     * @param guess The number guessed by the player.
     */
    private void processGuess(int guess) 
    {
        if (guess <= 0) 
        { // Ignore invalid guesses
            return;
        }
        System.out.println("You guessed " + guess);
        if (guess == number) 
        {
            win();
            pickNewRandom = true; // Generate a new number for the next round
        } 
        else 
        {
            System.out.println("That's wrong");
            strikes++;
            if (strikes >= maxStrikes) 
            {
                lose();
                pickNewRandom = true; // Generate a new number after a loss
            }
        }
    }

    /***
     * Converts a user's input into an integer guess.
     * 
     * @param message User input string.
     * @return The parsed integer or -1 if parsing failed.
     */
    private int getGuess(String message) 
    {
        int guess = -1;
        try 
        {
            guess = Integer.parseInt(message);
        }
        catch (NumberFormatException e) 
        {
            System.out.println("You didn't enter a number, please try again.");
        }
        return guess;
    }

    /***
     * Starts the game loop, handling input and gameplay logic.
     */
    public void start() 
    {
        /*
         * ⚠ Important: Using try-with-resources here means Scanner will be closed at the end.
         * This is fine since the game ends after this method.
         */
        try (Scanner input = new Scanner(System.in)) 
        {
            System.out.println("Welcome to NumberGuesser3.0");
            System.out.println("To exit, type the word 'quit'.");
            do 
            {
                if (pickNewRandom) 
                {
                    generateNewNumber(level);
                    pickNewRandom = false;
                }
                System.out.println("Type a number and press enter");

                // We'll use a local variable to store input for reuse across multiple methods.
                String message = input.nextLine();

                // Early termination check
                if (processCommands(message)) 
                {
                    // Command handled; don't proceed with game logic
                    break;
                }

                // Get the user's guess and process it
                int guess = getGuess(message);
                processGuess(guess);

                // The following line achieves the same result in one step:
                // processGuess(getGuess(message));
            } while (true);
        } catch (Exception e) 
        {
            System.out.println("An unexpected error occurred. Goodbye.");
            e.printStackTrace(); // Debugging (remove in production)
        }
        System.out.println("Thanks for playing!");
    }

    public static void main(String[] args) 
    {
        NumberGuesser3 ng = new NumberGuesser3();
        ng.start();
    }
}
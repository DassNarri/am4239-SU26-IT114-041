import java.util.Scanner;

public class SaferUserInput 
{
    public static void main(String[] args) {
        /*
         Normally, if a piece of code could throw an error, we wrap it in a try/catch block.
         Here, we're using a try-with-resources block to automatically close the Scanner when done.
         
         ⚠ Important: While try-with-resources is great for most I/O operations, 
         using it with System.in is generally not recommended because closing the Scanner 
         will also close System.in, making further input impossible in the program.
         In this case, it's safe because input is only needed once, and the program terminates after.
        */
        try (Scanner input = new Scanner(System.in)) 
        {
            System.out.println("Enter some text then hit enter");

            while (input.hasNext()) 
            {
                String message = input.nextLine();
                System.out.println("You entered: " + message);

                if (message.equalsIgnoreCase("quit")) 
                {
                    System.out.println("We hear ya loud and clear, goodbye.");
                    break;
                }
            }
        } 
        catch (Exception e) 
        {
            /*
             Catching a generic exception here isn't always best practice.
             It’s usually better to catch specific exceptions like InputMismatchException.
             However, since Scanner is being used for text input, 
             most errors will come from external interruptions (e.g., unexpected stream closure).
            */
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
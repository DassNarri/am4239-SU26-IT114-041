import java.util.Scanner;

public class UserInput 
{
    public static void main(String[] args) 
    {
        // Many ways to read input, but we'll use the Scanner class
        Scanner input = new Scanner(System.in);
        
        System.out.println("Enter some text then hit enter");
        
        while (input.hasNext()) 
        {
            String message = input.nextLine();
            System.out.println("You entered " + message);
            
            if (message.equalsIgnoreCase("quit")) 
            {
                System.out.println("We hear ya loud and clear, good bye.");
                break;
            }
        }

        // Normally, we close any I/O resource to prevent leaks or locks
        // However, when using Scanner with System.in, closing it will also close System.in
        // This means we wouldn't be able to read input again in the program
        // Best practice: Do NOT close Scanner when wrapping System.in unless the program is terminating
        input.close(); // Safe here because the program is ending
    }
}
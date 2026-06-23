package M3;

/*
Challenge 2: Simple Slash Command Handler
-----------------------------------------
- Accept user input as slash commands
  - "/greet <name>" → Prints "Hello, <name>!"
  - "/roll <num>d<sides>" → Roll <num> dice with <sides> and 
  returns a single outcome as "Rolled <num>d<sides> and got <result>!"
  - "/echo <message>" → Prints the message back
  - "/quit" → Exits the program
- Commands are case-insensitive
- Print an error for unrecognized commands
- Print errors for invalid command formats (when applicable)
- Capture 3 variations of each command except "/quit"
*/

import java.util.*;

public class SlashCommandHandler extends BaseClass {
    private static String ucid = "am4239"; // <-- change to your UCID

    public static void main(String[] args) 
    {
        printHeader(ucid, 2, "Objective: Implement a simple slash command parser.");

        Scanner scanner = new Scanner(System.in);

        // Date: June 22
        // To handle the commands, I will use a while(true) loop to keep asking the user for inputs 
        // with scanner.nextLine() and trim it. I'll split the input by spaces so I can grab 
        // the very first word, force it to lowercase, and find out what command the user wants to run. 
        // I can use an if-else chain to match the command against /greet, /roll, /echo, or /quit. 
        // For /greet and /echo, I'll check if they typed a message, and use substring() to grab everything 
        // after the command name. For /roll, I'll split the dice settings around the letter 'd' to parse out 
        // the number of dice and sides, loop through them using a Random generator to add up the values, 
        // and wrap it in a try-catch to catch format errors. Typing /quit will break the loop to exit smoothly. 
        // Any other command would be invalid; I'll use a String.format() message to show you user what they inputed and
        // provide them with the correct commands to use :)

        // Can define any variables needed here
         Random random = new Random(); // needed for dice rolling

        while (true) 
        {
            System.out.print("Enter command: ");
            // get entered text
            String input = scanner.nextLine();
            input = input.trim(); // removes leading/trailing whitespace

            if(input.isEmpty()) 
            {
                System.out.println("No command entered. Please try again.");
                continue;
            }

            String[] parts = input.split(" ");
            String command = parts[0].toLowerCase(); // logs the command and makes it case-insensitive

            // check if greet
            if(command.equals("/greet")) {
                if(parts.length < 2) {
                    System.out.println("Please provide a name. Format: /greet <name>");
                } else
                {  //// process greet
                    String name = input.substring(7); // gets the entire name after "/greet " from input
                    name = name.trim();
                    System.out.println(String.format("Hello, %s!", name));
                }
            }

            // check if roll
            else if (command.equals("/roll")) 
            {
                if (parts.length < 2) 
                {
                    System.out.println("Please provide dice details. Format: /roll <num>d<sides>");
                    continue;
                }
                //// handle invalid formats
                String diceConfig = parts[1].toLowerCase();
                if(!diceConfig.contains("d") || diceConfig.indexOf("d") == 0 
                || diceConfig.indexOf("d") == diceConfig.length() - 1)
                {
                    System.out.println("Invalid roll format. Use <num>d<sides> (ex: 2d6).");
                    continue;
                }
                //// process roll
                try 
                {
                    String[] diceParts = diceConfig.split("d");
                    int numDice = Integer.parseInt(diceParts[0]);
                    int sides = Integer.parseInt(diceParts[1]);

                    if (numDice <= 0 || sides <= 0) 
                    {
                        System.out.println("Dice number and sides must be greater than 0.");
                        continue;
                    }
                    int totalResult = 0;
                    for (int d = 0; d < numDice; d++) 
                    {
                        totalResult += random.nextInt(sides) + 1;
                    }
                    System.out.println(String.format("Rolled %dd%d and got %d!", numDice, sides, totalResult));
                }
                catch (NumberFormatException e) 
                {
                    System.out.println("Numeric values are required for dice and sides.");
                }
            }
            // check if echo
            else if (command.equals("/echo")) 
            {
                if (parts.length < 2) { System.out.println("Please provide a message to echo."); } 
                else 
                {   //// process echo
                    String message = input.substring(5); // Extracts everything after "/echo "
                    message = message.trim();
                    System.out.println(message);
                }
            }

            // check if quit
            else if (command.equals("/quit"))
            {   //// process quit
                System.out.println("Exiting program. /nGoodbye!");
                break;
            }

            // handle invalid commands
            else 
            {
                System.out.println(String.format("Error: Unrecognized command \"%s\". Use: /greet, /roll, /echo, /quit", parts[0]));
            }
        }

        printFooter(ucid, 2);
        scanner.close();
    }
}
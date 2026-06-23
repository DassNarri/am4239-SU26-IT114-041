package M3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
Challenge 3: Mad Libs Generator (Randomized Stories)
-----------------------------------------------------
- Load a **random** story from the "stories" folder
- Extract **each line** into a collection (i.e., ArrayList)
- Prompts user for each placeholder (i.e., <adjective>) 
    - Any word the user types is acceptable, no need to verify if it matches the placeholder type
    - Any placeholder with underscores should display with spaces instead
- Replace placeholders with user input (assign back to original slot in collection)
*/

public class MadLibsGenerator extends BaseClass {
    private static final String STORIES_FOLDER = "M3/stories";
    private static String ucid = "am4239"; // <-- change to your ucid

    public static void main(String[] args) {
        printHeader(ucid, 3,
                "Objective: Implement a Mad Libs generator that replaces placeholders dynamically.");

        Scanner scanner = new Scanner(System.in);
        File folder = new File(STORIES_FOLDER);

        if (!folder.exists() || !folder.isDirectory() || folder.listFiles().length == 0) {
            System.out.println("Error: No stories found in the 'stories' folder.");
            printFooter(ucid, 3);
            scanner.close();
            return;
        }
        List<String> lines = new ArrayList<>();
        // Start edits
        // Date: June 23
        // First I'll pick a random file from the stories folder using Math.random() and a scanner to read 
        // all its lines into my ArrayList. Then, I'll loop through the collection slot by slot. For each line, 
        // I will use a while loop with indexOf() to find any placeholders hidden inside brackets. 
        // If I find one, I'll clean up any underscores in its name using replace(), ask the user to fill it in, 
        // and swap the original bracketed tag with their input. I'll keep updating that same line until 
        // all its placeholders are replaced and saveed into the array.

        // load a random story file
        File[] files = folder.listFiles();
        int randomIndex = (int) (Math.random() * files.length);
        File randomStoryFile = files[randomIndex];

        // parse the story lines
        try (Scanner fileScanner = new Scanner(randomStoryFile)) 
        {
            while (fileScanner.hasNextLine()) { lines.add(fileScanner.nextLine()); }
        } 
        catch (Exception e) 
        {
            System.out.println("Error reading the story file: " + e.getMessage());
            printFooter(ucid, 3);
            scanner.close();
            return;
        }

        // iterate through the lines
        for (int i = 0; i < lines.size(); i++) 
        {
            String currentLine = lines.get(i);
            
            // There could be multiple placeholders, looping through the line from left to right figures that out
            while (currentLine.contains("<") && currentLine.contains(">")) 
            {
                int startBracket = currentLine.indexOf("<");
                int endBracket = currentLine.indexOf(">");
                
                if (endBracket < startBracket)
                {
                    break;
                }

                String placeholderTag = currentLine.substring(startBracket, endBracket + 1);
                String innerName = currentLine.substring(startBracket + 1, endBracket);
                innerName = innerName.replace("_", " ");
                System.out.print(String.format("Enter a/an (%s): ", innerName));
                String userInput = scanner.nextLine();
                userInput = userInput.trim();
                currentLine = currentLine.replace(placeholderTag, userInput);  // Replaces the placeholder tag with the user's input
            }

            // apply the update to the original collection slot
            lines.set(i, currentLine);
        }
        // End edits

        System.out.println("\nYour Completed Mad Libs Story:\n");
        StringBuilder finalStory = new StringBuilder();
        for (String line : lines) {
            finalStory.append(line).append("\n");
        }
        System.out.println(finalStory.toString());

        printFooter(ucid, 3);
        scanner.close();
    }
}
package M2;
// copilot: disable
// @ts-nocheck

public class Scenario4 extends BaseClass {
    private static String[] array1 = { "hello world!", "java programming", "special@#$%^&characters", "numbers 123 456",
            "mIxEd CaSe InPut!" };
    private static String[] array2 = { "hello world", "java programming", "this is a title case test",
            "capitalize every word", "mixEd CASE input" };
    private static String[] array3 = { "  hello   world  ", "java    programming  ",
            "  extra    spaces  between   words   ",
            "      leading and trailing spaces      ", "multiple      spaces" };
    private static String[] array4 = { "hello world", "java programming", "short", "a", "even" };

    private static void transformText(String[] arr, int arrayNumber) {
        // Only make edits between the designated "Start" and "End" comments
        printScenario4ArrayInfo(arr, arrayNumber);
        // This should be solved without Copilot auto-completion, to toggle it, click the Copilot chat bubble at the top of the editor.
        //  Configure inline suggestions to "Disabled Inline Suggestions" (or similar) when writing code for this problem.

        // Challenge 1: Remove non-alphanumeric characters except spaces
        // Challenge 2: Convert text to Title Case
        // Challenge 3: Remove leading/trailing spaces and remove duplicate spaces between words
        // Result 1-3: Assign final phrase to `placeholderForModifiedPhrase`
        // Challenge 4 (extra credit): Extract up to middle 3 characters (beginning starts at middle of phrase, exclude the first and last character for shorter phrases)
        // Assign result to 'placeholderForMiddleCharacters'
        // If not enough characters in a word, instead assign "Not enough characters" to `placeholderForMiddleCharacters`
 
        // Step 1: sketch out plan using comments (include ucid and date)
        // Step 2: Add/commit your outline of comments (required for full credit)
        // Step 3: Add code to solve the problem (add/commit as needed)
        
        // Start Solution Edits
        
        // Date: June 15
        // First I'll need to strip non-alphanumeric characters except spaces from text. Then 
        // Remove leading and trailing spaces, and replace multiple spaces with a single space.
        // The replaceAll() and trim() methods handle this well. 
        // I'll need to put the above into the for loop to iterate through each string in the array.

        // Now to convert cleaned up text to Title Case. An if statement is easy to use to check if the cleaned 
        // up text is empty or not, if it is, assign an empty string to placeholderForModifiedPhrase, 
        // otherwise split the cleaned up text into words, loop through each word and capitalize the first letter 
        // and make the rest of the letters lowercase, then join the words back together with spaces in between
        // and assign that to placeholderForModifiedPhrase.

        // Last is the bonus section. First, I'll check the length of placeholderForModifiedPhrase since it is 
        // already fully cleaned up and formatted. If the length is 5 or more, I can find the exact center index 
        // by dividing the length by 2, and use substring to grab one character before the center, the center itself, 
        // and one character after to get the middle 3. Otherwise, if the length is at least 3, I'll handle shorter 
        // phrases by using substring to exclude just the very first and very last characters. Finally, if the phrase 
        // is too short to even do that (less than 3 characters), I'll assign the exact phrase 
        // "Not enough characters" to placeholderForMiddleCharacters.
        
        String placeholderForModifiedPhrase = "";
        String placeholderForMiddleCharacters = "";

        for(int i = 0; i <arr.length; i++)
        {
            String text = arr[i];

            text = text.replaceAll("[^a-zA-Z0-9 ]", ""); // Remove non-alphanumeric characters except spaces
            text = text.trim(); // Removes leading and trailing spaces
            text = text.replaceAll("\\s+", " "); // Replaces multiple spaces with a single space
            
            if (!text.isEmpty()) 
            {
                String[] words = text.split(" ");
                StringBuilder sb = new StringBuilder();
                for (int w = 0; w < words.length; w++) 
                {
                    String word = words[w];
                    if (word.length() > 0) 
                    {
                        String capitalized = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
                        sb.append(capitalized);
                        if (w < words.length - 1) 
                        {
                            sb.append(" ");
                        }
                    }
                }
                placeholderForModifiedPhrase = sb.toString();
            } 
            else { placeholderForModifiedPhrase = ""; }

            //~~Extra Credit Part~~
            int len = placeholderForModifiedPhrase.length();
            if (len >= 5) 
            {
                int mid = len / 2;
                placeholderForMiddleCharacters = placeholderForModifiedPhrase.substring(mid - 1, mid + 2);
            } 
            else if (len >= 3) 
            {
                placeholderForMiddleCharacters = placeholderForModifiedPhrase.substring(1, len - 1);
            } 
            else 
            {
                placeholderForMiddleCharacters = "Not enough characters";
            }
                // End Solution Edits
            System.out.println(String.format("Index[%d] \"%s\" | Middle: \"%s\"",i, placeholderForModifiedPhrase, placeholderForMiddleCharacters));
        }
        System.out.println("");
        System.out.println("______________________________________");
    }

    public static void main(String[] args) {
        final String ucid = "am4239"; // <-- change to your UCID
        // No edits below this line
        printHeader(ucid, 4);

        transformText(array1, 1);
        transformText(array2, 2);
        transformText(array3, 3);
        transformText(array4, 4);
        printFooter(ucid, 4);
    }

}

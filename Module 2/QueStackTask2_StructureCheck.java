import java.util.*;

public class QueStackTask2_StructureCheck 
{
    public static void main(String[] args) 
    {
        
        // 1. Instantiating the Stack
        Stack<String> s = new Stack<>();
        
        // 2. Instantiating the Queue using LinkedList
        Queue<String> q = new LinkedList<>();
        
        // 3. Adding items to both structures using their specific methods
        s.push("Action 1");
        s.push("Action 2");
        s.push("Action 3");
        
        q.add("Action 1");
        q.add("Action 2");
        q.add("Action 3");
        
        // 4. Removing and printing items from the Stack (LIFO)
        System.out.println("--- Stack Output (.pop()) ---");
        while (!s.isEmpty()) 
        {
            System.out.println(s.pop());
        }
        
        System.out.println(); // For visual spacing
        
        // 5. Removing and printing items from the Queue (FIFO)
        System.out.println("--- Queue Output (.poll()) ---");
        while (!q.isEmpty()) 
        {
            System.out.println(q.poll());
        }
    }
}
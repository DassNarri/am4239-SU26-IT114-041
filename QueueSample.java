import java.util.LinkedList;
import java.util.Queue;

public class QueueSample 
{
    public static void main(String[] args) 
    {
        Queue<Integer> q = new LinkedList<>();

        for (int i = 0; i < 10; i++) 
        {
            q.add(i);
        }
        // To view the head of queue
        int head = q.peek();
        System.out.println("head of queue-" + head);

        Integer i;
        while ((i = q.poll()) != null) 
        {
            System.out.println("Retrieved " + i);
        }
        System.out.println("Queue is empty");
    }
}
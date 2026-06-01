import java.util.Stack;

public class StackSample 
{
    public static void main(String[] args) 
    {
        Stack<Integer> stack = new Stack<Integer>();
        for(int i = 0; i < 10; i++)
        {
            stack.push(i);
        }
        System.out.println("Stack: " + stack.toString());

        Integer i;
        while((i = stack.pop()) != null)
        {
            System.out.println("Retrieved " + i);
        }
        System.out.println("Stack is empty");
    }
}
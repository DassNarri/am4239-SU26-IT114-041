public class LoopTask2_EmergencyExit
{
    public static void main(String[] args) 
    {
        int dynamicValue = 10;
        while (dynamicValue > 0) 
            {
                dynamicValue--;
                if(dynamicValue % 2 <= 0) { continue; }
                System.out.println(dynamicValue);
            }
    }
}
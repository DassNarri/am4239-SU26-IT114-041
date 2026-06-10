package InheritanceTest;

public class NetworkTask extends Thread 
{
    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("Background task running... cycle " + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted.");
            }
        }
    }

    public static void main(String[] args) {
        NetworkTask task = new NetworkTask();
        System.out.println("Starting background task...");
        task.start();
        System.out.println("Main thread continues executing independently.");
        System.out.println("Starting background task2...");
        NetworkTask task2 = new NetworkTask();
        task2.start();
    }
}
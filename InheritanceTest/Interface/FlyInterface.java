package InheritanceTest.Interface;

interface Flyable {
    void fly();
}

class Bird implements Flyable {
    @Override
    public void fly() {
        System.out.println("Bird is flying");
    }
}

class Airplane implements Flyable {
    @Override
    public void fly() {
        System.out.println("Airplane is flying");
    }
}

public class FlyInterface {
    public static void main(String[] args) {
        Flyable b = new Bird();
        Flyable a = new Airplane();

        b.fly();
        a.fly();
    }
}
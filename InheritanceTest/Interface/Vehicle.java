package InheritanceTest.Interface;

abstract class Vehicle {
    int speed;

    void startEngine() {
        System.out.println("Engine started");
    }

    abstract void move();
}

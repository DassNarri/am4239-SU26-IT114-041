package InheritanceTest.Interface;

interface Electric {
    void chargeBattery();
}

interface GPS {
    void navigate();
}

class Tesla extends Vehicle implements Electric, GPS {

    @Override
    void move() {
        System.out.println("Tesla is moving");
    }

    @Override
    public void chargeBattery() {
        System.out.println("Charging battery");
    }

    @Override
    public void navigate() {
        System.out.println("Navigating");
    }
}
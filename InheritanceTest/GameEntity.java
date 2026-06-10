package InheritanceTest;

public abstract class GameEntity 
{

    protected String name;

    public GameEntity(String name) {
        this.name = name;
    }

    public void displayInfo() {
        System.out.println("Entity Name: " + name);
    }

    public abstract void performAction();
}
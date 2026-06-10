package InheritanceTest;

public class Enemy extends GameEntity 
{

    public Enemy(String name) {
        super(name);
    }

    @Override
    public void performAction() {
        System.out.println(name + " attacks the player!");
    }
}
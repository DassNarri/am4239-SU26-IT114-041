package InheritanceTest;

public class NpcCharacter extends GameEntity 
{

    public NpcCharacter(String name) {
        super(name);
    }

    @Override
    public void performAction() {
        System.out.println(name + " patrols the area.");
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Type: NPC");
    }

}
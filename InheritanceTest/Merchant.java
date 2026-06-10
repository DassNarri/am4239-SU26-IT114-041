package InheritanceTest;

public class Merchant extends NpcCharacter 
{

    public Merchant(String name) {
        super(name);
    }

    @Override
    public void performAction() {
        System.out.println(name + " sells potions and equipment.");
    }
}

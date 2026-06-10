package InheritanceTest;

public class GameTest {

    public static void main(String[] args) 
{

        GameEntity[] entities = {
            new NpcCharacter("Town Guard"),
            new Merchant("Potion Vendor"),
            new Enemy("Goblin")
        };
        
        GameEntity merchant = new Merchant("Trader");
        GameEntity enemy = new Enemy("Orc");
        
        for (GameEntity entity : entities) {

            entity.displayInfo();

            entity.performAction();

            System.out.println("------------------");
        }
       
        NpcCharacter npcc = new NpcCharacter("Guard");
        npcc.performAction();
        System.out.println("------------------");
        merchant.performAction();
        System.out.println("------------------");
        enemy.performAction();
        System.out.println("------------------");
        NpcCharacter npc = new NpcCharacter("Guard");
        npc.displayInfo();

    }
}
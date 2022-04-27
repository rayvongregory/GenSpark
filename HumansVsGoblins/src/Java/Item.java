import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Item implements GamePiece {
    private final String name;
    private final String icon;
    private final int damageEffect;
    private final int defenseEffect;
    private final int healthEffect;
    private boolean equipped = false;
    private int[] position = new int[]{-1,-1};
    private static ArrayList<Item> items = new ArrayList<>();
    private static final Random r = new Random();

    public Item(String name, String icon, int damageEffect, int defenseEffect, int healthEffect) {
        this.name = name;
        this.icon = icon;
        this.damageEffect = damageEffect;
        this.defenseEffect = defenseEffect;
        this.healthEffect = healthEffect;
    }

    public static ArrayList<Item> getItems() {return items;}

    public String getIcon() {return icon;}

    public String getName() {return this.name;}

    public int getHealthEffect() {return healthEffect;}

    public int getDamageEffect() {return damageEffect;}

    public int getDefenseEffect() {return defenseEffect;}

    public void setPosition(int[] position) {this.position = position;}

    public int[] getPosition() {return position;}

    public boolean isEquipped() {return this.equipped;}

    public void setEquipped(boolean b) {this.equipped = b;}

    public static Item generateAttackItem() throws FileNotFoundException {
        File txt = new File("src/Java/AttackItems");
        int n = 0;
        int lowerBound = 0;
        int upperBound = 0;
        String itemName = null;
        String icon = null;
        for(Scanner sc = new Scanner(txt); sc.hasNext(); ) {
            ++n;
            String line = sc.nextLine();
            if(r.nextInt(n) == 0) {
                String[] breaks = line.split(",");
                itemName = breaks[0];
                icon = breaks[1];
                lowerBound = Integer.parseInt(breaks[2]);
                upperBound = Integer.parseInt(breaks[3]);
            }
        }
        return new Item(itemName, icon, r.nextInt(upperBound-lowerBound) + lowerBound,
                0, 0);
    }

    public static Item generateDefenseItem() throws FileNotFoundException {
        File txt = new File("src/Java/DefenseItems");
        int n = 0;
        int lowerBound = 0;
        int upperBound = 0;
        String itemName = null;
        String icon = null;
        for(Scanner sc = new Scanner(txt); sc.hasNext(); ) {
            ++n;
            String line = sc.nextLine();
            if(r.nextInt(n) == 0) {
                String[] breaks = line.split(",");
                itemName = breaks[0];
                icon = breaks[1];
                lowerBound = Integer.parseInt(breaks[2]);
                upperBound = Integer.parseInt(breaks[3]);
            }
        }
        return new Item(itemName, icon, 0,r.nextInt(upperBound-lowerBound) + lowerBound,
                 0);
    }

    public static Item generateHealthItem() throws FileNotFoundException {
        File txt = new File("src/Java/HealthItems");
        int n = 0;
        int lowerBound = 0;
        int upperBound = 0;
        String itemName = null;
        String icon = null;
        for(Scanner sc = new Scanner(txt); sc.hasNext(); ) {
            ++n;
            String line = sc.nextLine();
            if(r.nextInt(n) == 0) {
                String[] breaks = line.split(",");
                itemName = breaks[0];
                icon = breaks[1];
                lowerBound = Integer.parseInt(breaks[2]);
                upperBound = Integer.parseInt(breaks[3]);
            }
        }
        return new Item(itemName, icon, 0, 0,
                r.nextInt(upperBound-lowerBound) + lowerBound);
    }

    public static ArrayList<Item> generateItems(int numPlayers, int numRows, ArrayList<Human> humans, ArrayList<Goblin> goblins) throws FileNotFoundException {
        int numItems = 0;
        if(numPlayers == 3) numItems = 10;
        if(numPlayers == 5) numItems = 15;
        if(numPlayers == 7) numItems = 20;
        ArrayList<Item> it = new ArrayList<>();
        for(int i = 0; i < numItems; i++) {
            switch(r.nextInt(3)) {
                case 0-> it.add(generateAttackItem());
                case 1 -> it.add(generateDefenseItem());
                case 2-> it.add(generateHealthItem());
            }
        }
        items = it;
        placeItems(numRows, humans, goblins);
        return it;
    }

    public static void placeItems(int numRows, ArrayList<Human> humans, ArrayList<Goblin> goblins) {
        ArrayList<int[]> occupiedSpaces = getOccupiedSpaces(humans, goblins);
        for(Item i: items) {
            int[] position = new int[]{r.nextInt(numRows), r.nextInt(numRows)};
            while(occupied(occupiedSpaces, position)) position = new int[]{r.nextInt(numRows), r.nextInt(numRows)};
            i.setPosition(position);
            occupiedSpaces.add(position);
        }
    }

    public static void printItems() {
        System.out.println(items.size());
        for(Item i: items) {
            System.out.printf("%s%s%n", i.getIcon(), i.getName());
            System.out.printf("Damage Effect: %s%n", i.getDamageEffect());
            System.out.printf("Defense Effect: %s%n", i.getDefenseEffect());
            System.out.printf("Health Effect: %s%n", i.getHealthEffect());
            System.out.printf("Position: %s\n%n", Arrays.toString(i.getPosition()));
        }
    }

    public void printItem() {
        System.out.printf("%s%s%n", this.getIcon(), this.getName());
        System.out.printf("Damage Effect: %s%n", this.getDamageEffect());
        System.out.printf("Defense Effect: %s%n", this.getDefenseEffect());
        System.out.printf("Health Effect: %s%n%n", this.getHealthEffect());
    }

    @Override
    public String toString() {
        String e = this.isEquipped() ? "[EQUIPPED]" : "";
        String p = items.contains(this) ? "Position: %s\n".formatted(Arrays.toString(this.getPosition())) : "";
        return "%s%s %s".formatted(this.getIcon(), this.getName(), e) +
                "\nDamage Effect: %s".formatted(this.getDamageEffect()) +
                "\nDefense Effect: %s".formatted(this.getDefenseEffect()) +
                "\nHealth Effect: %s\n".formatted(this.getHealthEffect()) + p;
    }

    private static boolean occupied(ArrayList<int[]> occupied, int[] position) {
        for(int[] i:occupied) if(Arrays.toString(i).equals(Arrays.toString(position))) return true;
        return false;
    }

    private static ArrayList<int[]> getOccupiedSpaces(ArrayList<Human> humans, ArrayList<Goblin> goblins) {
        ArrayList<int[]> occupied = new ArrayList<>();
        for(Human h:humans) occupied.add(h.getPosition());
        for(Goblin g:goblins) occupied.add(g.getPosition());
        return occupied;
    }
}

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static java.util.Map.entry;

public class Human implements Player, GamePiece {
    private String name;
    private String icon;
    private final int id;
    private int damage;
    private int defense;
    private int mentalStrength;
    private int health;
    private boolean hasNotChangedName = true;
    private boolean hasNotMovedThisTurn = true;
    private int[] position = new int[]{-1,-1};
    private final ArrayList<Item> inventory = new ArrayList<>();
    private static int maxPosition = 0;
    private static ArrayList<Human> humans = new ArrayList<>();
    private static final HashMap<String, String> directions = new HashMap<>(Map.ofEntries(entry("1","Up"),
            entry("2", "Right"), entry("3", "Down"), entry("4", "Left"), entry("5", "Go back")));
    private static final HashMap<String, String> consumableItemOptions =
            new HashMap<>(Map.ofEntries(entry("1","Eat"), entry("2", "Store in inventory"),
                    entry("3", "Leave it here"), entry("4", "Stop search")));
    private static final HashMap<String, String> equippableItemOptions =
            new HashMap<>(Map.ofEntries(entry("1","Equip"), entry("2", "Store in inventory"),
                    entry("3", "Leave it here"), entry("4", "Stop search")));
    private static final HashMap<String, String> checkConsumableInventoryOptions =
            new HashMap<>(Map.ofEntries(entry("1","Eat"), entry("2", "Keep item"),
                    entry("3", "Drop item"), entry("4", "Stop search")));
    private static final HashMap<String, String> checkEquippableInventoryOptions =
            new HashMap<>(Map.ofEntries(entry("1","Equip/Unequip"), entry("2", "Keep item"),
                    entry("3", "Drop item"), entry("4", "Stop search")));
    private static final Random r = new Random();
    private static final Scanner s = new Scanner(System.in);

    public Human(int id) throws FileNotFoundException {
        getNameAndIcon(r.nextInt(2));
        this.id = id;
        this.damage = generateRandomNumberNear100(r.nextDouble()); // max damage they can inflict on a goblin, will use math.random to determine how much is inflicted each time
        this.defense = generateRandomNumberNear100(r.nextDouble()); // how well a player defends each round of combat... if defense outweighs damage, the attacking player will take damage equal to the difference
        this.mentalStrength = generateRandomNumberNear100(r.nextDouble()); // if a human's mentalStrength is higher than a goblin's aggression, they are more likely to deal critical damage
        // humans lose mental strength when their teammates die
        this.health = generateRandomNumberNear100(r.nextDouble());
    }

    private void getNameAndIcon(int zeroOrOne) throws FileNotFoundException {
        String file = "BoyNames";
        if(zeroOrOne == 1) {
            file = "GirlNames";
            this.icon = "👧";
        } else {
            this.icon = "👦";
        }
        File txt = new File("src/Java/" + file);
        int n = 0;
        for(Scanner sc = new Scanner(txt); sc.hasNext(); ) {
            ++n;
            String line = sc.nextLine();
            if(r.nextInt(n) == 0)
                this.name = line;
        }
    }

    @Override
    public String getName() {return this.name;}

    private void changeName() {
        System.out.println("What's your new name? You can also enter 0 if you want to go back." );
        String name = s.nextLine();
        if(name.equals("0")) return;
        if(name.equals(this.getName())) {
            System.out.println("That was already your name...");
            changeName();
        } else {
            System.out.println(name+"... cool!");
            this.setName(name);
        }
    }

    public void setName(String name) {
        this.name = name;
        this.hasNotChangedName = false;
    }

    @Override
    public String getIcon() {return this.icon;}

    @Override
    public void setIcon(String icon) {this.icon = icon;}

    private static int generateRandomNumberNear100(double d) {
        int posOrNeg = r.nextInt(2);
        if(posOrNeg == 0) return 100 + (int) (d * 20);
        return 100 - (int) (d * 20);
    }

    @Override
    public int getId() {return this.id;}

    public void handleMove(ArrayList<Goblin> goblins, ArrayList<Item> items) {
        System.out.println("Where would you like to move?");
        printOptions(directions);
        int x = this.position[0];
        int y = this.position[1];
        String choice = s.nextLine();
        if(choice.equals("5")) return;
        switch (choice) {
            case "1":
                if (y == 0) {
                    System.out.println("Sorry, you can't move up any further. Try again.");
                    handleMove(goblins, items);
                } else {
                    this.setPosition(new int[]{x, --y});
                    this.hasNotMovedThisTurn = false;
                }
                break;
            case "2":
                if (x + 1 == maxPosition) {
                    System.out.println("Sorry, you can't move right any further. Try again.");
                    handleMove(goblins, items);
                } else {
                    this.setPosition(new int[]{++x, y});
                    this.hasNotMovedThisTurn = false;
                }
                break;
            case "3":
                if (y + 1 == maxPosition) {
                    System.out.println("Sorry, you can't move down any further. Try again.");
                    handleMove(goblins, items);
                } else {
                    this.setPosition(new int[]{x, ++y});
                    this.hasNotMovedThisTurn = false;
                }
                break;
            case "4":
                if (x == 0) {
                    System.out.println("Sorry, you can't move left any further. Try again.");
                    handleMove(goblins, items);
                } else {
                    this.setPosition(new int[]{--x, y});
                    this.hasNotMovedThisTurn = false;
                }
                break;
            default:
                System.out.println("Sorry, your input was not recognized. Please try again.");
                handleMove(goblins, items);
                break;
        }
        this.fightGoblinsHere(goblins, items);
    }

    @Override
    public int[] getPosition() {return this.position;}

    @Override
    public void setPosition(int[] p) {this.position = p;}

    public int getMentalStrength() {return this.mentalStrength;}

    public void setMentalStrength(int difference) { // difference can be positive or negative
        int maxMentalStrength = 150;
        if(this.mentalStrength + difference > maxMentalStrength) {
            this.mentalStrength = maxMentalStrength;
        } else {
            this.mentalStrength += difference;
        }
    }

    public static void healHumansHere(int[] position) {
        for(Human h: humans) {
            if(Arrays.toString(h.getPosition()).equals(Arrays.toString(position))) {
                int initHealth = h.getHealth();
                int initMentalStrength = h.getMentalStrength();
                h.setHealth(r.nextInt(4)+1);
                h.setMentalStrength(r.nextInt(4)+1);
                System.out.printf(("%s's health increased from %s to %s, and their mental strength increased " +
                                "from %s to %s.%n"), h.getName(), initHealth, h.getHealth(), initMentalStrength,
                h.getMentalStrength());
            }
        }
    }

    @Override
    public int getHealth() {return this.health;}

    @Override
    public void setHealth(int difference) {
        int maxHealth = 150;
        int initHealth = this.getHealth();
        if(this.damage + difference > maxHealth) {
            this.health = maxHealth;
        } else {
            this.health += difference;
            if(difference > 0)
                System.out.printf("%s's health increased from %s to %s.%n", this.getName(), initHealth, this.getHealth());
            else if(difference < 0 && difference != -initHealth)
                System.out.printf("%s's health decreased from %s to %s.%n", this.getName(), initHealth, this.getHealth());
        }
    }

    @Override
    public int getDamage() {return this.damage;}

    @Override
    public void setDamage(int difference) {
        int initDamage = this.getDamage();
        this.damage += difference;
        if(difference > 0)
            System.out.printf("%s's damage increased from %s to %s.%n", this.getName(), initDamage, this.getDamage());
        else if(difference < 0 && difference != -initDamage)
            System.out.printf("%s's damage decreased from %s to %s.%n", this.getName(), initDamage, this.getDamage());
    }

    @Override
    public int getDefense() {return this.defense;}

    @Override
    public void setDefense(int difference) {
        int initDefense = this.getDefense();
        this.defense += difference;
        if(difference > 0)
            System.out.printf("%s's defense increased from %s to %s.%n", this.getName(), initDefense, this.getDefense());
        else if(difference < 0 && difference != -initDefense)
            System.out.printf("%s's defense decreased from %s to %s.%n", this.getName(), initDefense, this.getDefense());
    }

    private void checkInventory(ArrayList<Item> items) {
        ArrayList<Item> itemsToDrop = new ArrayList<>();
        ArrayList<Item> itemsToRemove = new ArrayList<>();
        for(Item i:this.getInventory()) {
            System.out.println(i);
            System.out.println("What would you like to do with this item?");
            boolean isHealthItem = i.getHealthEffect() > 0;
            if(isHealthItem) printOptions(checkConsumableInventoryOptions);
            else printOptions(checkEquippableInventoryOptions);
            String choice;
            while (true) { // guarantees the input is a choice
                choice = s.nextLine();
                if((isHealthItem && checkConsumableInventoryOptions.get(choice) == null) ||
                        (!isHealthItem && checkEquippableInventoryOptions.get(choice) == null)) {
                    System.out.println("Sorry, your input was not recognized. Please try again.");
                    continue;
                }
                break;
            }
            switch(choice) {
                case "1":
                    if(isHealthItem) { // eating item from inventory
                        this.setHealth(i.getHealthEffect());
                        itemsToRemove.add(i);
                    } else {
                        if(!i.isEquipped()) { // equipping item from inventory
                            this.unequipItemLikeThis(i);
                            this.equipThisItem(i);
                        } else {
                            this.unequipThisItem(i);
                        }
                    }
                    break;
                case "2":
                    continue;
                case "3":
                    if(i.isEquipped()) this.unequipThisItem(i);
                    itemsToDrop.add(i);
                    break;
                case "4":
                    return;
            }
        }
        for(Item i: itemsToDrop) {
            this.inventory.remove(i);
            i.setPosition(this.getPosition());
            items.add(i);
        }
        for(Item i: itemsToRemove) {
            this.inventory.remove(i);
        }
    }

    public void equipThisItem(Item i) {
        if(i.getDamageEffect() > 0) this.setDamage(i.getDamageEffect());
        if(i.getDefenseEffect() > 0) this.setDefense(i.getDefenseEffect());
        i.setEquipped(true);
    }

    public void unequipThisItem(Item i) {
        if(i.getDamageEffect() > 0) this.setDamage(-i.getDamageEffect()); //remove damage effect
        if(i.getDefenseEffect() > 0)this.setDefense(-i.getDefenseEffect()); //remove defense effect
        i.setEquipped(false); // unequip it
    }

    public void unequipItemLikeThis(Item item) {
        if(item.getDamageEffect() > 0) { //if this item is damage item
            for(Item i: this.getInventory()) { // loop through inventory
                if(i.isEquipped() && i.getDamageEffect() > 0) { // look for equipped damage item
                    i.setEquipped(false); // unequip it
                    this.setDamage(-i.getDamageEffect()); //remove damage effect
                }
            }
        } else { //if this item is a defense item
            for(Item i: this.getInventory()) { // loop through inventory
                if(i.isEquipped() && i.getDefenseEffect() > 0) { // look for equipped defense item
                    i.setEquipped(false); // unequip it
                    this.setDefense(-i.getDefenseEffect()); //remove defense effect
                }
            }
        }

    }

    @Override
    public ArrayList<Item> getInventory() {return this.inventory;}

    @Override
    public void setInventory(Item item) {this.inventory.add(item);}

    @Override
    public void dropAllItems(ArrayList<Item> items) {
        for(Item i: this.inventory) i.setPosition(this.getPosition());
        items.addAll(this.inventory);
        this.inventory.clear();
    }

    @Override
    public boolean isAlive() {return this.health > 0;}

    public static ArrayList<Human> generateHumans(int numPlayers, int numRows) throws FileNotFoundException {
        ArrayList<Human> h = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++) h.add(new Human(i+1));
        humans = h;
        placeHumans(numRows);
        return h;
    }

    public static void placeHumans(int numRows) {
        maxPosition = numRows;
        for(Human h: humans) {
            int[] pos = new int[]{r.nextInt(2), r.nextInt(numRows)};
            while(playerAlreadyThere(pos)) {
                pos[0] = r.nextInt(2);
                pos[1] = r.nextInt(numRows);
            }
            h.setPosition(pos);
        }
    }

    public static boolean notAllDead() {
        for(Human h: humans) {
            if(h.isAlive()) return true;
        }
        return false;
    }

    public Object[] getOptions(ArrayList<Item> items) {
        HashMap<String, String> options = new HashMap<>();
        ArrayList<String> choices = new ArrayList<>();
        int optionNumber = 1;
        if(this.hasNotMovedThisTurn) {
            choices.add(String.valueOf(optionNumber));
            options.put(String.valueOf(optionNumber++), "Move");
        }
        if(this.getInventory().size() > 0) {
            choices.add(String.valueOf(optionNumber));
            options.put(String.valueOf(optionNumber++), "Check inventory");
        }
        if(itemsHere(this.getPosition(), items)) {
            choices.add(String.valueOf(optionNumber));
            options.put(String.valueOf(optionNumber++), "Search for items");
        }
        if(this.hasNotChangedName) {
            choices.add(String.valueOf(optionNumber));
            options.put(String.valueOf(optionNumber++), "Change name");
        }
        choices.add(String.valueOf(optionNumber));
        options.put(String.valueOf(optionNumber), "End turn");
        return new Object[]{options, choices};
    }

    public static boolean anotherHumanHere(Human h) {
        for(Human otherHuman: humans) {
            if(otherHuman.getId() == h.getId()) continue;
            if(Arrays.toString(otherHuman.getPosition()).equals(Arrays.toString(h.getPosition()))) return true;
        }
        return false;
    }

    private static boolean playerAlreadyThere(int[] position) {
        for(Human h:humans) {
            if (Arrays.toString(h.getPosition()).equals(Arrays.toString(position))) return true;
        }
        return false;
    }

    private void searchForItems(ArrayList<Item> items) {
        ArrayList<Item> itemsToRemove = new ArrayList<>();
        for(Item i: items) {
            if(Arrays.toString(i.getPosition()).equals(Arrays.toString(this.getPosition()))) {
                System.out.println(i+"\nWhat would you like to do with this item?");
                boolean isHealthItem = i.getHealthEffect() > 0;
                if(isHealthItem) printOptions(consumableItemOptions);
                else printOptions(equippableItemOptions);
                String choice;
                while (true) { // guarantees the input is a choice
                    choice = s.nextLine();
                    if((isHealthItem && consumableItemOptions.get(choice) == null) ||
                            (!isHealthItem && equippableItemOptions.get(choice) == null)) {
                        System.out.println("Sorry, your input was not recognized. Please try again.");
                        continue;
                    }
                    break;
                }
                switch(choice) {
                    case "1":
                        if(isHealthItem) this.setHealth(i.getHealthEffect());
                        if(!isHealthItem) {
                            this.unequipItemLikeThis(i);
                            this.setInventory(i);
                            i.setEquipped(true);
                            if(i.getDamageEffect() > 0) this.setDamage(i.getDamageEffect());
                            if(i.getDefenseEffect() > 0) this.setDefense(i.getDefenseEffect());
                        }
                        itemsToRemove.add(i);
                        break;
                    case "2":
                        this.setInventory(i);
                        itemsToRemove.add(i);
                        break;
                    case "3":
                        continue;
                    case "4":
                        return;
                }
            }
        }
        for(Item i: itemsToRemove) items.remove(i);
    }

    private static boolean itemsHere(int[] playerPosition, ArrayList<Item> items) {
        for(Item i: items) if(Arrays.toString(i.getPosition()).equals(Arrays.toString(playerPosition))) return true;
        return false;
    }

    public static void printOptions(Object o) {
        HashMap<String, String> options = (HashMap<String, String>) o;
        for (Map.Entry<String, String> stringStringEntry : options.entrySet()) System.out.println(stringStringEntry);
    }

    public void handleChoice(int choice,  HashMap<String, String> options, ArrayList<Goblin> goblins,ArrayList<Item> items) {
        String value = options.get(String.valueOf(choice));
        if(value.startsWith("Move")) this.handleMove(goblins, items);
        if(value.startsWith("Change")) this.changeName();
        if(value.startsWith("Check")) this.checkInventory(items);
        if(value.startsWith("Search")) this.searchForItems(items);
    }

    private void fightGoblinsHere(ArrayList<Goblin> goblins, ArrayList<Item> items) {
        ArrayList<Goblin> goblinsHere = getGoblinsHere(goblins);
        switch(goblinsHere.size()) {
            case 0: break;
            case 1: {
                this.fightGoblin(goblinsHere.get(0), items);
                break;
            }
            default: {
                this.ambushGoblins(goblinsHere, items);
                break;
            }
        }
    }

    private ArrayList<Goblin> getGoblinsHere(ArrayList<Goblin> goblins) {
        ArrayList<Goblin> goblinsHere = new ArrayList<>();
        for(Goblin g: goblins)
            if(Arrays.toString(g.getPosition()).equals(Arrays.toString(this.getPosition())))
                goblinsHere.add(g);
        return goblinsHere;
    }

    private void fightGoblin(Goblin g, ArrayList<Item> items) {
        double totalHumanStats = this.getDamage() + this.getDefense() + this.getHealth() + this.getMentalStrength();
        double totalGoblinStats = g.getDamage() + g.getDefense() + g.getHealth() + g.getAggression();
        double chanceOfVictory = totalHumanStats / (totalHumanStats + totalGoblinStats);
        double combatNumber = Math.random();
        if(combatNumber <= chanceOfVictory) { // human wins
            this.wonCombat();
            boolean goblinHadItems = g.getInventory().size() > 0;
            g.isDead(items); //goblin drops their items
            System.out.printf("%s defeated %s in combat.%n", this.getName(), g.getName());
            if(goblinHadItems) System.out.println("It dropped something...");
        } else {
            this.isDead(); // human dies
            g.wonCombat(this); // goblin gets all the human's items
            System.out.printf("%s was defeated by %s in combat.%n", this.getName(), g.getName());
        }
    }

    public void ambushGoblins(ArrayList<Goblin> goblins, ArrayList<Item> items) {
        boolean goblinsHadItems = false;
        for(Goblin g: goblins) {
            if(g.getInventory().size() > 0 && !goblinsHadItems) goblinsHadItems = true;
            this.wonCombat();
            g.isDead(items);
        }
        System.out.printf("%s ambushed %s goblins and defeated them all.%n", this.getName(), goblins.size());
        if(goblinsHadItems) System.out.println("Looks like they dropped something...");
    }

    public void wonCombat() {
        this.setMentalStrength(-(r.nextInt(10)+1));
        this.setHealth(-(r.nextInt(10)+1));
    }

    public void isDead() {
        this.setDamage(-this.getDamage());
        this.setDefense(-this.getDefense());
        this.setHealth(-this.getHealth());
        this.setMentalStrength(-this.getMentalStrength());
        this.setPosition(new int[]{-1,-1});
    }

    public void endTurn() {this.hasNotMovedThisTurn = true;}

    public String toString() {
        return String.format("%s\n", this.getIcon()+this.getName() + " (H%s)".formatted(this.getId()))
                + String.format("%s\n", "Damage: " + this.getDamage())
                + String.format("%s\n", "Defense: " + this.getDefense())
                + String.format("%s\n", "Health: " + this.getHealth())
                + String.format("%s\n", "Mental Strength: " + this.getMentalStrength())
                + String.format("%s\n", "Position: " + Arrays.toString(this.getPosition()));
    }
}

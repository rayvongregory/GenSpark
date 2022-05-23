package gameClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Goblin implements Player, GamePiece {
    private final String name;
    private String icon;
    private final int id;
    private int damage;
    private int defense;
    private int aggression;
    private int health;
    private int[] position = new int[]{-1,-1};
    private final ArrayList<Item> inventory = new ArrayList<>();
    private static ArrayList<Goblin> goblins = new ArrayList<>();
    private static final Random r = new Random();

    public Goblin(int id) throws FileNotFoundException {
        this.name = generateName();
        this.icon = "👺";
        this.id = id;
        this.damage = generateRandomNumberNear100(r.nextDouble());
        this.defense = generateRandomNumberNear100(r.nextDouble());
        this.aggression = generateRandomNumberNear100(r.nextDouble());
        this.health = generateRandomNumberNear100(r.nextDouble());
    }

    private static String generateName() throws FileNotFoundException {
        String name = "Goblin";
        File txt = new File("src/main/java/textfiles/GoblinNames");
        int n = 0;
        for(Scanner sc = new Scanner(txt); sc.hasNext(); ) {
            ++n;
            String line = sc.nextLine();
            if(r.nextInt(n) == 0)
                name = line;
        }
        return name;
    }

    @Override
    public int getId() {
        return this.id;
    }

    private static int generateRandomNumberNear100(double d) {
        int posOrNeg = r.nextInt(2);
        if(posOrNeg == 0) return 100 + (int) (d * 20);
        return 100 - (int) (d * 20);
    }

    @Override
    public int[] getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(int[] p) {this.position = p;}

    @Override
    public String getName() {return this.name;}

    @Override
    public String getIcon() {return this.icon;}

    @Override
    public void setIcon(String icon) {this.icon = icon;}

    public int getAggression() {return this.aggression;}

    public void setAggression(int difference) {
        int maxAggression = 150;
        if(this.aggression + difference > maxAggression) {
            this.aggression = maxAggression;
        } else {
            this.aggression += difference;
        }
    }

    @Override
    public int getHealth() {return this.health;}

    @Override
    public void setHealth(int difference) {
        int maxHealth = 150;
        if(this.health + difference > maxHealth) {
            this.health = maxHealth;
        } else {
            this.health += difference;
        }
    }

    public boolean anotherGoblinHere() {
        for(Goblin otherGoblin: goblins) {
            if(otherGoblin.getId() == this.getId()) {
                continue;
            }
            if(Arrays.toString(otherGoblin.getPosition()).equals(Arrays.toString(this.getPosition()))) return true;
        }
        return false;
    }

    public static void healGoblinsHere(int[] position) {
        for(Goblin g:goblins) {
            if(Arrays.toString(g.getPosition()).equals(Arrays.toString(position))) {
                g.setHealth(r.nextInt(4)+1);
                g.setAggression(r.nextInt(4)+1);
            }
        }
    }

    @Override
    public int getDamage() {return this.damage;}

    @Override
    public void setDamage(int difference) {this.damage += difference;}

    @Override
    public int getDefense() {return this.defense;}

    @Override
    public void setDefense(int difference) {this.defense += difference;}

    public static boolean notAllDead() {
        return goblins.size() > 0;
    }

    public static ArrayList<Goblin> generateGoblins(int numPlayers, int numRows) throws FileNotFoundException {
        ArrayList<Goblin> g = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++) g.add(new Goblin(i+1));
        goblins = g;
        placeGoblins(numRows);
        return g;
    }

    public static void placeGoblins(int numRows) {
        for(Goblin g: goblins) {
            int[] pos = new int[]{r.nextInt(2) + numRows - 2, r.nextInt(numRows)};
            while(playerAlreadyThere(pos)) {
                pos[0] = r.nextInt(2) + numRows - 2;
                pos[1] = r.nextInt(numRows);
            }
            g.setPosition(pos);
        }
    }

    public static boolean playerAlreadyThere(int[] position) {
        for(Goblin g:goblins) if (Arrays.toString(g.getPosition()).equals(Arrays.toString(position))) return true;
        return false;
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
    public String toString() {
        return String.format("%s%s (G%s)\n",this.getIcon(), this.getName(), this.getId())
                + String.format("%s\n", "Damage: " + this.getDamage())
                + String.format("%s\n", "Defense: " + this.getDefense())
                + String.format("%s\n", "Health: " + this.getHealth())
                + String.format("%s\n", "Aggression: " + this.getAggression())
                + String.format("%s\n", "Position: " + Arrays.toString(this.getPosition()));
    }

    public String getStats() {
        return String.format("%s\n", "Damage: " + this.getDamage())
                + String.format("%s\n", "Defense: " + this.getDefense())
                + String.format("%s\n", "Health: " + this.getHealth())
                + String.format("%s\n", "Aggression: " + this.getAggression())
                + String.format("%s\n", "Position: " + Arrays.toString(this.getPosition()));
    }

    public static void moveGoblin(ArrayList<Human> humans, ArrayList<Item> items) {
        // console version
        Goblin g = goblins.get(0);
        Human h = getClosestHuman(g, humans);
        if(h == null) return; // do i need this?
        int humanX = h.getPosition()[0];
        int humanY = h.getPosition()[1];
        int goblinX = g.getPosition()[0];
        int goblinY = g.getPosition()[1];
        double up = getDistance(humanX, humanY, goblinX, goblinY-1);
        double right = getDistance(humanX, humanY, goblinX+1, goblinY);
        double down = getDistance(humanX, humanY, goblinX, goblinY+1);
        double left = getDistance(humanX, humanY, goblinX-1, goblinY);
        double stay = getDistance(humanX, humanY, goblinX, goblinY);
        double shortestDistance = getShortestDistance(up, right, down, left, stay);
        if(shortestDistance == up) g.setPosition(new int[]{goblinX, goblinY-1});
        else if(shortestDistance == right) g.setPosition(new int[]{goblinX+1, goblinY});
        else if(shortestDistance == down) g.setPosition(new int[]{goblinX, goblinY+1});
        else if(shortestDistance == left) g.setPosition(new int[]{goblinX-1, goblinY});
        else if(shortestDistance == stay) g.setPosition(new int[]{goblinX, goblinY});
        g.pickUpItemsHere(items);
        Goblin.healGoblinsHere(g.getPosition());
        g.fightTheHumansHere(humans, items);
        goblins.remove(g);
        goblins.add(g);
    }

    public Human moveThisGoblin (ArrayList<Human> humans) {
        // gui version - only moves closer to human and returns the human
        Human h = getClosestHuman(this, humans);
        int humanX = h.getPosition()[0];
        int humanY = h.getPosition()[1];
        int goblinX = this.getPosition()[0];
        int goblinY = this.getPosition()[1];
        double up = getDistance(humanX, humanY, goblinX, goblinY-1);
        double right = getDistance(humanX, humanY, goblinX+1, goblinY);
        double down = getDistance(humanX, humanY, goblinX, goblinY+1);
        double left = getDistance(humanX, humanY, goblinX-1, goblinY);
        double stay = getDistance(humanX, humanY, goblinX, goblinY);
        double shortestDistance = getShortestDistance(up, right, down, left, stay);
        if(shortestDistance == up) this.setPosition(new int[]{goblinX, goblinY-1});
        else if(shortestDistance == right) this.setPosition(new int[]{goblinX+1, goblinY});
        else if(shortestDistance == down) this.setPosition(new int[]{goblinX, goblinY+1});
        else if(shortestDistance == left) this.setPosition(new int[]{goblinX-1, goblinY});
        else if(shortestDistance == stay) this.setPosition(new int[]{goblinX, goblinY});
        return h;
    }

    public void endTurn() {
        goblins.remove(this);
        goblins.add(this);
    }

    private static double getDistance(int humanX, int humanY, int goblinX, int goblinY) {
        return StrictMath.hypot((goblinX-humanX),(goblinY-humanY));
    }

    private static double getShortestDistance(double up, double right, double down, double left, double stay) {
        double shortestDistance = Integer.MAX_VALUE;
        for(Double d: new double[]{up, right, down, left, stay}) if(d < shortestDistance) shortestDistance = d;
        return shortestDistance;
    }

    private static Human getClosestHuman(Goblin g, ArrayList<Human> humans) {
        double shortestDistance = Integer.MAX_VALUE;
        Human closestHuman = null;
        for(Human h: humans) {
            double distance = getDistance(h.getPosition()[0], h.getPosition()[1], g.getPosition()[0], g.getPosition()[1]);
            if(distance < shortestDistance) {
                shortestDistance = distance;
                closestHuman = h;
            }
        }
        return closestHuman;
    }

    public void pickUpItemsHere(ArrayList<Item> items) {
        ArrayList<Item> itemsToRemove = new ArrayList<>();
        for(Item i: items) {
            if(Arrays.toString(i.getPosition()).equals(Arrays.toString(this.getPosition()))) {
                this.setInventory(i);
                itemsToRemove.add(i);
            }
        }
        for(Item i:itemsToRemove) items.remove(i);
    }

    public void fightTheHumansHere(ArrayList<Human> humans, ArrayList<Item> items) {
        ArrayList<Human> humansHere = this.getHumansHere(humans);
        switch(humansHere.size()) {
            case 0: break;
            case 1: {
                this.fightHuman(humansHere.get(0), items, Math.random());
                break;
            }
            default: {
                this.ambushHumans(humansHere);
                break;
            }
        }
    }

    public ArrayList<Human> getHumansHere(ArrayList<Human> humans) {
        ArrayList<Human> humansHere = new ArrayList<>();
        for(Human h: humans)
            if(Arrays.toString(h.getPosition()).equals(Arrays.toString(this.getPosition()))) humansHere.add(h);
        return humansHere;
    }

    private void fightHuman(Human h, ArrayList<Item> items, double combatNumber) {
        double totalHumanStats = h.getDamage() + h.getDefense() + h.getHealth() + h.getMentalStrength();
        double totalGoblinStats = this.getDamage() + this.getDefense() + this.getHealth() + this.getAggression();
        double chanceOfVictory = totalGoblinStats / (totalHumanStats + totalGoblinStats);
        if(combatNumber <= chanceOfVictory) { // goblin wins
            boolean humanHadItems = h.getInventory().size() > 0;
            this.wonCombat(h);
            h.isDead(); // human dies
            System.out.printf("%s defeated %s in combat.%n", this.getName(), h.getName());
            if(humanHadItems) System.out.printf("%s now has all of %s's items.%n", this.getName(), h.getName());
        } else {
            boolean goblinHadItems = this.getInventory().size() > 0;
            this.isDead(items); //goblin dies and drops all items
            h.wonCombat(); //human wins
            System.out.printf("%s was defeated by %s in combat.%n", this.getName(), h.getName());
            if(goblinHadItems) System.out.printf("%s dropped something...%n", this.getName());
        }
    }

    private void ambushHumans(ArrayList<Human> humans) {
        for(Human h:humans) {
            this.wonCombat(h);
            h.isDead();
        }
    }

    public void isDead(ArrayList<Item> items) {
        this.setHealth(-this.getHealth());
        this.dropAllItems(items);
        goblins.remove(this);
    }

    public void wonCombat(Human h) {
        this.setAggression(-(r.nextInt(10)+1));
        this.setHealth(-(r.nextInt(10)+1));
        for(Item i: h.getInventory()) i.setEquipped(false);
        this.getInventory().addAll(h.getInventory());
        h.getInventory().clear();
    }
}

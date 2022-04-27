import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GoblinTest {

    ArrayList<Goblin> threeGoblins = Goblin.generateGoblins(3, 5);
    ArrayList<Human> threeHumans = Human.generateHumans(3, 5);
    ArrayList<Item> items = Item.generateItems(3, 5, threeHumans, threeGoblins);
    Goblin firstGoblin = threeGoblins.get(0);
    Goblin secondGoblin = threeGoblins.get(1);
    Goblin thirdGoblin = threeGoblins.get(2);
    Human firstHuman = threeHumans.get(0);
    Human secondHuman = threeHumans.get(1);
    Human thirdHuman = threeHumans.get(2);

    GoblinTest() throws FileNotFoundException {
    }

    @Test
    void setHealthTest() {
        thirdGoblin.setHealth(thirdGoblin.getHealth());
        assertEquals(150, thirdGoblin.getHealth());
        int doubleHealth = secondGoblin.getHealth() * 2;
        secondGoblin.setHealth(secondGoblin.getHealth());
        assertNotEquals(doubleHealth, secondGoblin.getHealth());
        firstGoblin.setHealth(-firstGoblin.getHealth());
        assertEquals(0, firstGoblin.getHealth());
    }

    @Test
    void setDamageTest() {
        int initDamage = thirdGoblin.getDamage();
        thirdGoblin.setDamage(thirdGoblin.getDamage());
        assertEquals(initDamage * 2, thirdGoblin.getDamage());
        initDamage = secondGoblin.getDamage();
        secondGoblin.setDamage(10);
        assertNotEquals(initDamage + 5, secondGoblin.getDamage());
        firstGoblin.setDamage(-firstGoblin.getDamage());
        assertEquals(0, firstGoblin.getDamage());
    }

    @Test
    void setDefenseTest() {
        int initDefense = thirdGoblin.getDefense();
        thirdGoblin.setDefense(thirdGoblin.getDefense());
        assertEquals(initDefense * 2, thirdGoblin.getDefense());
        initDefense = secondGoblin.getDefense();
        secondGoblin.setDefense(10);
        assertNotEquals(initDefense + 5, secondGoblin.getDefense());
        firstGoblin.setDefense(-firstGoblin.getDefense());
        assertEquals(0, firstGoblin.getDefense());
    }

    @Test
    void setAggressionTest() {
        thirdGoblin.setHealth(thirdGoblin.getHealth());
        assertEquals(150, thirdGoblin.getHealth());
        int doubleHealth = secondGoblin.getHealth() * 2;
        secondGoblin.setHealth(secondGoblin.getHealth());
        assertNotEquals(doubleHealth, secondGoblin.getHealth());
        firstGoblin.setHealth(-firstGoblin.getHealth());
        assertEquals(0, firstGoblin.getHealth());
    }

    @Test
    void notAllDeadTest() {
        firstGoblin.isDead(items);
        secondGoblin.isDead(items);
        assertTrue(Goblin.notAllDead());
        thirdGoblin.isDead(items);
        assertFalse(Goblin.notAllDead());
        thirdGoblin.setHealth(100);
        assertTrue(Goblin.notAllDead());
        thirdGoblin.isDead(items);
        assertTrue(!Goblin.notAllDead());
    }

    @Test
    void isDeadTest() {
        firstGoblin.isDead(items);
        assertEquals(0, firstGoblin.getDamage());
        assertEquals(0, firstGoblin.getDefense());
        assertEquals(0, firstGoblin.getHealth());
        assertEquals(0, firstGoblin.getAggression());
        assertEquals(0, firstGoblin.getInventory().size());
        assertEquals(Arrays.toString(new int[]{-1, -1}), Arrays.toString(firstGoblin.getPosition()));
        assertNotEquals(10, firstGoblin.getDamage());
        assertNotEquals(10, firstGoblin.getDefense());
        assertNotEquals(10, firstGoblin.getHealth());
        assertNotEquals(10, firstGoblin.getAggression());
        assertNotEquals(1, firstGoblin.getInventory().size());
        assertNotEquals(Arrays.toString(new int[]{-3, -1}), Arrays.toString(firstGoblin.getPosition()));
    }

    @Test
    void playerAlreadyThereTest() {
        firstGoblin.setPosition(new int[]{4, 3});
        secondGoblin.setPosition(new int[]{5, 1});
        thirdGoblin.setPosition(new int[]{4, 0});
        assertTrue(Goblin.playerAlreadyThere(firstGoblin.getPosition()));
        assertTrue(Goblin.playerAlreadyThere(secondGoblin.getPosition()));
        assertTrue(Goblin.playerAlreadyThere(thirdGoblin.getPosition()));
        assertFalse(Goblin.playerAlreadyThere(new int[]{4, 4}));
        assertFalse(Goblin.playerAlreadyThere(new int[]{5, 4}));
        assertFalse(Goblin.playerAlreadyThere(new int[]{3, 0}));
    }

    @Test
    void getShortestDistanceTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getShortestDistance = firstGoblin.getClass().getDeclaredMethod("getShortestDistance", double.class, double.class, double.class, double.class, double.class);
        getShortestDistance.setAccessible(true);
        assertEquals(0.0, getShortestDistance.invoke(firstGoblin, 0, 2, 4, 0, 0));
        assertNotEquals(4.0, getShortestDistance.invoke(firstGoblin, 0, 2, 4, 0, 0));
        assertEquals(1.2, getShortestDistance.invoke(firstGoblin,1.2,1.222,1.2224,1.22222,1.22229));
        assertNotEquals(0.3, getShortestDistance.invoke(firstGoblin,0.0, 0.1, 0.05, 0.15, 0.2));
    }

    @Test
    void ambushHumansTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method ambushHumans = firstGoblin.getClass().getDeclaredMethod("ambushHumans", ArrayList.class);
        ambushHumans.setAccessible(true);
        int initGoblinHealth = firstGoblin.getHealth();
        int initGoblinAggression = firstGoblin.getAggression();
        ambushHumans.invoke(firstGoblin, new ArrayList<>(Arrays.asList(firstHuman, secondHuman)));
        assertEquals(0, firstHuman.getHealth());
        assertEquals(0, secondHuman.getHealth());
        assertTrue(initGoblinAggression > firstGoblin.getAggression());
        assertFalse(initGoblinAggression < firstGoblin.getAggression());
        assertTrue(initGoblinHealth > firstGoblin.getHealth());
        assertFalse(initGoblinHealth < firstGoblin.getHealth());
    }

    @Test
    void wonCombatTest() throws FileNotFoundException {
        firstHuman.setInventory(Item.generateAttackItem());
        firstHuman.setInventory(Item.generateDefenseItem());
        firstHuman.setInventory(Item.generateHealthItem());
        secondHuman.setInventory(Item.generateHealthItem());
        firstGoblin.wonCombat(firstHuman);
        assertEquals(3, firstGoblin.getInventory().size());
        assertEquals(0, firstHuman.getInventory().size());
        firstGoblin.wonCombat(secondHuman);
        assertEquals(4, firstGoblin.getInventory().size());
        assertEquals(0, secondHuman.getInventory().size());
        thirdHuman.setInventory(Item.generateAttackItem());
    }

    @Test
    void fightHumanTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, FileNotFoundException {
        Method fightHuman = firstGoblin.getClass().getDeclaredMethod("fightHuman", Human.class, ArrayList.class, double.class);
        fightHuman.setAccessible(true);
        int initNumItems = items.size();
        firstHuman.setDamage(150);
        firstHuman.setDefense(150);
        firstHuman.setHealth(150);
        firstHuman.setMentalStrength(150);
        firstGoblin.setDamage(-firstGoblin.getDamage());
        firstGoblin.setDefense(-firstGoblin.getDefense());
        firstGoblin.setHealth(-firstGoblin.getHealth());
        firstGoblin.setAggression(-firstGoblin.getAggression());
        firstGoblin.setInventory(Item.generateAttackItem());
        firstGoblin.setInventory(Item.generateDefenseItem());
        fightHuman.invoke(firstGoblin, firstHuman, items, 0.4);
        assertEquals(initNumItems+2, Item.getItems().size());

        initNumItems = items.size();
        secondHuman.setDamage(-secondHuman.getDamage());
        secondHuman.setDefense(-secondHuman.getDefense());
        secondHuman.setHealth(-secondHuman.getHealth());
        secondHuman.setMentalStrength(-secondHuman.getMentalStrength());
        secondGoblin.setDamage(150);
        secondGoblin.setDefense(150);
        secondGoblin.setHealth(150);
        secondGoblin.setAggression(150);
        secondHuman.setInventory(Item.generateAttackItem());
        secondHuman.setInventory(Item.generateDefenseItem());
        secondHuman.setInventory(Item.generateHealthItem());
        fightHuman.invoke(secondGoblin, secondHuman, items, 0.5);
        assertEquals(initNumItems, Item.getItems().size());
        assertEquals(3, secondGoblin.getInventory().size());
    }

    @Test
    void pickUpItemsHereTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int initNumItems = items.size();
        Item item0 = items.get(0);
        Item item1 = items.get(1);
        int[] position = new int[]{2,4};
        item0.setPosition(position);
        item1.setPosition(position);
        firstGoblin.setPosition(position);
        Method pickUpItemsHere = firstGoblin.getClass().getDeclaredMethod("pickUpItemsHere", Goblin.class, ArrayList.class);
        pickUpItemsHere.setAccessible(true);
        pickUpItemsHere.invoke(null, firstGoblin, items);
        assertEquals(initNumItems-2, items.size());
    }

    @Test
    void getClosestHumanTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getClosestHuman = firstGoblin.getClass().getDeclaredMethod("getClosestHuman", Goblin.class, ArrayList.class);
        getClosestHuman.setAccessible(true);
        firstHuman.setPosition(new int[]{3,3});
        secondHuman.setPosition(new int[]{5,5});
        thirdHuman.setPosition(new int[]{1,5});
        firstGoblin.setPosition(new int[]{3,4});
        secondGoblin.setPosition(new int[]{4,5});
        thirdGoblin.setPosition(new int[]{0,5});
        assertEquals(firstHuman,getClosestHuman.invoke(null, firstGoblin, threeHumans));
        assertEquals(secondHuman,getClosestHuman.invoke(null, secondGoblin, threeHumans));
        assertEquals(thirdHuman,getClosestHuman.invoke(null, thirdGoblin, threeHumans));
    }
}
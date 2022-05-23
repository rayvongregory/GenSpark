package gameClasses;

import java.util.ArrayList;

public interface Player {
    int getHealth();
    int getDamage();
    int getId();
    void setIcon(String icon);
    void setDamage(int d);
    void setHealth(int d);
    int getDefense();
    void setDefense(int d);
    ArrayList<Item> getInventory();
    void setInventory(Item item);
    void dropAllItems(ArrayList<Item> items);
}

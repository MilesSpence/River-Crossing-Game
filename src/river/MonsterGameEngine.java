package river;

import java.awt.*;
import java.util.HashMap;

public class MonsterGameEngine extends AbstractGameEngine {

    public static final Item MONSTER1 = Item.ITEM_0;
    public static final Item MUNCHKIN1 = Item.ITEM_1;
    public static final Item MONSTER2 = Item.ITEM_2;
    public static final Item MUNCHKIN2 = Item.ITEM_3;
    public static final Item MONSTER3 = Item.ITEM_4;
    public static final Item MUNCHKIN3 = Item.ITEM_5;

    public MonsterGameEngine() {
        itemMap = new HashMap() {{
            put(MONSTER1, new GameObject("Mo", Location.START, Color.blue));
            put(MUNCHKIN1, new GameObject("Mu", Location.START, Color.pink));
            put(MONSTER2, new GameObject("Mo", Location.START, Color.blue));
            put(MUNCHKIN2, new GameObject("Mu", Location.START, Color.pink));
            put(MONSTER3, new GameObject("Mo", Location.START, Color.blue));
            put(MUNCHKIN3, new GameObject("Mu", Location.START, Color.pink));
        }};
    }

    @Override
    public int numberOfItems() {
        return itemMap.size();
    }

    @Override
    public void rowBoat() {
        assert (boatLocation != Location.BOAT);
        for (Item item : Item.values()) {
            if (!(item.ordinal() < numberOfItems())) break;
            if(getItemLocation(item) == Location.BOAT) {
                if (boatLocation == Location.START) {
                    boatLocation = Location.FINISH;
                } else if (boatLocation == Location.FINISH) {
                    boatLocation = Location.START;
                }
                break;
            }
        }
    }

    @Override
    public boolean gameIsLost() {
        // The first element of each array represents the number of the item
        // at the start and the second element is the number at the finish.
        int[] numMonsters = {0,0};
        int[] numMunchkins = {0,0};
        int i = 0;
        // Count how many monsters and munchkins are at the start and the finish
        for (Item item : Item.values()) {
            if (!(item.ordinal() < numberOfItems())) break;
            if (i % 2 == 0) {
                // If the item is a monster
                checkItemLocation(item, numMonsters);
            } else {
                // Else the item is a munchkin
                checkItemLocation(item, numMunchkins);
            }
            i++;
        }
        // Now check if the game is lost
        if (numMunchkins[0] == 0) {
            // If there are no munchkins at the start
            return numMonsters[1] > numMunchkins[1];
        } else if (numMunchkins[1] == 0) {
            // If there are no munchkins at the finish
            return numMonsters[0] > numMunchkins[0];

        } else {
            // Else there are munchkins at the start and the finish
            return numMonsters[0] > numMunchkins[0] || numMonsters[1] > numMunchkins[1];
        }
    }

    private void checkItemLocation(Item item, int[] numItems) {
        if (getItemLocation(item) == Location.START) {
            // If the item is at the start
            numItems[0]++;
        } else if (getItemLocation(item) == Location.FINISH) {
            // Else if the item is at the finish
            numItems[1]++;
        } else {
            // Else the item is on the boat
            if (getBoatLocation() == Location.START) {
                // If the boat is at the start
                numItems[0]++;
            } else if (getBoatLocation() == Location.FINISH) {
                // Else if the boat is at the finish
                numItems[1]++;
            }
        }
    }
}

package river;

import java.awt.*;
import java.util.HashMap;

public class FarmerGameEngine extends AbstractGameEngine {

    public static final Item BEANS = Item.ITEM_0;
    public static final Item GOOSE = Item.ITEM_1;
    public static final Item WOLF = Item.ITEM_2;
    public static final Item FARMER = Item.ITEM_3;

    public FarmerGameEngine() {
        itemMap = new HashMap() {{
            put(BEANS, new GameObject("B", Location.START, Color.CYAN));
            put(GOOSE, new GameObject("G", Location.START, Color.CYAN));
            put(WOLF, new GameObject("W", Location.START, Color.CYAN));
            put(FARMER, new GameObject("", Location.START, Color.MAGENTA));
        }};
    }

    @Override
    public int numberOfItems() {
        return itemMap.size();
    }

    @Override
    public void rowBoat() {
        assert (boatLocation != Location.BOAT);
        if (boatLocation == Location.START && getItemLocation(FARMER) == Location.BOAT) {
            boatLocation = Location.FINISH;
        } else if (boatLocation == Location.FINISH && getItemLocation(FARMER) == Location.BOAT) {
            boatLocation = Location.START;
        }
    }

    @Override
    public boolean gameIsLost() {
        if (getItemLocation(GOOSE) == Location.BOAT || getItemLocation(GOOSE) == getItemLocation(FARMER)
                || getItemLocation(GOOSE) == boatLocation) {
            return false;
        }
        return getItemLocation(GOOSE) == getItemLocation(WOLF) || getItemLocation(GOOSE) == getItemLocation(BEANS);
    }
}

package river;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGameEngine implements GameEngine {

    protected Map<Item, GameObject> itemMap = new HashMap<>();
    protected Location boatLocation = Location.START;
    protected int boatCapacity = 2;

    @Override
    abstract public int numberOfItems();

    @Override
    public String getItemLabel(Item item) {
        return itemMap.get(item).getLabel();
    }

    @Override
    public Color getItemColor(Item item) {
        return itemMap.get(item).getColor();
    }

    @Override
    public Location getItemLocation(Item item) {
        return itemMap.get(item).getLocation();
    }

    @Override
    public void setItemLocation(Item item, Location location) {
        itemMap.get(item).setLocation(location);
    }

    @Override
    public Location getBoatLocation() {
        return boatLocation;
    }

    @Override
    public void loadBoat(Item item) {
        if (itemMap.get(item).getLocation() == boatLocation && boatCapacity > 0) {
            itemMap.get(item).setLocation(Location.BOAT);
            boatCapacity--;
        }
    }

    @Override
    public void unloadBoat(Item item) {
        if (itemMap.get(item).getLocation() == Location.BOAT) {
            itemMap.get(item).setLocation(boatLocation);
            boatCapacity++;
        }
    }

    @Override
    public void rowBoat() {
        assert (boatLocation != Location.BOAT);
        boatLocation = (boatLocation == Location.START) ? Location.FINISH : Location.START;
    }

    @Override
    public boolean gameIsWon() {
        for (Item item : Item.values()) {
            if (!(item.ordinal() < numberOfItems())) break;
            if (getItemLocation(item) != Location.FINISH) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean gameIsLost() {
        return false;
    }

    @Override
    public void resetGame() {
        for (Item item : Item.values()) {
            if (!(item.ordinal() < numberOfItems())) break;
            itemMap.get(item).setLocation(Location.START);
        }
        boatLocation = Location.START;
        boatCapacity = 2;
    }
}

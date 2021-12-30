package river;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Graphical interface for the River application
 *
 * @author Gregory Kulczycki and Miles Spence
 */
public class RiverGUI extends JPanel implements MouseListener {

    // ==========================================================
    // Private Fields
    // ==========================================================

    private final Map<Item, Rectangle> itemRectangleMap = new HashMap() {{
        put(Item.ITEM_0, new Rectangle(20, 215, 50, 50));
        put(Item.ITEM_1, new Rectangle(80, 215, 50, 50));
        put(Item.ITEM_2, new Rectangle(20, 155, 50, 50));
        put(Item.ITEM_3, new Rectangle(80, 155, 50, 50));
        put(Item.ITEM_4, new Rectangle(140, 155, 50, 50));
        put(Item.ITEM_5, new Rectangle(140, 215, 50, 50));
    }};
    private final int[] itemXOffset = {0, 60, 0, 60, 0, 60}; // Each element corresponds to an item's x offset
    private final int[] itemYOffset = {60, 60, 0, 0, 120, 120}; // Each element corresponds to an item's y offset
    private final int[] boatXOffset = {0, 410}; // Used to add 0 when boat is at the start or 410 when at the finish
    private int numItemsPaintedOnBoat = 0;
    private Rectangle boatRectangle = new Rectangle(140, 275, 110, 50);
    private final Rectangle restartFarmerGameRect = new Rectangle(290, 120, 100, 30);
    private final Rectangle restartMonsterGameRect = new Rectangle(400, 120, 100, 30);
    private GameEngine engine; // Model
    private boolean gameIsOver = false;

    // ==========================================================
    // Constructor
    // ==========================================================

    public RiverGUI() {
        engine = new FarmerGameEngine();
        addMouseListener(this);
    }

    // ==========================================================
    // Paint Methods (View)
    // ==========================================================

    /**
     * Create the GUI and show it. For thread safety, this method should be invoked
     * from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        // Create and set up the window
        JFrame frame = new JFrame("RiverCrossing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane
        RiverGUI newContentPane = new RiverGUI();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);

        // Display the window
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(RiverGUI::createAndShowGUI);
    }

    @Override
    public void paintComponent(Graphics g) {
        // Update every rectangle's location
        updateLocationsOfItemRectangles();
        updateLocationOfBoatRectangle();

        // Set the background color to gray
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        // Paint every rectangle
        for (Item item : Item.values()) {
            if (!(item.ordinal() < engine.numberOfItems())) break;
            paintRectangle(g, engine.getItemColor(item), engine.getItemLabel(item), itemRectangleMap.get(item));
        }
        paintRectangle(g, Color.ORANGE, "", boatRectangle);

        // Check if the game is over
        if (engine.gameIsLost()) {
            paintMessage("You Lost!", g);
            gameIsOver = true;
        } else if (engine.gameIsWon()) {
            paintMessage("You Won!", g);
            gameIsOver = true;
        }

        // If the game is over, then offer to restart the game
        if (gameIsOver) {
            g.setColor(Color.BLACK);
            // Paint farmer game restart button
            g.fillRect(restartFarmerGameRect.x - 3, restartFarmerGameRect.y - 3,
                    restartFarmerGameRect.width + (2 * 3), restartFarmerGameRect.height + (2 * 3));
            paintRectangle(g, Color.PINK, "Farmer", restartFarmerGameRect);
            // Paint monster game restart button
            g.fillRect(restartMonsterGameRect.x - 3, restartMonsterGameRect.y - 3,
                    restartMonsterGameRect.width + (2 * 3), restartMonsterGameRect.height + (2 * 3));
            paintRectangle(g, Color.PINK, "Monster", restartMonsterGameRect);
        }
    }

    private void updateLocationsOfItemRectangles() {
        for (Item item : Item.values()) {
            if (!(item.ordinal() < engine.numberOfItems())) break;
            if (engine.getItemLocation(item) == Location.START) {
                // If the item is at the start
                itemRectangleMap.put(item, new Rectangle(20 + itemXOffset[item.ordinal()],
                        155 + itemYOffset[item.ordinal()], 50, 50));
                /* Each statement like the above adds the minimum x and y values for all
                 * items to the corresponding offset of the specific item. For example, the
                 * third munchkin is in the bottom right corner of all the items. Therefore,
                 * its x value is 20 + itemXOffset[item.ordinal()] (which is equivalent to
                 * itemXOffset[5] or 60) and its y value is 155 + itemYOffset[item.ordinal()]
                 * (which is equivalent to itemYOffset[5] or 120). So, (80, 275).
                 */
            } else if (engine.getItemLocation(item) == Location.FINISH) {
                // Else if the item is at the finish
                itemRectangleMap.put(item, new Rectangle(670 + itemXOffset[item.ordinal()],
                        155 + itemYOffset[item.ordinal()], 50, 50));
            } else {
                // Else the item is on the boat
                itemRectangleMap.put(item, new Rectangle(140 + boatXOffset[engine.getBoatLocation().ordinal()]
                        + itemXOffset[numItemsPaintedOnBoat], 215, 50, 50));
                numItemsPaintedOnBoat++;
            }
        }
        numItemsPaintedOnBoat = 0;
    }

    private void updateLocationOfBoatRectangle() {
        boatRectangle = new Rectangle(140 + boatXOffset[engine.getBoatLocation().ordinal()],
                275, 110, 50);
    }

    private void paintRectangle(Graphics g, Color itemColor, String itemLabel, Rectangle rectangle) {
        // Paint the Rectangle
        g.setColor(itemColor);
        g.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

        // Write the item's label inside the rectangle
        g.setColor(Color.BLACK);
        int fontSize = (rectangle.height >= 40) ? 36 : 18;
        if (itemLabel.equals("Mu") || itemLabel.equals("Mo")) {
            // Shrink the font if it's a monster or munchkin
            fontSize = 24;
        }
        g.setFont(new Font("Verdana", Font.BOLD, fontSize));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(itemLabel, rectangle.x + rectangle.width / 2 - fm.stringWidth(itemLabel) / 2,
                rectangle.y + rectangle.height / 2 + fontSize / 2 - 4);
    }

    public void paintMessage(String message, Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Verdana", Font.BOLD, 36));
        FontMetrics fm = g.getFontMetrics();
        g.drawString(message, 400 - fm.stringWidth(message) / 2, 100);
    }

    // ==========================================================
    // MouseListener Methods (Controller)
    // ==========================================================

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameIsOver) {
            // The game is over (either lost or won)
            if (this.restartFarmerGameRect.contains(e.getPoint())) {
                // If the farmer game button is pressed
                engine = new FarmerGameEngine();
            } else if (this.restartMonsterGameRect.contains(e.getPoint())) {
                // Else if the monster game button is pressed
                engine = new MonsterGameEngine();
            }
            // Reset the game
            engine.resetGame();
            gameIsOver = false;
            repaint();
            return;
        }

        for (Item item : Item.values()) {
            if (!(item.ordinal() < engine.numberOfItems())) break;
            // Respond to the click of any item
            clickItem(item, e);
        }
        if (boatRectangle.contains(e.getPoint())) {
            // Respond to the click of the boat
            engine.rowBoat();
            // Reminder: it is not necessary to check if the boat is allowed
            // to be rowed because the game engines already check this
        }
        repaint();
    }

    // A couple private helper methods for the mouseClicked method
    private void clickItem(Item item, MouseEvent e) {
        if (itemRectangleMap.get(item).contains(e.getPoint())) {
            // If the item is clicked
            if (engine.getItemLocation(item) == Location.START) {
                // If the item is at the start
                if (getBoatCapacity() > 0) {
                    // If there's room on the boat
                    engine.loadBoat(item);
                }
            } else if (engine.getItemLocation(item) == Location.FINISH) {
                // Else if the item is at the finish
                if (getBoatCapacity() > 0) {
                    // If there's room on the boat
                    engine.loadBoat(item);
                }
            } else {
                // Else the item is on the boat and is always free to unload
                engine.unloadBoat(item);
            }
        }
    }

    private int getBoatCapacity() {
        int boatCapacity = 2;
        for (Item item : Item.values()) {
            if (!(item.ordinal() < engine.numberOfItems())) break;
            if (engine.getItemLocation(item) == Location.BOAT) {
                boatCapacity--;
            }
        }
        return boatCapacity;
    }

    // ----------------------------------------------------------
    // None of these methods will be used
    // ----------------------------------------------------------

    @Override
    public void mousePressed(MouseEvent e) {
        //
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }
}

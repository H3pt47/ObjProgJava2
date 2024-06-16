package view;

import model.Wall;
import model.World;

/**
 * A view that prints the current state of the world to the console upon every
 * update.
 */
public class ConsoleView implements View {

    @Override
    public void update(World world) {
        // The player's position
        int playerX = world.getPlayerX();
        int playerY = world.getPlayerY();

        for (int row = 0; row < world.getHeight(); row++) {
            for (int col = 0; col < world.getWidth(); col++) {
                // If the player is here, print #, otherwise print .
                if (row == playerY && col == playerX) {
                    System.out.print("#");
                } else if (world.getWalls().contains(new Wall(row, col))){
                    System.out.print("W");
                } else if (col == world.getEndX() && row == world.getEndY()){
                    System.out.print("T");
                }else {
                    System.out.print(".");
                }
            }

            // A newline after every row
            System.out.println();
        }

        // A newline between every update
        System.out.println();
    }

    @Override
    public void newLevel(World world) {
        update(world);
    }

}

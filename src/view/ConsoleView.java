package view;

import model.Enemies.Enemies;
import model.Wall;
import model.World;

import java.util.ArrayList;
import java.util.List;

/**
 * A view that prints the current state of the world to the console upon every
 * update.
 */
public class ConsoleView implements View {

    /**
     * Creates and updates the console view with '#' for rows, 'W' for walls,
     * 'T' for target, E for enemy. The rest is filled with dots.
     * @param world updates the given world.
     */
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
                } else if (world.getWalls().contains(new Wall(col, row))){
                    System.out.print("W");
                } else if (col == world.getEndX() && row == world.getEndY()){
                    System.out.print("T");
                }else if (isEnemy(world.getEnemies(), col, row)){
                    System.out.print("E");
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

    /**
     * Updates the world.
     * @param world the given world.
     */
    @Override
    public void newLevel(World world) {
        update(world);
    }

    /**
     * Checks if an enemy is at a given x-and y coordinate.
     * @param enemies Arraylist of enemies.
     * @param x x-coordinate.
     * @param y y-coordinate.
     * @return If an enemy is at a given x-and y coordinate.
     */
    private Boolean isEnemy(List<Enemies> enemies, int x, int y){
        for (Enemies e : enemies) {
            if (e.getX() == x && e.getY() == y){
                return true;
            }
        }
        return false;
    }

}

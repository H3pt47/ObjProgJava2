package model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Each instance of this class represents a level, that is loaded into the world, for the player to enjoy.
 */
public class Level {

    private final int _lenX;
    private final int _lenY;
    private final String _name;
    private final ArrayList<Wall> _walls;
    private final int _startX;
    private final int _startY;

    /**
     * Make a level. Yeah! It's just that easy..... Maybe not.
     * @param lenX the length of the game board in the X-axis
     * @param lenY The length of the game board in the Y-axis
     * @param name The name of the level
     * @param walls An ArrayList, that contains all the Walls. It is not checked, if theses are valid, so beware.
     * @param startX Starting coordinate for the player. It's of the X variety...
     * @param startY Starting coordinate for the player. It's of the Y variety...
     */
    public Level(int lenX, int lenY, String name, ArrayList<Wall> walls, int startX, int startY) {
        _lenX = lenX;
        _lenY = lenY;
        _name = name;
        _walls = walls;
        _startX = startX;
        _startY = startY;
    }

    public int getLenX() {
        return _lenX;
    }
    public int getLenY() {
        return _lenY;
    }
    public String getName() {
        return _name;
    }
    public ArrayList<Wall> getWalls() {
        return _walls;
    }
    public int getStartX() {
        return _startX;
    }
    public int getStartY() {
        return _startY;
    }
}

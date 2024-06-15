package model;

import java.util.ArrayList;

import view.View;

/**
 * The world is our model. It saves the bare minimum of information required to
 * accurately reflect the state of the game. Note how this does not know
 * anything about graphics.
 */
public class World {

    /** The world's width. */
    private final int width;
    /** The world's height. */
    private final int height;
    /** The player's x position in the world. */
    private int _playerX;
    /** The player's y position in the world. */
    private int _playerY;

    private ArrayList<Wall> _walls;

    /** Set of views registered to be notified of world updates. */
    private final ArrayList<View> views = new ArrayList<>();

    /**
     * Creates a new world with the given size.t
     */
    public World(Level level) {
        // Normally, we would check the arguments for proper values
        // Well...but you guys didn't and I am lazy, so we will see if i do that when i clean up around here...
        //TODO valueCheck N+ etc
        this.width = level.getLenX();
        this.height = level.getLenY();
        this._walls = level.getWalls();

        this._playerX = level.getStartX();
        this._playerY = level.getStartY();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getters and Setters

    /**
     * Returns the width of the world.
     *
     * @return the width of the world.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the world.
     *
     * @return the height of the world.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the player's x position.
     *
     * @return the player's x position.
     */
    public int getPlayerX() {
        return _playerX;
    }

    /**
     * Sets the player's x position.
     *
     * @param playerX the player's x position.
     */
    public void setPlayerX(int playerX) {
        //check if value is out of bounds
        playerX = Math.max(0, playerX);
        playerX = Math.min(getWidth() - 1, playerX);
        //check for valid position e.g. no wall
        if(newPosCheck(playerX, _playerY)){
            this._playerX = playerX;
            updateViews();
        }

    }

    /**
     * Returns the player's y position.
     *
     * @return the player's y position.
     */
    public int getPlayerY() {
        return _playerY;
    }

    /**
     * Sets the player's y position.
     *
     * @param playerY the player's y position.
     */
    public void setPlayerY(int playerY) {
        playerY = Math.max(0, playerY);
        playerY = Math.min(getHeight() - 1, playerY);
        if(newPosCheck(_playerX, playerY)){
            this._playerY = playerY;
            updateViews();
        }
    }

    /**
     * Returns the walls in an ArrayList.
     * @return The walls in an ArrayList
     */
    public ArrayList<Wall> getWalls() {
        return _walls;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Player Management

    /**
     * Moves the player along the given direction.
     *
     * @param direction where to move.
     */
    public void movePlayer(Direction direction) {
        // The direction tells us exactly how much we need to move along
        // every direction
        setPlayerX(getPlayerX() + direction.deltaX);
        setPlayerY(getPlayerY() + direction.deltaY);
    }

    ///////////////////////////////////////////////////////////////////////////
    // View Management

    /**
     * Adds the given view of the world and updates it once. Once registered through
     * this method, the view will receive updates whenever the world changes.
     *
     * @param view the view to be registered.
     */
    public void registerView(View view) {
        views.add(view);
        view.update(this);
    }

    /**
     * Updates all views by calling their {@link View#update(World)} methods.
     */
    private void updateViews() {
        for (View view : views) {
            view.update(this);
        }
    }

    private boolean newPosCheck(int playerX, int playerY){
        for (Wall wall : _walls) {
            if (wall.x() == playerX && wall.y() == playerY){
                return false;
            }
        }
        return true;
    }

}

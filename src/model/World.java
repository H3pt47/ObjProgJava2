package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import controller.Labyrinth;
import model.Enemies.Enemies;
import view.View;

/**
 * The world is our model. It saves the bare minimum of information required to
 * accurately reflect the state of the game. Note how this does not know
 * anything about graphics.
 */
public class World {

    /** The world's width. */
    private int width;
    /** The world's height. */
    private int height;
    /** The player's x position in the world. */
    private int _playerX;
    /** The player's y position in the world. */
    private int _playerY;

    private Direction _playerDirection;

    private int _endX;

    private int _endY;

    private ArrayList<Wall> _walls;

    /** Set of views registered to be notified of world updates. */
    private final ArrayList<View> views = new ArrayList<>();

    private Level _level;

    private ArrayList<Enemies> _enemies;

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

        this._playerDirection = Direction.NONE;

        this._endX = level.getEndX();
        this._endY = level.getEndY();

        this._level = level;

        this._enemies = new ArrayList<>(level.getEnemies());
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
        if(noWallChecker(playerX, _playerY)){
            this._playerX = playerX;
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
        if(noWallChecker(_playerX, playerY)){
            this._playerY = playerY;
        }
    }

    public Direction getPlayerDirection() {
        return _playerDirection;
    }

    /**
     * Returns the walls in an ArrayList.
     * @return The walls in an ArrayList
     */
    public ArrayList<Wall> getWalls() {
        return _walls;
    }

    public int getEndX() {
        return _endX;
    }
    public int getEndY() {
        return _endY;
    }

    public ArrayList<Enemies> getEnemies() {
        return _enemies;
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
        _playerDirection = direction;
        setPlayerX(getPlayerX() + direction.deltaX);
        setPlayerY(getPlayerY() + direction.deltaY);
        if(enemyChecker(_playerX, _playerY)){
            levelReset();
        }
        moveEnemies();
        updateViews();
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
        //If end was reached
        if (this._playerX == this._endX && this._playerY == this._endY) {
            Labyrinth.loadNextLevel();
        }
    }

    public boolean enemyChecker(int X, int Y){
        for (Enemies e: _enemies){
            if (e.getX() == X && e.getY() == Y){
                return true;
            }
        }
        return false;
    }

    public boolean noWallChecker(int X, int Y){
        return (!_walls.contains(new Wall(X, Y)));
    }

    public boolean boundsChecker(int X, int Y){
        return (X >= 0 && Y >= 0 && X < width && Y < height);
    }

    public boolean posCheckEnemies(int X, int Y){
        return (noWallChecker(X,Y) && boundsChecker(X,Y) && !enemyChecker(X,Y));
    }

    public void newLevel(Level level){
        this._level = level;

        this.width = level.getLenX();
        this.height = level.getLenY();
        this._walls = level.getWalls();

        this._playerX = level.getStartX();
        this._playerY = level.getStartY();
        this._endX = level.getEndX();
        this._endY = level.getEndY();
        this._enemies = new ArrayList<>(level.getEnemies());
        for (View view : views) {
            view.newLevel(this);
        }
        updateViews();
    }

    public void levelReset(){
        _playerDirection = Direction.NONE;
        _playerX = _level.getStartX();
        _playerY = _level.getStartY();
        this._enemies = new ArrayList<>(_level.getEnemies());
        _enemies.forEach(Enemies::reset);
        updateViews();
    }

    private void moveEnemies() {
        Iterator<Enemies> it = _enemies.iterator();
        while(it.hasNext()){
            Enemies e = it.next();
            e.update(this);
        }
    }

    public void newEnemy(Enemies e){
        this._enemies.add(e);
        updateViews();
    }
}

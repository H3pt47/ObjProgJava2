package model;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.Labyrinth;
import model.Enemies.Enemies;
import values.path;
import values.pathCoordinate;
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
    /** The direction the player is facing.*/
    private model.Direction _playerDirection;
    /** The end X - coordinate of the world*/
    private int _endX;
    /** The end Y - coordinate of the world*/
    private int _endY;
    /** An Arraylist that stores all walls of a world.*/
    private ArrayList<Wall> _walls;

    /** Set of views registered to be notified of world updates. */
    private final ArrayList<View> views = new ArrayList<>();

    /** The level that is loaded*/
    private Level _level;
    /** A list that stores enemies*/
    private List<Enemies> _enemies;
    /** Boolean that allows userinput*/
    private Boolean _userInputEnabled;

    //timing management currently not in use :(
    private static final int DELAY = 100;

    /** Map with the Directions to the Player*/
    private Map<pathCoordinate, path> _paths;

    private int slashX;
    private int slashY;
    private int slashCoolDown;

    /**
     * Creates a new world with the given level.
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

        this._enemies = new CopyOnWriteArrayList<>(level.getEnemies());

        this._paths = new HashMap<>();
        calcPaths();
        slashReset();
        this._userInputEnabled = true;
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
        if(noWallChecker(playerX, _playerY) && noDeactivatedEnemyChecker(playerX, _playerY)){
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
        if(noWallChecker(_playerX, playerY) && noDeactivatedEnemyChecker(_playerX, playerY)){
            this._playerY = playerY;
        }
    }

    /**
     * Returns the Direction the player is facing.
     *
     * @return The Direction the player is facing.
     */
    public Direction getPlayerDirection() {
        return _playerDirection;
    }

    /**
     * Returns the walls in an ArrayList.
     *
     * @return The walls in an ArrayList
     */
    public ArrayList<Wall> getWalls() {
        return _walls;
    }

    /**
     * Returns The End X-coordinate of the world.
     *
     * @return The End X-coordinate of the world.
     */
    public int getEndX() {
        return _endX;
    }

    /**
     * Returns the End Y-coordinate of the world.
     *
     * @return The End Y-coordinate of the world.
     */
    public int getEndY() {
        return _endY;
    }

    /**
     * Returns the List where the enemies are stored.
     *
     * @return The List where the enemies are stored.
     */
    public List<Enemies> getEnemies() {
        return _enemies;
    }

    /**
     *
     *
     * @return weiss selber nicht.
     */
    public Map<pathCoordinate, path> getPaths() {
        return _paths;
    }
    public int getSlashX(){
        return slashX;
    }
    public int getSlashY(){
        return slashY;
    }
    public int getSlashCoolDown(){
        return slashCoolDown;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Player Management

    /**
     * Moves the player along the given direction.
     *
     * @param direction where to move.
     */
    public void movePlayer(Direction direction) {
        if (_userInputEnabled){
            _userInputEnabled = false;

            if (slashCoolDown > 0){
                slashCoolDown--;
            } else{
                slashReset();
            }

            // The direction tells us exactly how much we need to move along
            // every direction
            _playerDirection = direction;
            setPlayerX(getPlayerX() + direction.deltaX);
            setPlayerY(getPlayerY() + direction.deltaY);
            if(enemyChecker(_playerX, _playerY)){
                levelReset();
            }
            calcPaths();
            moveEnemies();
            updateViews();
            _userInputEnabled = true;
        }

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
            Labyrinth.loadNewLevel();
        }
    }

    /**
     *
     * @param X The X-coordinate that should be checked for enemies
     * @param Y The Y-coordinate that should be checked for enemies
     * @return if there is an enemiy at the given Point.
     */
    public boolean enemyChecker(int X, int Y){
        for (Enemies e: _enemies){
            if (e.getX() == X && e.getY() == Y && !e.isDead()){
                return true;
            }
        }
        return false;
    }

    /**
     * Method to check for walls
     * @param X The X coordinate
     * @param Y The Y coordinate
     * @return Returns True if no Wall present, else False
     */
    public boolean noWallChecker(int X, int Y){
        return (!_walls.contains(new Wall(X, Y)));
    }

    /**
     * Method to check if coordinates are in bounds
     * @param X The X coordinate
     * @param Y The Y coordinate
     * @return Returns True if in Bounds, else False
     */
    public boolean boundsChecker(int X, int Y){
        return (X >= 0 && Y >= 0 && X < width && Y < height);
    }

    /**
     *
     * @param X X-coordinate that is checked for no walls, no bounds and no enemies.
     * @param Y Y-coordinate that is checked for no walls, no bounds and no enemies.
     * @return if there is no wall and no bound and no enemy.
     */
    public boolean posCheckEnemies(int X, int Y){
        return (noWallChecker(X,Y) && boundsChecker(X,Y) && !enemyChecker(X,Y));
    }

    public boolean noDeactivatedEnemyChecker(int x, int y){
        for (Enemies e: _enemies){
            if (e.getX() == x && e.getY() == y && !e.isActivated() && !e.isDead()){
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param level the level that is created.
     */
    public void newLevel(Level level){
        this._level = level;

        this.width = level.getLenX();
        this.height = level.getLenY();
        this._walls = level.getWalls();

        this._playerX = level.getStartX();
        this._playerY = level.getStartY();
        this._endX = level.getEndX();
        this._endY = level.getEndY();
        this._enemies = new CopyOnWriteArrayList<>(level.getEnemies());
        _paths.clear();
        calcPaths();
        slashReset();
        for (View view : views) {
            view.newLevel(this);
        }
        updateViews();
    }

    /**
     * Resets the level to the beginning.
     */
    public void levelReset(){
        _playerDirection = Direction.NONE;
        _playerX = _level.getStartX();
        _playerY = _level.getStartY();
        this._enemies = new CopyOnWriteArrayList<>(_level.getEnemies());
        _enemies.forEach(Enemies::reset);
        _paths.clear();
        calcPaths();
        slashReset();
        updateViews();
    }

    /**
     *  Moves the enemies.
     */
    private void moveEnemies() {
        Iterator<Enemies> it = _enemies.iterator();
        while (it.hasNext()) {
            Enemies e = it.next();
            e.update(this);
            updateViews();
        }
    }

    /**
     * Adds an Enemy to the Level. This Action is not permanent. A reset will revert it.
     * @param e Enemy that is added to the _enemies Array.
     */
    public void newEnemy(Enemies e){
        this._enemies.add(e);
        updateViews();
    }

    /**
     * calculates the Paths for every Path shorter than _foresight to the Player and saves it in a HashMap _paths.
     */
    private void calcPaths(){
        _paths = new HashMap<>();
        pathCoordinate pc = new pathCoordinate(_playerX, _playerY);
        List<Direction> l = new ArrayList<>();
        l.add(Direction.NONE);
        path p = new path(l);
        _paths.put(pc, p);
        //left
        ArrayList<Direction> left = new ArrayList<>(l);
        left.add(Direction.RIGHT);
        calcFromThisPoint(new pathCoordinate(_playerX - 1, _playerY), left);
        //right
        ArrayList<Direction> right = new ArrayList<>(l);
        right.add(Direction.LEFT);
        calcFromThisPoint(new pathCoordinate(_playerX + 1, _playerY), right);
        //up
        ArrayList<Direction> up = new ArrayList<>(l);
        up.add(Direction.DOWN);
        calcFromThisPoint(new pathCoordinate(_playerX, _playerY - 1), up);
        //down
        ArrayList<Direction> down = new ArrayList<>(l);
        down.add(Direction.UP);
        calcFromThisPoint(new pathCoordinate(_playerX, _playerY + 1), down);

    }

    /**
     * A Helper Method for calcPaths.
     * @param point
     * @param path
     */
    private void calcFromThisPoint(pathCoordinate point, ArrayList<Direction> path){
        //check if all routes that are necessary are calculated.
        if (path.size() >= Labyrinth.getFORESIGHT()){return;}
        //check if new field is valid
        if(noWallChecker(point.x(), point.y()) && boundsChecker(point.x(), point.y())){
            //int currentX = point.x();
            //int currentY = point.y();
            //boolean shouldContinue = true;

            if (!_paths.containsKey(point)) {
                _paths.put(point, new path(new ArrayList<>(path)));
                branchPathCalc(point, path);
            } else if (_paths.get(point).getLength() > path.size()){
                _paths.get(point).setPath(new ArrayList<>(path));
                branchPathCalc(point, path);
            }
        }
    }

    /**
     * A Helper Method for calcFromThisPoint. It calculates branching off the main path
     * @param point The Point, from which one moves to the last
     * @param path The current Path
     */
    private void branchPathCalc(pathCoordinate point, ArrayList<Direction> path){
        if(path.isEmpty()){return;}
        switch (path.get(path.size()-1)) {
            //if the last Direction was Vertical you branch Left and Right.
            case UP, DOWN -> {
                ArrayList<Direction> newPathLeft = new ArrayList<>(path);
                newPathLeft.add(Direction.RIGHT);
                calcFromThisPoint(new pathCoordinate(point.x() - 1, point.y()), newPathLeft);

                ArrayList<Direction> newPathRight = new ArrayList<>(path);
                newPathRight.add(Direction.LEFT);
                calcFromThisPoint(new pathCoordinate(point.x() + 1, point.y()), newPathRight);
            }
            // if the last Direction was Horizontal you branch Up and Down.
            case LEFT, RIGHT -> {
                ArrayList<Direction> newPathUp = new ArrayList<>(path);
                newPathUp.add(Direction.DOWN);
                calcFromThisPoint(new pathCoordinate(point.x(), point.y() - 1), newPathUp);

                ArrayList<Direction> newPathDown = new ArrayList<>(path);
                newPathDown.add(Direction.UP);
                calcFromThisPoint(new pathCoordinate(point.x(), point.y() + 1), newPathDown);
            }
        }
        ArrayList<Direction> pathContinue = new ArrayList<>(path);
        pathContinue.add(path.get(path.size()-1));
        calcFromThisPoint(new pathCoordinate(point.x() - path.get(path.size()-1).deltaX, point.y() - path.get(path.size()-1).deltaY), pathContinue);
    }

    /**
     * Resets the slash
     */
    private void slashReset(){
        slashX = -1;
        slashY = -1;
        slashCoolDown = 0;
    }

    /**
     * Makes a slash around the Player, killing any Enemy standing in the 3x3 around him. It has a CoolDown of 6 Turns.
     */
    public void doSlash(){
        if (slashCoolDown == 0){
            slashX = _playerX;
            slashY = _playerY;
            slashCoolDown = 6;
            for (Enemies e : _enemies) {
                if ((e.getX() == _playerX - 1 || e.getX() == _playerX || e.getX() == _playerX + 1)
                        && (e.getY() == _playerY - 1 || e.getY() == _playerY || e.getY() == _playerY + 1)){
                    e.kill();
                }
            }
            updateViews();
            moveEnemies();
        }
    }
}

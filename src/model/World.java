package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import controller.Labyrinth;
import model.Enemies.Enemies;
import model.Interactable.Interactable;
import model.level.Level;
import values.Direction;
import values.Wall;
import values.path;
import values.coordinate;
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
    private Direction _playerDirection;
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
    /** A Map of Interactable objects*/
    private Map<coordinate, Interactable> _interactables;
    /** If you can see the path to the end */
    private boolean _canSeePath;
    /** Boolean that allows user input*/
    private Boolean _userInputEnabled;

    //timing management currently not in use :(
    private static final int DELAY = 100;

    /** Map with the Directions to the Player*/
    private Map<coordinate, path> _paths;
    //Boolean to recalculate the pathfinding
    private boolean _didPlayerMove = false;

    //Slashing
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

        this._interactables = level.get_interactables();

        this._canSeePath = false;

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
     * @return the player's x position.
     */
    public int getPlayerX() {
        return _playerX;
    }

    /**
     * Sets the player's x position.
     * @param playerX the player's x position.
     */
    public void setPlayerX(int playerX) {
        //check if value is out of bounds
        playerX = Math.max(0, playerX);
        playerX = Math.min(getWidth() - 1, playerX);
        //check for valid position e.g. no wall
        if(noWallChecker(playerX, _playerY) && noDeactivatedEnemyChecker(playerX, _playerY) && noInteractableChecker(playerX, _playerY)){
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
        if(noWallChecker(_playerX, playerY) && noDeactivatedEnemyChecker(_playerX, playerY) && noInteractableChecker(_playerX, playerY)){
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
     * Getter method for the Paths map.
     * @return The Map, where the key is a coordinate and the item is a path, containing the Direction to go to the player and the distance to them.
     */
    public Map<coordinate, path> getPaths() {
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

    public Map<coordinate, Interactable> get_interactables() {
        return _interactables;
    }

    public boolean getCanSeePath() {
        return _canSeePath;
    }

    public void setCanSeePath(boolean canSeePath) {
        _canSeePath = canSeePath;
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

            doSlashCooldown();

            coordinate tempPosition = new coordinate(_playerX, _playerY);

            // The direction tells us exactly how much we need to move along
            // every direction
            _playerDirection = direction;
            setPlayerX(getPlayerX() + direction.deltaX);
            setPlayerY(getPlayerY() + direction.deltaY);
            if(enemyChecker(_playerX, _playerY)){
                levelReset();
            } else{
                _didPlayerMove = !tempPosition.equals(new coordinate(_playerX, _playerY));
                calcPaths();
                moveEnemies();
                updateViews();
            }

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

    //////////////////////////Checker for Things///////////////////////////
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
     * This method checks for no interactable object on the target field.
     * @param x The X coordinate
     * @param y The Y coordinate
     * @return True if no interactable at the tile, false otherwise.
     */
    public boolean noInteractableChecker(int x, int y){
        return !_interactables.containsKey(new coordinate(x,y));
    }

    public boolean validTileChecker(int x, int y){
        return noWallChecker(x,y) && boundsChecker(x,y) && noInteractableChecker(x,y);
    }

    public boolean allEnemiesDead(){
        return _enemies.stream().allMatch(Enemies::isDead);
    }

    ////////////////// LOADING AND RELOADING //////////////////////

    /**
     * Opens a new Level in the world.
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

        this._playerDirection = Direction.NONE;
        this._didPlayerMove = false;

        this._interactables = level.get_interactables();
        this._canSeePath = false;

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
        //Player
        _playerDirection = Direction.NONE;
        _playerX = _level.getStartX();
        _playerY = _level.getStartY();
        this._didPlayerMove = false;
        //Enemies
        this._enemies = new CopyOnWriteArrayList<>(_level.getEnemies());
        _enemies.forEach(Enemies::reset);
        //Pathing
        _paths.clear();
        calcPaths();

        this._canSeePath = false;
        //Slash
        slashReset();

        updateViews();
    }
    ///////////////////// ENEMIES ////////////////////////
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

    /////////////////////////// PATHING ///////////////////////////

    /**
     * calculates the Paths for every Path to the Player and saves it in a HashMap _paths.
     */
    private void calcPaths(){
        if (_didPlayerMove){
            recalculatePathsExtended();
        } else if (_paths.isEmpty()){
            initiatePaths();
            recalculatePathsExtended();
        }

    }

    private void initiatePaths(){
        Queue<coordinate> queue = new LinkedList<>();
        coordinate playerPosition = new coordinate(_playerX, _playerY);
        _paths.put(playerPosition, new path(Direction.NONE, 1));
        queue.add(playerPosition);
        int currentLength;
        while (!queue.isEmpty()){
            coordinate current = queue.poll();
            currentLength = _paths.get(current).getLength();

            for (Direction direction : Direction.values()){
                if (direction != Direction.NONE){
                    int newX = current.x() - direction.deltaX;
                    int newY = current.y() - direction.deltaY;
                    coordinate newPosition = new coordinate(newX, newY);
                    if (_paths.containsKey(newPosition)){
                        if (_paths.get(newPosition).getLength() > currentLength){
                            _paths.get(newPosition).updatePath(new path(direction, currentLength + 1));
                            if (!queue.contains(newPosition)){
                                queue.add(newPosition);
                            }
                        }
                    } else if (validTileChecker(newX, newY)){
                        _paths.put(newPosition, new path(direction, currentLength + 1));
                        if (!queue.contains(newPosition)){
                            queue.add(newPosition);
                        }
                    }
                }
            }
        }
    }

    private void recalculatePathsExtended(){
        Queue<coordinate> queue = new LinkedList<>();
        Set<coordinate> visited = new HashSet<>();
        coordinate playerPosition = new coordinate(_playerX, _playerY);
        _paths.get(playerPosition).updatePath(new path(Direction.NONE, 1));
        queue.add(playerPosition);
        visited.add(playerPosition);
        while (!queue.isEmpty()){
            coordinate current = queue.poll();
            int currentLength = _paths.get(current).getLength();

            for (Direction direction : Direction.values()) {
                if (direction != Direction.NONE){
                    int newX = current.x() - direction.deltaX;
                    int newY = current.y() - direction.deltaY;
                    coordinate neighbor = new coordinate(newX, newY);
                    //check if already visited
                    if (!visited.contains(neighbor) && (_paths.containsKey(neighbor))){
                        _paths.get(neighbor).updatePath(new path(direction, currentLength + 1));
                        queue.add(neighbor);
                        visited.add(neighbor);
                    }
                }
            }
        }
    }
    /**
     * Helper method to recalculate the pathing. It's a simplified version of the algorithm that simply updates the nearest 4 squares.
     * Use instead of extended version if game very slow and the pathing algorithm might be the cause.
     */
    private void recalculatePathsLight(){
        coordinate playerPosition = new coordinate(_playerX, _playerY);
        _paths.get(playerPosition).updatePath(new path(Direction.NONE, 1));
        for (Direction direction : Direction.values()){
            if (direction != Direction.NONE){
                int newX = playerPosition.x() - direction.deltaX;
                int newY = playerPosition.y() - direction.deltaY;
                coordinate newPosition = new coordinate(newX, newY);
                if (_paths.containsKey(newPosition)){
                    _paths.get(newPosition).updatePath(new path(direction, 2));
                }
            }
        }
    }
    /**
     * Get the Coordinates and Directions for the shortest path from the player to the end square.
     * It already handles the inversion of the other pathing algorithms, so that every square has the correct Direction.
      * @return A Map, where the key is a pathCoordinate and the item is a Direction.
     */
    public Map<coordinate, Direction> getPathToEnd(){
        Map<coordinate, Direction> pathToEnd = new HashMap<>();
        coordinate prevPos = new coordinate(_endX, _endY);
        coordinate currentPos = new coordinate(_endX + _paths.get(prevPos).getDirection().deltaX, _endY + _paths.get(prevPos).getDirection().deltaY);
        while (currentPos.x() != _playerX || currentPos.y() != _playerY){

            pathToEnd.put(currentPos, Direction.getOppositeDirection(_paths.get(prevPos).getDirection()));
            prevPos = currentPos;
            currentPos = new coordinate(
                    prevPos.x() + _paths.get(prevPos).getDirection().deltaX,
                    prevPos.y() + _paths.get(prevPos).getDirection().deltaY);
        }
        pathToEnd.put(currentPos, Direction.getOppositeDirection(_paths.get(prevPos).getDirection()));
        return pathToEnd;
    }

    /////////////////////// SLASH /////////////////////////////////

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

    private void doSlashCooldown(){
        if (slashCoolDown > 0){
            slashCoolDown--;
        } else{
            slashReset();
        }
    }

    ////////////////////////// INTERACTABLE ///////////////////////////

    /**
     * Tries to interact with an object in front of the player.
     */
    public void doInteraction(){
        coordinate c = new coordinate(_playerX + _playerDirection.deltaX, _playerY + _playerDirection.deltaY);
        if (_interactables.containsKey(c)){
            _interactables.get(c).interact(this);
        }
        this.doSlashCooldown();
        this.moveEnemies();
        this.updateViews();
    }

    ////////////////////// AUTOSOLVE ////////////////////////////

    public void autoSolve(){
        if(allEnemiesDead() && _canSeePath){
            _userInputEnabled = false;
            int delay = 50;
            Timer t = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    if (getPathToEnd().size() < 2){
                        t.cancel();
                    }
                    doAutoStep();
                }
            };
            t.scheduleAtFixedRate(task, 0, delay);
            _userInputEnabled = true;
        }
    }

    private void doAutoStep(){
        this.movePlayerAuto(getPathToEnd().get(new coordinate(_playerX, _playerY)));
    }

    private void movePlayerAuto(Direction direction){
        _didPlayerMove = true;
        doSlashCooldown();
        _playerDirection = direction;
        setPlayerX(getPlayerX() + direction.deltaX);
        setPlayerY(getPlayerY() + direction.deltaY);
        calcPaths();
        updateViews();
    }
}

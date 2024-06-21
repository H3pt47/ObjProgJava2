package model;

import controller.Labyrinth;
import model.Enemies.Dijkstremy;
import model.Enemies.Enemies;
import model.Enemies.Randemy;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.Stack;

public class LevelGenerator {
    private int width, height;
    private Cell[][] grid;
    private ArrayList<Wall> walls;
    private Random random = new Random();
    private int playerX;
    private int playerY;
    private int endX;
    private int endY;

    private int _numberOfHalls;
    private int _hallCoolDown;

    private int _spawnChance;

    private ArrayList<Enemies> _enemies;

    public LevelGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new Cell[width][height];
        walls = new ArrayList<>();
        _enemies = new ArrayList<>();
        this._numberOfHalls = (width * height) / (54);
        _hallCoolDown = 4;
        _spawnChance = 5 - Labyrinth.getDifficulty() * 2;
        initializeGrid();
    }

    public int getPlayerX(){
        return playerX;
    }
    public int getPlayerY(){
        return playerY;
    }
    public int getEndX(){
        return endX;
    }
    public int getEndY(){
        return endY;
    }
    public ArrayList<Enemies> get_enemies(){
        return _enemies;
    }

    private void initializeGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell(x, y, true);
            }
        }
    }

    private void resetGrid(){
        grid = new Cell[width][height];
        walls = new ArrayList<>();
        _enemies = new ArrayList<>();
        recalcHalls();
        recalcSpawnChance();
        initializeGrid();
    }

    public void generateMaze() {
        resetGrid();
        Stack<Cell> stack = new Stack<>();
        Cell start = grid[random.nextInt(width / 2) * 2][random.nextInt(height / 2) * 2]; // Start from a random cell
        playerX = start.x;
        playerY = start.y;
        start.isWall = false;
        stack.push(start);

        while (!stack.isEmpty()) {
            Cell current = stack.peek();
            Cell next = getRandomUnvisitedNeighbor(current);

            if (next != null) {
                next.isWall = false;
                removeWall(current, next);
                if(hallMaker(current, next, stack)){
                    _numberOfHalls--;
                };
                stack.push(next);
                _hallCoolDown--;
            } else {
                stack.pop();
            }
        }
        //creates random endPoint
        do {
            endX = random.nextInt(width);
            endY = random.nextInt(height);
        } while (grid[endX][endY].isWall);
    }

    private Cell getRandomUnvisitedNeighbor(Cell cell) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        // Check the neighbor two cells to the left
        if (cell.x > 1 && grid[cell.x - 2][cell.y].isWall) {
            neighbors.add(grid[cell.x - 2][cell.y]);
        }
        // Check the neighbor two cells to the right
        if (cell.x < width - 2 && grid[cell.x + 2][cell.y].isWall) {
            neighbors.add(grid[cell.x + 2][cell.y]);
        }
        // Check the neighbor two cells above
        if (cell.y > 1 && grid[cell.x][cell.y - 2].isWall) {
            neighbors.add(grid[cell.x][cell.y - 2]);
        }
        // Check the neighbor two cells below
        if (cell.y < height - 2 && grid[cell.x][cell.y + 2].isWall) {
            neighbors.add(grid[cell.x][cell.y + 2]);
        }

        if (neighbors.isEmpty()) {
            return null;
        }
        return neighbors.get(random.nextInt(neighbors.size()));
    }

    /**
     * Removes the Wall in between current and next and next.
     * @param current First Cell. It should already be marked as a non Wall
     * @param next Second Cell. This one is getting de-wall-ified.
     */
    private void removeWall(Cell current, Cell next) {
        int x = (current.x + next.x) / 2;
        int y = (current.y + next.y) / 2;
        grid[x][y].isWall = false;
    }

    /**
     * Returns the ArrayList of Walls after generation.
     * @return The complete List of Walls
     */
    public ArrayList<Wall> getWalls() {
        ArrayList<Wall> remainingWalls = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y].isWall) {
                    remainingWalls.add(new Wall(x, y));
                }
            }
        }
        return remainingWalls;
    }

    private boolean hallMaker(Cell current, Cell next, Stack<Cell> stack) {
        if(_numberOfHalls == 0
                || _hallCoolDown > 0
                || !(0 < ((current.x + next.x) / 2) - 4
                && width > ((current.x + next.x) / 2) + 4
                && 0 < ((current.y + next.y) / 2) - 4
                && height > ((current.y + next.y) / 2) + 4)){return false;}
        //checks if the pair of tiles is horizontal or vertical and checks for borderPositions
        // and if a 3x3 Room can be created.
        if (current.y == next.y
                && grid[current.x][current.y - 2].isWall
                && grid[next.x][next.y - 2].isWall) {
            removeWall(current, grid[current.x][current.y - 2]);
            grid[current.x][current.y - 2].isWall = false;
            stack.push(grid[current.x][current.y - 2]);

            removeWall(next, grid[next.x][next.y - 2]);
            grid[current.x][next.y - 2].isWall = false;
            stack.push(grid[next.x][next.y - 2]);

            grid[(current.x + next.x) / 2][current.y - 1].isWall = false;
            grid[(current.x + next.x) / 2][current.y - 2].isWall = false;

            spawnEnemy((current.x + next.x) / 2, (current.y - 1));
            return true;
        }
        else if(current.y == next.y
                && grid[next.x][next.y + 2].isWall
                && grid[current.x][current.y + 2].isWall) {

            removeWall(current, grid[current.x][current.y + 2]);
            grid[current.x][current.y + 2].isWall = false;
            stack.push(grid[current.x][current.y + 2]);

            removeWall(next, grid[next.x][next.y + 2]);
            grid[next.x][next.y + 2].isWall = false;
            stack.push(grid[next.x][next.y + 2]);

            grid[(current.x + next.x) / 2][current.y + 1].isWall = false;
            grid[(current.x + next.x) / 2][current.y + 2].isWall = false;

            spawnEnemy((current.x + next.x) / 2, (current.y + 1));
            return true;
        }
        else if(current.x == next.x
                && grid[current.x - 2][current.y].isWall
                && grid[current.x - 2][next.y].isWall){
            removeWall(current, grid[current.x - 2][current.y]);
            grid[current.x - 2][current.y].isWall = false;
            stack.push(grid[current.x - 2][current.y]);

            removeWall(next, grid[current.x - 2][next.y]);
            grid[current.x - 2][next.y].isWall = false;
            stack.push(grid[current.x - 2][next.y]);

            grid[current.x - 1][(current.y + next.y) / 2].isWall = false;
            grid[current.x - 2][(current.y + next.y) / 2].isWall = false;

            spawnEnemy(current.x - 1, (current.y + next.y) / 2);

            return true;
        }
        else if(current.x == next.x
                && grid[current.x + 2][current.y].isWall
                && grid[current.x + 2][next.y].isWall){
            removeWall(current, grid[current.x + 2][current.y]);
            grid[current.x + 2][current.y].isWall = false;
            stack.push(grid[current.x + 2][current.y]);

            removeWall(next, grid[current.x + 2][next.y]);
            grid[current.x + 2][next.y].isWall = false;
            stack.push(grid[current.x + 2][next.y]);

            grid[current.x + 1][(current.y + next.y) / 2].isWall = false;
            grid[current.x + 2][(current.y + next.y) / 2].isWall = false;

            spawnEnemy(current.x + 1, (current.y + next.y) / 2);
             return true;
        }
        return false;
    }

    private void recalcHalls(){
        this._numberOfHalls = (width * height) / (54);
        this._hallCoolDown = 4;
    }

    private void spawnEnemy(int x, int y){
        Random r = new Random();

        if (r.nextInt(0, _spawnChance) == 0){
            _enemies.add(new Dijkstremy(x, y, true, false));
        }
    }

    public void recalcSpawnChance(){
        _spawnChance = 5 - Labyrinth.getDifficulty() * 2;
    }
}

class Cell {
    int x, y;
    boolean isWall;

    Cell(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
    }
}

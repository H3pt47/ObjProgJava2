package controller;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

import GameWindow.*;
import model.Direction;
import model.*;
import model.Enemies.Dijkstremy;
import model.Enemies.Enemies;
import model.Enemies.Randemy;
import values.keyPresses;
import view.ConsoleView;
import view.GraphicView;

/**
 * This is our main program. It is responsible for creating all of the objects
 * that are part of the MVC pattern and connecting them with each other.
 */
public class Labyrinth {

    private static String TITEL;
    private static int SCALE_X;
    private static int SCALE_Y;
    private static Boolean BORDERLESS;
    private static String LANGUAGE;
    private static int DIFFICULTY;

    private static int SIZE_X;
    private static int SIZE_Y;

    private static MainMenu mainMenu;
    private static World world;
    private static Dimension fieldDimensions;
    private static GraphicView gview;
    private static ConsoleView cview;
    private static Controller controller;

    private static LevelGenerator _generator;

    private static ArrayList<keyPresses> _mazeKeys;
    private static ArrayList<keyPresses> _menuKeys;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                // set parameter
                paramSetup();

                // Create a new game world.
                _generator.generateMaze();

                world = new World(new Level(SIZE_X, SIZE_Y, "GENERATED", _generator.getWalls(), _generator.getPlayerX(), _generator.getPlayerY(), _generator.getEndX(), _generator.getEndY(), _generator.get_enemies()));

                // Size of a field in the graphical view.
                fieldDimensions = new Dimension(SCALE_X, SCALE_Y);
                // Create and register graphical view.
                gview = new GraphicView(
                        world.getWidth() * fieldDimensions.width,
                        world.getHeight() * fieldDimensions.height,
                        fieldDimensions,
                        world);
                world.registerView(gview);

                // Create and register console view.
                cview = new ConsoleView();
                world.registerView(cview);

                // Register Keys
                registerMazeKeys();
                registerMenuKeys();

                // Create Main Menu
                mainMenu = new MainMenu(TITEL);

                // Create controller and initialize JFrame.
                setupController(world, gview, mainMenu);

                //gview.setOffset(controller);
                gview.setController(controller);

                controller.setVisible(true);
            }
        });
    }

    /////////////////// HELPER METHODS ////////////////////////////////

    /**
     * Sets up the controller with the right settings.
     * @param world The world in which the level gets loaded
     * @param gview
     * @param mainMenu
     */
    private static void setupController(World world, GraphicView gview, MainMenu mainMenu){
        controller = new Controller(world, gview, mainMenu, _mazeKeys, _menuKeys);
        controller.setExtendedState(JFrame.MAXIMIZED_BOTH);
        controller.setTitle(TITEL);
        controller.setUndecorated(BORDERLESS);
        controller.setResizable(false);
        controller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add controller to the action listener in menu
        mainMenu.setController(controller);

        //display MainMenu on startup
        controller.showMainMenu();

        //displays Main menu on startup / reload
        controller.setVisible(true);
    }

    public static void reset(){
        controller.dispose();
        setupController(world, gview, mainMenu);
        controller.setVisible(true);
    }

    private static void registerMazeKeys() {
        _mazeKeys = new ArrayList<>();
        _mazeKeys.add(new keyPresses("UP", KeyEvent.VK_UP, () -> world.movePlayer(Direction.UP), 0));
        _mazeKeys.add(new keyPresses("DOWN", KeyEvent.VK_DOWN, () -> world.movePlayer(Direction.DOWN), 0));
        _mazeKeys.add(new keyPresses("LEFT", KeyEvent.VK_LEFT, () -> world.movePlayer(Direction.LEFT), 0));
        _mazeKeys.add(new keyPresses("RIGHT", KeyEvent.VK_RIGHT, () -> world.movePlayer(Direction.RIGHT), 0));
        _mazeKeys.add(new keyPresses("SLASH", KeyEvent.VK_ENTER, () -> world.doSlash(), 0));
        _mazeKeys.add(new keyPresses("ESC", KeyEvent.VK_ESCAPE, () -> controller.showMainMenu(), 0));
        _mazeKeys.add(new keyPresses("e", KeyEvent.VK_E, Labyrinth::loadNewLevel, 0));
        _mazeKeys.add(new keyPresses("r", KeyEvent.VK_R, () -> world.levelReset(), 0));
    }

    private static void registerMenuKeys() {
        _menuKeys = new ArrayList<>();
        _menuKeys.add(new keyPresses("ESC", KeyEvent.VK_ESCAPE, () -> controller.showGame(), 0));
    }

    private static void paramSetup() {
        TITEL = "The lazy Labyrinth";
        SCALE_X = 50;
        SCALE_Y = 50;
        SIZE_X = 17;
        SIZE_Y = 17;
        DIFFICULTY = 2;
        BORDERLESS = true;
        LANGUAGE = "english";
        _generator = new LevelGenerator(SIZE_X,SIZE_Y);
    }

    public static void loadNewLevel(){
        _generator.generateMaze();
        world.newLevel(new Level(SIZE_X, SIZE_Y, "GENERATED", _generator.getWalls(), _generator.getPlayerX(), _generator.getPlayerY(), _generator.getEndX(), _generator.getEndY(), _generator.get_enemies()));
    }

    /////////////////// GETTER AND SETTER METHODS ////////////////////////////////

    public static String getLanguage(){
        return LANGUAGE;
    }

    public static int getSCALE_X(){
        return SCALE_X;
    }

    public static int getSCALE_Y(){
        return SCALE_Y;
    }

    public static Boolean getBORDERLESS(){
        return BORDERLESS;
    }

    public static void setBORDERLESS(boolean b){
        BORDERLESS = b;
    }

    public static String getLANGUAGE(){
        return LANGUAGE;
    }

    public static void setLANGUAGE(String language){
        LANGUAGE = language;
    }

    public static int getDifficulty(){
        return DIFFICULTY;
    }

    public static void setDifficulty(int difficulty){
        DIFFICULTY = difficulty;
    }

    public static ArrayList<keyPresses> getKeys(){
        return _mazeKeys;
    }
}

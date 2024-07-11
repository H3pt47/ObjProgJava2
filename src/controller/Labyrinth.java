package controller;

import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

import GameWindow.*;
import Sound.audioPlayer;
import values.Direction;
import model.*;
import model.level.Level;
import model.level.LevelGenerator;
import values.keyPresses;
import view.ConsoleView;
import view.GraphicView;

/**
 * This is our main program. It is responsible for creating all of the objects
 * that are part of the MVC pattern and connecting them with each other.
 */
public class Labyrinth {

    /** The Titel of the Labyrinth.*/
    private static String TITEL;
    /** The X-Scale of one cell in the world.*/
    private static int SCALE_X;
    /** The Y-Scale of one cell in the world.*/
    private static int SCALE_Y;
    /** Sets the borders of the world.*/
    private static Boolean BORDERLESS;
    /** Language that can be selected.*/
    private static String LANGUAGE;
    /** Difficulty that can be selected.*/
    private static int DIFFICULTY;
    /** X-coordinate size of the world.*/
    private static int SIZE_X;
    /** Y-coordinate sice of the world.*/
    private static int SIZE_Y;
    public static int DELAY_MS = 150;
    public static int SLASH_DELAY = 5;
    public static int SLASH_SIZE = 5;
    /** Main menu of the world */
    private static MainMenu mainMenu;
    /** The WORLD. */
    private static World world;
    /** The Scale of the cell*/
    private static Dimension fieldDimensions;
    /** Graphcal View of the world.*/
    private static GraphicView gview;
    /** Console view of the world.*/
    private static ConsoleView cview;
    /** Controller of the world to control the player.*/
    private static Controller controller;
    /** The Level generator that generates us a new level.*/
    private static LevelGenerator _generator;

    private static Level _preloadedLevel;
    /** Arraylist that stores the keys that can be used in the maze.*/
    private static ArrayList<keyPresses> _mazeKeys;
    /** Arraylist that store the keys that can be used in the menu.*/
    private static ArrayList<keyPresses> _menuKeys;

    private static audioPlayer _audioPlayer;

    /**
     * Main method that connects everything and runs the game.
     * @param args The system arguments.
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                // set parameter
                paramSetup();

                // Generate a new Level
                _generator.generateMaze();

                //world = new World(new Level(SIZE_X, SIZE_Y, "TEST", new ArrayList<>(), 0, 0, 10, 10, new ArrayList<>()));
                world = new World(_generator.getLevel());

                preGenerateLevel();

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
                //world.registerView(cview);

                // Register Keys
                registerMazeKeys();
                registerMenuKeys();

                // Create Main Menu
                mainMenu = new MainMenu(TITEL);

                // Create controller and initialize JFrame.
                setupController(world, gview, mainMenu);

                //gview.setOffset(controller);
                gview.setController(controller);
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
        controller = new Controller(world, gview, mainMenu, _mazeKeys, _menuKeys, _audioPlayer);
        controller.frameSetup();

        //add controller to the action listener in menu
        mainMenu.setController(controller);

        //display MainMenu on startup
        controller.showMainMenu();

        //displays Main menu on startup
        controller.get_frame().setVisible(true);
    }

    /**
     *  Initializes the keys that can be used in the maze.
     */
    private static void registerMazeKeys() {
        _mazeKeys = new ArrayList<>();
        _mazeKeys.add(new keyPresses("UP", KeyEvent.VK_W, () -> world.keyPressed("UP"), () -> world.keyReleased("UP"), 0));
        _mazeKeys.add(new keyPresses("DOWN", KeyEvent.VK_S, () -> world.keyPressed("DOWN"), () -> world.keyReleased("DOWN"), 0));
        _mazeKeys.add(new keyPresses("LEFT", KeyEvent.VK_A, () -> world.keyPressed("LEFT"), () -> world.keyReleased("LEFT"), 0));
        _mazeKeys.add(new keyPresses("RIGHT", KeyEvent.VK_D, () -> world.keyPressed("RIGHT"), () -> world.keyReleased("RIGHT"), 0));
        _mazeKeys.add(new keyPresses("SLASH", KeyEvent.VK_SPACE, () -> world.keyPressed("SLASH"), () -> world.keyReleased("SLASH"), 0));
        _mazeKeys.add(new keyPresses("INTERACT", KeyEvent.VK_E, () -> world.keyPressed("INTERACT"), () -> world.keyReleased("INTERACT"), 0));
        _mazeKeys.add(new keyPresses("ESC", KeyEvent.VK_ESCAPE, () -> controller.showMainMenu(), 0));
        _mazeKeys.add(new keyPresses("RESET", KeyEvent.VK_R, () -> world.levelReset(), 0));
        _mazeKeys.add(new keyPresses("NEW_LEVEL", KeyEvent.VK_Q, Labyrinth::loadNewLevel, 0));
        _mazeKeys.add(new keyPresses("AUTO_SOLVE", KeyEvent.VK_S, () -> world.autoSolve(), InputEvent.CTRL_DOWN_MASK));
        _mazeKeys.add(new keyPresses("PRINT_INPUT_STACK", KeyEvent.VK_K, () -> System.out.println(world.get_keyPressManager().getStack().toString()), 0));
        _mazeKeys.add(new keyPresses("RESTART_SONG", KeyEvent.VK_L, () -> _audioPlayer.startFromBeginning(), 0));
    }

    /**
     *  Initializes the keys that can be used in the Main Menu.
     */
    private static void registerMenuKeys() {
        _menuKeys = new ArrayList<>();
        _menuKeys.add(new keyPresses("ESC", KeyEvent.VK_ESCAPE, () -> controller.showGame(), 0));
    }

    /**
     *  Sets up all parameters
     */
    private static void paramSetup() {
        TITEL = "The lazy Labyrinth";
        SCALE_X = 25;
        SCALE_Y = 25;
        SIZE_X = 35;
        SIZE_Y = 25;
        DIFFICULTY = 0;
        BORDERLESS = true;
        LANGUAGE = "english";
        _generator = new LevelGenerator(SIZE_X,SIZE_Y);
        _audioPlayer = new audioPlayer("Sound/Project_3_downScale.wav", 0.2f);
    }

    /**
     *  Generates a new Level and Loads it into the world.
     */
    public static void loadNewLevel(){
        world.newLevel(_preloadedLevel);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                preGenerateLevel();
            }
        });
    }

    private static void preGenerateLevel(){
        _generator.generateMaze();
        _preloadedLevel = _generator.getLevel();
    }

    /////////////////// GETTER AND SETTER METHODS ////////////////////////////////


    public static int getSCALE_X(){
        return SCALE_X;
    }

    public static int getSCALE_Y(){
        return SCALE_Y;
    }

    public static int getSizeX(){
        return SIZE_X;
    }

    public static int getSizeY(){
        return SIZE_Y;
    }

    public static String getTITEL(){
        return TITEL;
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

    public static audioPlayer getAudioPlayer(){
        return _audioPlayer;
    }

    public static Controller getController(){
        return controller;
    }
}

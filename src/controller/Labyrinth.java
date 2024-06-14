package controller;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import GameWindow.*;
import model.Direction;
import model.*;
import values.keyPresses;
import view.ConsoleView;
import view.GraphicView;

/**
 * This is our main program. It is responsible for creating all of the objects
 * that are part of the MVC pattern and connecting them with each other.
 */
public class Labyrinth {

    private static String TITEL;
    private static int SCALE;
    private static Boolean BORDERLESS;
    private static String LANGUAGE;
    private static int DIFFICULTY;

    private static Level _level1;

    private static MainMenu mainMenu;
    private static World world;
    private static Dimension fieldDimensions;
    private static GraphicView gview;
    private static ConsoleView cview;
    private static Controller controller;

    private static ArrayList<keyPresses> _keys;

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                // set parameter
                paramSetup();

                // Create a new game world.
                world = new World(_level1);

                // Size of a field in the graphical view.
                fieldDimensions = new Dimension(25, 25);
                // Create and register graphical view.
                gview = new GraphicView(
                        _level1.getLenX() * fieldDimensions.width,
                        _level1.getLenY() * fieldDimensions.height,
                        fieldDimensions);
                world.registerView(gview);
                gview.setVisible(true);

                // Create and register console view.
                cview = new ConsoleView();
                world.registerView(cview);

                // Register Keys

                registerKeys();

                // Create Main Menu
                mainMenu = new MainMenu(TITEL);

                // Create controller and initialize JFrame.
                setupController(world, gview, mainMenu);

                gview.setVisible(false);
                controller.getContentPane().add(gview);

                // pack() is needed before JFrame size can be calculated.
                //controller.pack();


                // Calculate size of window by size of insets (titlebar + border) and size of
                // graphical view.
                //Insets insets = controller.getInsets();
                /*
                int windowX = WIDTH * fieldDimensions.width + insets.left + insets.right;
                int windowY = HEIGHT * fieldDimensions.height + insets.bottom + insets.top;
                Dimension size = new Dimension(windowX, windowY);
                controller.setSize(size);
                controller.setMinimumSize(size);
                */
                controller.setVisible(true);


            }
        });
    }

    /////////////////// HELPER METHODS ////////////////////////////////

    private static void setupController(World world, GraphicView gview, MainMenu mainMenu){
        controller = new Controller(world, gview, mainMenu, _keys);
        controller.setExtendedState(JFrame.MAXIMIZED_BOTH);
        controller.setTitle(TITEL);
        controller.setUndecorated(BORDERLESS);
        controller.setResizable(false);
        controller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add controller to the action listener in menu
        mainMenu.setController(controller);

        //display MainMenu on startup
        controller.showMainMenu();

        //displays Mainmenu on startup / reload
        controller.setVisible(true);
    }

    public static void reset(){
        controller.dispose();
        setupController(world, gview, mainMenu);
        controller.setVisible(true);
    }

    private static void registerKeys() {
        _keys = new ArrayList<keyPresses>();
        _keys.add(new keyPresses("UP", KeyEvent.VK_UP, () -> world.movePlayer(Direction.UP), 0));
        _keys.add(new keyPresses("DOWN", KeyEvent.VK_DOWN, () -> world.movePlayer(Direction.DOWN), 0));
        _keys.add(new keyPresses("LEFT", KeyEvent.VK_LEFT, () -> world.movePlayer(Direction.LEFT), 0));
        _keys.add(new keyPresses("RIGHT", KeyEvent.VK_RIGHT, () -> world.movePlayer(Direction.RIGHT), 0));
        //_keys.add(new keyPresses("INTERACT", KeyEvent.VK_ENTER, () -> (), 0));
        _keys.add(new keyPresses("ESC", KeyEvent.VK_ESCAPE, () -> controller.showMainMenu(), 0));
    }

    private static void paramSetup() {
        TITEL = "The lazy Labyrinth";
        SCALE = 25;
        DIFFICULTY = 0;
        BORDERLESS = false;
        LANGUAGE = "english";
        ArrayList<Wall> walls = new ArrayList<Wall>();
        walls.add(new Wall(1,1));
        walls.add(new Wall(2,2));
        walls.add(new Wall(3,3));
        walls.add(new Wall(4,4));
        _level1 = new Level(25, 25, "LEVEL1", walls, 0, 0);
    }

    /////////////////// GETTER AND SETTER METHODS ////////////////////////////////

    public static String getLanguage(){
        return LANGUAGE;
    }

    public static int getSCALE(){
        return SCALE;
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
        return _keys;
    }

}

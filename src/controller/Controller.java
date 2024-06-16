package controller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import GameWindow.*;
import model.World;
import values.keyPresses;
import view.GraphicView;
import view.View;

/**
 * Our controller listens for key events on the main window.
 */
public class Controller extends JFrame implements KeyListener, ActionListener, MouseListener {

    /** The world that is updated upon every key press. */
    private World world;
    private List<View> views;
    private GraphicView graphicView;
    private MainMenu mainMenu;
    private Settings settings;

    private CardLayout cards;
    private Container mainContainer;

    private ArrayList<keyPresses> _keys;

    private InputMap _inputMapGame;
    private ActionMap _actionMapGame;
    /**
     * Creates a new instance.
     *
     * @param world the world to be updated whenever the player should move.
     * @param gview the GraphicView representation of the game so the Controller can take inputs from that.
     * @param mMenu The MainMenu, that is presented at the start of the game and anytime someone presses ESC
     * @param keys The list of Actions that
     */
    public Controller(World world, GraphicView gview, MainMenu mMenu, ArrayList<keyPresses> keys) {
        // Remember the world, gview, mainMenu, settings window,
        this.world = world;
        this.graphicView = gview;
        this.mainMenu = mMenu;
        this.settings = new Settings(this, this);

        this._keys = keys;

        setupInputActionMap();
        mainContainer = this.getContentPane();
        this.cards = new CardLayout();
        this.mainContainer.setLayout(cards);
        this.mainContainer.add("MENU", mainMenu.getMenuPanel());
        this.mainContainer.add("GAME", graphicView);

        //setup transparent / invisible Cursor for graphicView
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor transparentCursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "InvisibleCursor");

        graphicView.setCursor(transparentCursor);

        // Listen for key events
        addKeyListener(this);
        // Listen for mouse events.
        // Not used in the current implementation.
        addMouseListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    /////////////////// Key Events ////////////////////////////////

    @Override
    public void keyPressed(KeyEvent e) {
        //Now done with Input and Action Map due to complications with focused window.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    /////////////////// Action Events ////////////////////////////////

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(e.getActionCommand());
        switch (e.getActionCommand()) {
            case "Resume":
                //Resume
                this.showGame();
                break;
            case "Save Game":
                //save game
                //TODO SAVE GAME FUNCTIONALITY
                break;
            case "Load Game":
                //load game
                //TODO LOAD GAME FUNCTIONALITY
                break;
            case "Settings":
                //settings
                settings.enable();
                break;
            case "Confirm":
                //settings confirm
                handleSettings();
                break;
            case "Exit":
                //exit
                new areYouSureYouWantToExit(this);
                break;
        }

    }

    /////////////////// Mouse Events ////////////////////////////////

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    /////////////////// HELPER METHODS ////////////////////////////////

    public void showMainMenu(){
        cards.show(mainContainer, "MENU");
    }

    public void showGame(){
        cards.show(mainContainer, "GAME");
    }

    private void handleSettings(){
        if (settings.getDifficulty1().isSelected()){
            Labyrinth.setDifficulty(0);
        } else if (settings.getDifficulty2().isSelected()){
            Labyrinth.setDifficulty(1);
        } else {
            Labyrinth.setDifficulty(2);
        }
        if (settings.getLanguage1().isSelected()){
            Labyrinth.setLANGUAGE("english");
        } else if (settings.getLanguage2().isSelected()){
            Labyrinth.setLANGUAGE("german");
        } else {
            Labyrinth.setLANGUAGE("french");
        }
        Labyrinth.setBORDERLESS(settings.getScreenMode1().isSelected());
        settings.disable();
        Labyrinth.reset();
    }

    /**
     * This method takes the ArrayList of keyPresses given in the constructor and applies these
     * Values to the input and action map of the graphic view.
     */
    private void setupInputActionMap() {
        _inputMapGame = graphicView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        _actionMapGame = graphicView.getActionMap();
        _keys.forEach((key) -> {
            _inputMapGame.put(KeyStroke.getKeyStroke(key.getValue(), key.getModifier()), key.getKey());
            _actionMapGame.put(key.getKey(), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    key.getCommand().run();
                }
            });
        });
    }
}

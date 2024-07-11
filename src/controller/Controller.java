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
import Sound.audioPlayer;
import model.World;
import values.Direction;
import values.keyPressManager;
import values.keyPresses;
import view.GraphicView;
import view.View;

/**
 * Our controller listens for key events on the main window.
 */
public class Controller implements KeyListener, ActionListener, MouseListener {

    private JFrame _frame;

    /** The world that is updated upon every key press. */
    private World world;
    /** List of every view. */
    private List<View> views;
    /** The graphical view of the world.*/
    private GraphicView graphicView;
    /** The main menu where the user has various selections*/
    private MainMenu mainMenu;
    /** Setting menu where the user can change the settings.*/
    private Settings settings;

    private changeKeyBindingsWindow _changeKeys;
    /** */
    private CardLayout cards;
    private Container mainContainer;

    private ArrayList<keyPresses> _mazeKeys;
    private ArrayList<keyPresses> _menuKeys;

    private InputMap _inputMapGame;
    private ActionMap _actionMapGame;
    private InputMap _inputMapMenu;
    private ActionMap _actionMapMenu;

    private Timer _clock;

    private audioPlayer _audioPlayer;
    /**
     * Creates a new instance.
     *
     * @param world the world to be updated whenever the player should move.
     * @param gview the GraphicView representation of the game so the Controller can take inputs from that.
     * @param mMenu The MainMenu, that is presented at the start of the game and anytime someone presses ESC
     * @param mazeKeys The list of Actions that
     */
    public Controller(World world, GraphicView gview, MainMenu mMenu, ArrayList<keyPresses> mazeKeys, ArrayList<keyPresses> menuKeys, audioPlayer audioPlayer) {
        this._frame = new JFrame();
        // Remember the world, gview, mainMenu, settings window,
        this.world = world;
        this.graphicView = gview;
        this.mainMenu = mMenu;
        //this.settings = new Settings(this, this);

        this._audioPlayer = audioPlayer;

        this._mazeKeys = mazeKeys;
        this._menuKeys = menuKeys;

        setupInputActionMap();

        setupLayout();

        //setup transparent / invisible Cursor for graphicView
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Cursor transparentCursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "InvisibleCursor");

        graphicView.setCursor(transparentCursor);

        //Timer management
        _clock = new Timer(Labyrinth.DELAY_MS, e -> doTick());

        // Listen for key events
        _frame.addKeyListener(this);
        // Listen for mouse events.
        // Not used in the current implementation.
        _frame.addMouseListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
    }

    /////////////////// Key Events ////////////////////////////////

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /////////////////// Action Events ////////////////////////////////

    /**
     * The actions that can be selected in the main menu.
     * @param e the event to be processed
     */
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
                settings = new Settings(this._frame, this);
                settings.enable();
                break;
            case "Confirm Settings":
                //settings confirm
                handleSettings();
                break;
            case "Exit":
                //exit
                new areYouSureYouWantToExit(this);
                break;
            case "Change KeyBinds":
                //Change KeyBindsWindow on top of Settings
                _changeKeys = new changeKeyBindingsWindow(settings.getDialog(), this, _mazeKeys);
                _changeKeys.loadKeyBindings();
                _changeKeys.enable();
                break;
            case "Confirm KeyBindings":
                //Confirmation Button in the changeKeyBindingsWindow
                _mazeKeys = _changeKeys.getKeyPresses();
                this.setupInputActionMap();
                _changeKeys.disable();
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

    /**
     * Shows the main menu.
     */
    public void showMainMenu(){
        cards.show(mainContainer, "MENU");
        _audioPlayer.stop();
        world.set_isClockRunning(false);
        _clock.stop();
    }

    /**
     * Shows the game.
     */
    public void showGame(){
        cards.show(mainContainer, "GAME");
        _audioPlayer.start();
        world.set_isClockRunning(true);
        _clock.start();
    }

    /**
     * Method to handle the different settings that can be selected.
     */
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
        //BorderLess
        if (Labyrinth.getBORDERLESS() != settings.getScreenMode1().isSelected()){
            Labyrinth.setBORDERLESS(settings.getScreenMode1().isSelected());
            _frame.dispose();
            frameSetup();
        }
        //Audio
        Labyrinth.getAudioPlayer().setVolume((float) settings.getVolumeControl().getValue() / 100);
        //Close the Settings Dialog
        settings.dispose();
    }

    ///////////////////////////// INPUT //////////////////////////////////////

    /**
     * This method takes the ArrayList of keyPresses given in the constructor and applies these
     * Values to the input and action map of the graphic view.
     */
    private void setupInputActionMap() {
        _inputMapGame = graphicView.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        _actionMapGame = graphicView.getActionMap();
        setupInputActionMapHELPER(_inputMapGame, _actionMapGame, _mazeKeys);

        _inputMapMenu = mainMenu.getMenuPanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        _actionMapMenu = mainMenu.getMenuPanel().getActionMap();
        setupInputActionMapHELPER(_inputMapMenu, _actionMapMenu, _menuKeys);
    }

    private void setupInputActionMapHELPER(InputMap i, ActionMap a, ArrayList<keyPresses> k){
        k.forEach((key) -> {
            if(key.seperatePresses()){
                i.put(KeyStroke.getKeyStroke(key.getValue(), key.getModifier(), false), key.getKey() + "_p");
                a.put(key.getKey() + "_p", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        key.getCommand1().run();
                    }
                });
                i.put(KeyStroke.getKeyStroke(key.getValue(), key.getModifier(), true), key.getKey() + "_r");
                a.put(key.getKey() + "_r", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        key.getCommand2().run();
                    }
                });
            } else{
                i.put(KeyStroke.getKeyStroke(key.getValue(), key.getModifier(), false), key.getKey());
                a.put(key.getKey(), new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        key.getCommand1().run();
                    }
                });
            }
        });
    }

    ////////////////////////// CLOCK ///////////////////////////////

    public void doTick() {
        world.doTick();
    }

    public void pauseClock(){
        _clock.stop();
    }

    public void unpauseClock(){
        _clock.start();
    }

    //////////////////////// DISPOSAL /////////////////////
    public void dispose(){
        _frame.dispose();
    }

    ////////////////////////// STOPPING PROGRAM ///////////////////////

    public void stopProgram(){
        _audioPlayer.closeAudio();
        this.dispose();
        System.exit(-1);
    }

    //////////////////////// GETTER ///////////////////////////

    public JFrame get_frame(){
        return _frame;
    }

    //////////////////////// FRAME SETUP /////////////////////

    public void frameSetup(){
        _frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        _frame.setTitle(Labyrinth.getTITEL());
        _frame.setUndecorated(Labyrinth.getBORDERLESS());
        _frame.setResizable(false);
        _frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupLayout();
        _frame.setVisible(true);
    }

    private void setupLayout(){
        mainContainer = this._frame.getContentPane();
        this.cards = new CardLayout();
        this.mainContainer.setLayout(cards);
        this.mainContainer.add("MENU", mainMenu.getMenuPanel());
        this.mainContainer.add("GAME", graphicView);
    }
}

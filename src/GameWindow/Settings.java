package GameWindow;

import controller.Labyrinth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The settings menu thats currently not working 100% :)
 */

public class Settings {

    private JDialog _dialog;

    private JPanel panel;
    private JLabel titelText1;

    //Main Buttons
    private JButton confirm;
    private JButton cancel;

    //Difficulty Group
    private JLabel difficulty;
    private JRadioButton difficulty_1;
    private JRadioButton difficulty_2;
    private JRadioButton difficulty_3;

    //Language
    private JLabel language;
    private JRadioButton english;
    private JRadioButton german;
    private JRadioButton french;

    //Screen Mode
    private JLabel screenMode;
    private JRadioButton fullscreen;
    private JRadioButton windowed;


    public Settings(JFrame rel_frame, ActionListener actionListener) {
        _dialog = new JDialog();
        _dialog.setTitle("Settings");
        _dialog.setUndecorated(true);
        _dialog.setSize(500, 500);
        _dialog.setLocationRelativeTo(rel_frame);
        _dialog.setLayout(new BorderLayout());
        _dialog.add(panel, BorderLayout.CENTER);

        ButtonGroup difficulty_group = new ButtonGroup();
        difficulty_group.add(difficulty_1);
        difficulty_group.add(difficulty_2);
        difficulty_group.add(difficulty_3);

        if (Labyrinth.getDifficulty() == 0){
            difficulty_1.setSelected(true);
        }
        else if (Labyrinth.getDifficulty() == 1){
            difficulty_2.setSelected(true);
        }
        else{
            difficulty_3.setSelected(true);
        }

        ButtonGroup language_group = new ButtonGroup();
        language_group.add(english);
        language_group.add(german);
        language_group.add(french);

        //Handle Preselection on basis of settings
        if (Labyrinth.getLANGUAGE().equals("english")){
            english.setSelected(true);
        }
        else if (Labyrinth.getLANGUAGE().equals("german")){
            german.setSelected(true);
        }
        else{
            french.setSelected(true);
        }

        ButtonGroup screen_group = new ButtonGroup();
        screen_group.add(fullscreen);
        screen_group.add(windowed);

        if (Labyrinth.getBORDERLESS()){
            fullscreen.setSelected(true);
        }
        else{
            windowed.setSelected(true);
        }

        confirm.addActionListener(actionListener);
        cancel.addActionListener(e -> this.disable());
    }

    public void enable(){
        _dialog.setVisible(true);
    }

    public void disable(){
        _dialog.setVisible(false);
    }

    public JRadioButton getDifficulty1() {
        return difficulty_1;
    }

    public JRadioButton getDifficulty2() {
        return difficulty_2;
    }

    public JRadioButton getDifficulty3() {
        return difficulty_3;
    }

    public JRadioButton getLanguage1() {
        return english;
    }

    public JRadioButton getLanguage2() {
        return german;
    }

    public JRadioButton getLanguage3(){
        return french;
    }

    public JRadioButton getScreenMode1() {
        return fullscreen;
    }

    public JRadioButton getScreenMode2() {
        return windowed;
    }
}

package GameWindow;

import controller.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {
    private JPanel menuPanel;
    private JButton exitButton;
    private JButton settingsButton;
    private JButton resumeButton;
    private JButton loadSaveButton;
    private JButton saveGameButton;
    private JLabel titelText;




    public MainMenu(String titel) {
        titelText.setText(titel);
    }

    public void setController(Controller controller) {
        exitButton.addActionListener(controller);
        resumeButton.addActionListener(controller);
        saveGameButton.addActionListener(controller);
        loadSaveButton.addActionListener(controller);
        settingsButton.addActionListener(controller);

    }

    public JPanel getMenuPanel() {
        return menuPanel;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    public JButton getSettingsButton() {
        return settingsButton;
    }

    public JButton getResumeButton() {
        return resumeButton;
    }

    public JButton getLoadSaveButton() {
        return loadSaveButton;
    }

    public JButton getSaveGameButton() {
        return saveGameButton;
    }
}

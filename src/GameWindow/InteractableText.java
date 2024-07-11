package GameWindow;

import controller.Labyrinth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InteractableText {
    private JPanel _panel;
    private JLabel _label;
    private JButton _ok;

    private JDialog _dialog;


    /**
     * Opens a JDialog on top of relFrame.
     * @param relFrame The relative Frame, on which the JDialog gets opened.
     * @param text The text of the Label.
     * @param buttonText The text of the Button.
     * @param action The Action the button performs. After completing the runnable, it disposes the Dialog.
     */
    public InteractableText(JFrame relFrame, String text, String buttonText, Runnable action, int width, int height) {
        Labyrinth.getController().pauseClock();

        _dialog = new JDialog(relFrame);
        _dialog.setLayout(new BorderLayout());
        _dialog.setSize(width, height);
        _dialog.add(_panel, BorderLayout.CENTER);
        _dialog.setUndecorated(true);
        _dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        _dialog.setLocationRelativeTo(relFrame);
        _dialog.setBackground(Color.BLACK);

        _label.setText(text);

        _ok.setText(buttonText);
        _ok.addActionListener(e -> {
            action.run();
            _dialog.dispose();
            Labyrinth.getController().unpauseClock();
        });

        _dialog.setVisible(true);
    }
}

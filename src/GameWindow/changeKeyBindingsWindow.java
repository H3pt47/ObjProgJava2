package GameWindow;

import values.keyPresses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class changeKeyBindingsWindow {

    private JDialog _dialog;

    private ArrayList<keyPresses> _mazeKeys;

    private JPanel _panel;
    private JLabel titelText1;
    private JButton confirm;
    private JButton cancel;
    private JPanel _keyPressPanel;

    public changeKeyBindingsWindow(JDialog relFrame, ActionListener actionListener, ArrayList<keyPresses> mazeKeys) {
        _dialog = new JDialog(relFrame);
        _dialog.setSize(800, 400);
        _dialog.setUndecorated(true);
        _dialog.setResizable(false);
        _dialog.setLocationRelativeTo(relFrame);


        _dialog.setLayout(new BorderLayout());
        _dialog.add(_panel, BorderLayout.CENTER);

        _mazeKeys = new ArrayList<>(mazeKeys);

        confirm.addActionListener(actionListener);
        cancel.addActionListener(e -> this.disable());
    }

    public void loadKeyBindings(){
        _keyPressPanel.setLayout(new BoxLayout(_keyPressPanel, BoxLayout.Y_AXIS));

        Font font = _keyPressPanel.getFont();
        Color backGround = Color.black;
        Color foreGround = _keyPressPanel.getForeground();
        for (keyPresses key : _mazeKeys){

            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            rowPanel.setBackground(backGround);

            JLabel label = new JLabel(key.getKey());
            label.setFont(font);

            JTextField keyField = new JTextField(key.toString());
            keyField.setFont(font);
            keyField.setEditable(false);
            keyField.setBackground(backGround);
            keyField.setForeground(foreGround);
            keyField.setSelectionColor(Color.WHITE);
            keyField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    int modifiers = e.getModifiersEx();
                    key.setValue(keyCode);
                    key.setModifier(modifiers);
                    keyField.setText(key.toString());
                    keyField.revalidate();
                    _keyPressPanel.revalidate();
                }
            });

            rowPanel.add(label);
            rowPanel.add(keyField);

            _keyPressPanel.add(rowPanel);
            rowPanel.setVisible(true);
        }
    }

    public void enable(){
        _dialog.setVisible(true);
    }

    public void disable(){
        _dialog.setVisible(false);
        _dialog.dispose();
    }

    public ArrayList<keyPresses> getKeyPresses(){
        return _mazeKeys;
    }
}

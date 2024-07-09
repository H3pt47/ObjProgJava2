package GameWindow;

import controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 *  Creates a Pop-Up that ask the user: AreYouSureYouWantToExit ?
 */

public class areYouSureYouWantToExit {
    private JButton No;
    private JButton Yes;
    private JLabel titelText1;
    private JPanel panel;
    private JLabel titelText2;

    public areYouSureYouWantToExit(Controller controller) {
        JDialog frame = new JDialog(controller.get_frame());
        frame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        frame.setTitle("AreYouSureYouWantToExit?");
        frame.setUndecorated(true);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(controller.get_frame());
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);

        titelText1.setText("Are you sure you want to exit?");
        titelText2.setText("All unsaved progress will be lost.");

        No.addActionListener(e -> frame.dispose());
        Yes.addActionListener(e -> controller.stopProgram());

        frame.setVisible(true);
    }
}

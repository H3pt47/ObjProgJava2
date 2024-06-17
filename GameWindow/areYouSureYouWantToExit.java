package GameWindow;

import javax.swing.*;
import java.awt.*;

public class areYouSureYouWantToExit {
    private JButton No;
    private JButton Yes;
    private JLabel titelText1;
    private JPanel panel;
    private JLabel titelText2;

    public areYouSureYouWantToExit(JFrame rel_frame) {
        JDialog frame = new JDialog(rel_frame);
        frame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        frame.setTitle("AreYouSureYouWantToExit?");
        frame.setUndecorated(true);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(rel_frame);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);

        titelText1.setText("Are you sure you want to exit?");
        titelText2.setText("All unsaved progress will be lost.");

        No.addActionListener(e -> frame.dispose());
        Yes.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }
}

package model.Interactable;

import java.awt.*;
import java.util.List;
import model.World;

public interface Interactable {

    void interact(World world);

    void draw(Graphics g, int x, int y, int width, int height);

    String getText();
    void setText(String text);

    List<String> getInteractions();
    void setInteractions(List<String> interactions);

}

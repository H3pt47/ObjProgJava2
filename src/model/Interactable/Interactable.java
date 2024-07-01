package model.Interactable;

import java.awt.*;
import java.util.List;

public interface Interactable {
    void interact();

    void draw(Graphics g, int x, int y, int width, int height);

    String getText();
    void setText(String text);

    List<String> getInteractions();
    void setInteractions(List<String> interactions);

}

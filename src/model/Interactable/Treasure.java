package model.Interactable;

import java.awt.*;
import java.util.List;
import model.World;

public class Treasure implements Interactable {
    private String _text;
    private List<String> _interactions;

    public Treasure(String text, List<String> interactions) {
        _text = text;
        _interactions = interactions;
    }

    public Treasure(String text) {
        _text = text;
    }

    @Override
    public void interact(World world) {
        world.setCanSeePath(true);
    }

    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.YELLOW);
        g.drawRect(x + width / 4, y + height / 4, width / 2, height / 2);
    }

    @Override
    public String getText() {
        return _text;
    }

    @Override
    public void setText(String text) {
        _text = text;
    }

    @Override
    public List<String> getInteractions() {
        return _interactions;
    }

    @Override
    public void setInteractions(List<String> interactions) {
        _interactions = interactions;
    }
}

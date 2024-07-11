package model.Interactable;

import java.awt.*;
import java.util.List;

import GameWindow.InteractableText;
import controller.Labyrinth;
import model.World;

public class Treasure implements Interactable {
    private String _text;
    private List<String> _interactions;
    private boolean _hasBeenInteracted = false;

    public Treasure(String text, List<String> interactions) {
        _text = text;
        _interactions = interactions;
    }

    public Treasure(String text) {
        _text = text;
    }

    @Override
    public void interact(World world) {
        if (!_hasBeenInteracted) {
            _hasBeenInteracted = true;
            Runnable r = new Runnable() {
                public void run() {
                    world.setCanSeePath(true);
                }
            };
            new InteractableText(Labyrinth.getController().get_frame(), "You can now see the path.", "OK", r);
        }
    }

    @Override
    public void resetInteractions() {
        _hasBeenInteracted = false;
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

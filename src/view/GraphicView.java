package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

import model.Wall;
import model.World;

/**
 * A graphical view of the world.
 */
public class GraphicView extends JPanel implements View {

    /** The view's width. */
    private final int WIDTH;
    /** The view's height. */
    private final int HEIGHT;

    private Dimension fieldDimension;

    private World _world;

    public GraphicView(int width, int height, Dimension fieldDimension, World world) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.fieldDimension = fieldDimension;
        this.bg = new Rectangle(WIDTH, HEIGHT);
        this._world = world;
    }

    /** The background rectangle. */
    private final Rectangle bg;
    /** The rectangle we're moving. */
    private final Rectangle player = new Rectangle(1, 1);

    /** The Wall */
    private final Rectangle _wall = new Rectangle(1, 1);

    /**
     * Creates a new instance.
     */
    @Override
    public void paint(Graphics g) {
        // Paint background
        g.setColor(Color.RED);
        g.fillRect(bg.x, bg.y, bg.width, bg.height);
        // Paint player
        g.setColor(Color.BLACK);
        g.fillRect(player.x, player.y, player.width, player.height);

        //PaintWalls
        g.setColor(Color.GRAY);
        for (Wall wall: _world.getWalls()){
            g.fillRect(wall.x(), wall.y(), _wall.width, _wall.height);
        }
    }

    @Override
    public void update(World world) {

        // Update players size and location
        player.setSize(fieldDimension);
        player.setLocation(
                (int) (world.getPlayerX() * fieldDimension.width),
                (int) (world.getPlayerY() * fieldDimension.height)
        );
        repaint();
    }


}

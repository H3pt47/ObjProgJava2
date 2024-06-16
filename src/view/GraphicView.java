package view;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import controller.Controller;
import model.Enemies.Enemies;
import model.Wall;
import model.World;

/**
 * A graphical view of the world.
 */
public class GraphicView extends JPanel implements View {

    /** The width of the level in pixels. */
    private int WIDTH;
    /** The height of the level in pixels. */
    private int HEIGHT;

    /** Offset in Pixels, so the game is centered */
    private int _offSetX;
    private int _offSetY;

    /** Actual screen size in pixels */
    private int screenSizeX;
    private int screenSizeY;

    /**
     * The dimension, that regulates the scaling in the x and y coordinates:
     * Basically how big the fields are stretched
     */
    private Dimension fieldDimension;

    private Controller _controller;

    private World _world;

    private BufferedImage backGround;

    /**
     * The constructor of GraphicView
     * @param width The number of squares the game has horizontally
     * @param height The number of squares the game has vertically
     * @param fieldDimension A container for the scale of the Squares.
     * @param world The Class, that holds the information for the currently played level, and it's state
     */
    public GraphicView(int width, int height, Dimension fieldDimension, World world) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.fieldDimension = fieldDimension;
        this._world = world;
        _wall.setSize(fieldDimension);
        this.setVisible(true);
    }

    public void setController(Controller controller){
        _controller = controller;
        calcScreenSize();
        repaint();
    }

    private void calcScreenSize(){
        this.screenSizeX = _controller.getGraphicsConfiguration().getBounds().width;
        this.screenSizeY = _controller.getGraphicsConfiguration().getBounds().height;
        calcOffSet();
    }

    private void calcOffSet() {
        this._offSetX = (screenSizeX - this.WIDTH) / 2;
        this._offSetY = (screenSizeY - this.HEIGHT) / 2;
    }

    /** The rectangle we're moving. */
    private final Rectangle player = new Rectangle(1, 1);

    /** The Wall */
    private final Rectangle _wall = new Rectangle(1, 1);

    /**
     * Creates a new instance.
     */
    @Override
    public void paint(Graphics g) {
        // Paint buffered background: Does it only at the first update or when some parameter changes,
        // then it loads it out of memory, to save some resources
        if (backGround == null || backGround.getWidth() != this.screenSizeX || backGround.getHeight() != this.screenSizeY) {
            drawBackGround();
        }
        //actually drawing the background
        g.drawImage(backGround, 0, 0, screenSizeX, screenSizeY, null);

        drawEnemies(g);

        // draw player
        drawThePlayer(g);
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

    @Override
    public void newLevel(World world) {
        this.WIDTH = world.getWidth() * fieldDimension.width;
        this.HEIGHT = world.getHeight() * fieldDimension.height;
        calcOffSet();
        drawBackGround();
        update(world);
    }

    private void paintTheFrame(Graphics2D g) {
        for (int i = 0; i < _world.getWidth() + 2; i++) {
            //BORDER TOP
            drawWall(g, ((i - 1) * fieldDimension.width + _offSetX), (_offSetY - fieldDimension.height), fieldDimension.width, fieldDimension.height);
            //BORDER BOTTOM
            drawWall(g, ((i - 1) * fieldDimension.width + _offSetX), (_offSetY + (fieldDimension.height * (_world.getHeight()))), fieldDimension.width, fieldDimension.height);
        }
        for (int i = 0; i < _world.getHeight(); i++) {
            //BORDER LEFT
            drawWall(g, _offSetX - fieldDimension.width, ((i * fieldDimension.height) + _offSetY), fieldDimension.width, fieldDimension.height);
            //BORDER RIGHT
            drawWall(g, (_offSetX + (fieldDimension.width * _world.getWidth())), ((i * fieldDimension.height) + _offSetY), fieldDimension.width, fieldDimension.height);
        }
    }

    private void drawBackGround() {
        backGround = new BufferedImage(screenSizeX, screenSizeY, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = backGround.createGraphics();

        //Paint Black BackGround
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, screenSizeX, screenSizeY);

        //PaintWalls
        for (Wall wall: _world.getWalls()){
            drawWall(g2d, wall.x() * fieldDimension.width + _offSetX,
                    wall.y() * fieldDimension.height + _offSetY,
                    _wall.width,
                    _wall.height);
        }

        //paint the Frame
        paintTheFrame(g2d);

        //paint the End field
        drawEndField(g2d, _world.getEndX() * fieldDimension.width + _offSetX, _world.getEndY() * fieldDimension.height + _offSetY, fieldDimension.width, fieldDimension.height);

        //dispose to save resources
        g2d.dispose();
    }

    private void drawThePlayer(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(player.x + _offSetX, player.y + _offSetY, player.width, player.height);
        g.setColor(Color.RED);
        g.drawLine(player.x + _offSetX + (player.width/2),
                player.y  + _offSetY + (player.height/2),
                player.x + _offSetX + ((player.width + _world.getPlayerDirection().deltaX * fieldDimension.width)/2),
                player.y  + _offSetY + ((player.height + _world.getPlayerDirection().deltaY * fieldDimension.height)/2));
    }

    private void drawWall(Graphics2D g2d, int posX, int posY, int width, int height){
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(posX, posY, width, height);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(posX, posY, width, height);
    }

    private void drawEndField(Graphics2D g2d, int posX, int posY, int width, int height){
        g2d.setColor(Color.GREEN);
        g2d.drawRect(posX, posY, width, height);
    }

    private void drawEnemies(Graphics g) {
        for (Enemies e: _world.getEnemies()){
            drawEnemy(g, e.getX() * fieldDimension.width + _offSetX, e.getY() * fieldDimension.height + _offSetY, fieldDimension.width, fieldDimension.height, e);
        }
    }

    private void drawEnemy(Graphics g, int posX, int posY, int width, int height, Enemies e){
        g.setColor(Color.RED);
        g.drawRect(posX, posY, width, height);
    }
}

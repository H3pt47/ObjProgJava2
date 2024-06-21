package view;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import controller.Controller;
import model.Direction;
import model.Enemies.Enemies;
import model.Wall;
import model.World;
import values.path;
import values.pathCoordinate;

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
        drawPlayerTrackings(g);
        drawEnemies(g);

        if(_world.getSlashX() != -1 && _world.getSlashY() != -1){
            drawSlash(g, _world.getSlashX() * fieldDimension.width + _offSetX + fieldDimension.width / 2,
                    _world.getSlashY() * fieldDimension.height + _offSetY + fieldDimension.height / 2,
                    ((6 - _world.getSlashCoolDown()) * fieldDimension.width) / 2,
                    ((6 - _world.getSlashCoolDown()) * fieldDimension.height) / 2);
        }

        // draw player
        drawThePlayer(g, player.x + _offSetX, player.y + _offSetY, player.width, player.height);
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

        //Controls on the side [COMING SOON]
        //g2d.drawString()

        //dispose to save resources
        g2d.dispose();
    }

    private void drawThePlayer(Graphics g, int posX, int posY, int width, int height) {
        g.setColor(Color.WHITE);
        g.fillRect(posX, posY, width, height);
        g.setColor(Color.RED);
        g.drawLine(player.x + _offSetX + (player.width/2),
                player.y  + _offSetY + (player.height/2),
                player.x + _offSetX + ((player.width + _world.getPlayerDirection().deltaX * fieldDimension.width)/2),
                player.y  + _offSetY + ((player.height + _world.getPlayerDirection().deltaY * fieldDimension.height)/2));
    }

    private void drawWall(Graphics2D g2d, int posX, int posY, int width, int height) {
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(posX, posY, width, height);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(posX, posY, width, height);

        g2d.drawLine(posX, posY + height / 3, posX + width, posY + height / 3);
        g2d.drawLine(posX, (int) (posY + height / 1.5), posX + width, (int) (posY + height / 1.5));
        g2d.drawLine(posX + width / 4, posY, posX + width / 4, posY + height / 3);
        g2d.drawLine((int) (posX + 0.75 * width), posY, (int) (posX + 0.75 * width), posY + height / 3);
        g2d.drawLine((int) (posX + width / 2), posY + height / 3, (int) (posX + width / 2), (int) (posY + 0.66 * height));
        g2d.drawLine(posX + width / 4, (int) (posY + 0.66 * height), posX + width / 4, posY + height);
        g2d.drawLine((int) (posX + 0.75 * width), (int) (posY + 0.66 * height), (int) (posX + 0.75 * width), posY + height);
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

    private void drawEnemy(Graphics g, int posX, int posY, int width, int height, Enemies e) {

        if (e.isActivated() && !e.isDead()) {
            g.setColor(Color.RED);
            g.drawRect(posX, posY, width, height);
            g.drawRect(posX + width / 5, posY + height / 5, width / 5, height / 5);
            g.drawRect(posX + width / 5, posY + height / 5, width / 5, height / 5);
            g.drawRect((int) (posX + 0.6 * width), posY + height / 5, width / 5, height / 5);
            g.drawLine(posX + width / 5, (int) (posY + 0.6 * height), (int) (posX + 0.8 * width), (int) (posY + 0.6 * height));

        } else if (e.isDead()) {
            g.setColor(Color.RED);
            g.drawRect(posX, posY, width, height);

            g.drawLine((int) (posX + 0.2 * width), posY + height / 5, (int) (posX + 0.4 * width), (int) (posY + 0.4 * height));
            g.drawLine((int) (posX + 0.4 * width), posY + height / 5, posX + width / 5, (int) (posY + 0.4 * height));

            g.drawLine((int) (posX + 0.6 * width), posY + height / 5, (int) (posX + 0.8 * width), (int) (posY + 0.4 * height));
            g.drawLine((int) (posX + 0.8 * width), posY + height / 5, (int) (posX + 0.6 * width), (int) (posY + 0.4 * height));

            g.drawOval(posX + width/3 , (int) (posY + height * 0.5), width/3 , height /4);

        } else if (!e.isActivated()) {

            g.setColor(Color.WHITE);
            g.drawRect(posX, posY, width, height);
            g.drawLine(posX + width / 5, (int) (posY + 0.6 * height), (int) (posX + 0.8 * width), (int) (posY + 0.6 * height));
            g.drawLine(posX + width / 5, posY + height / 5, (int) (posX + 0.4 * width), posY + height / 5);
            g.drawLine((int) (posX + 0.6 * width), posY + height / 5, (int) (posX + 0.8 * width), posY + height / 5);
            if (_world.boundsChecker(posX, posY + height)) {
                g.setColor(Color.blue);
                String z = "z";
                Font stringFont = new Font("SansSerif", Font.PLAIN, 10);
                g.setFont(stringFont);
                g.drawString(z, (int) (posX + 0.6 * width), (int) (posY - 0.25 * height));
                Font stringFont1 = new Font("SansSerif", Font.PLAIN, 12);
                g.setFont(stringFont1);
                g.drawString(z, (int) (posX + 0.75 * width), (int) (posY - 0.60 * height));
                Font stringFont2 = new Font("SansSerif", Font.PLAIN, 14);
                g.setFont(stringFont2);
                g.drawString(z, (int) (posX + 0.9 * width), (int) (posY - 0.95 * height));

            }

        }

    }

    private void drawPlayerTrackings(Graphics g){
        if (_world.getPaths() != null){
            for(pathCoordinate p:_world.getPaths().keySet()){
            drawPlayerTracking(g, p.x() * fieldDimension.width + _offSetX + (fieldDimension.width / 4), p.y() * fieldDimension.height + _offSetY + (fieldDimension.height / 4),
                    fieldDimension.width / 2, fieldDimension.height / 2, _world.getPaths().get(p));

            }
        }
    }

    private void drawPlayerTracking(Graphics g, int posX, int posY, int width, int height, path p){
        Direction dir = p.getPath().get(p.getLength()-1);
        g.setColor(Color.BLUE);
        g.drawRect(posX, posY, width, height);
        switch (dir){
            case LEFT:
                g.drawPolygon(new Polygon(new int[]{posX, posX, posX - width / 2}, new int[]{posY, posY + height, posY + height / 2}, 3));
                break;
            case RIGHT:
                g.drawPolygon(new Polygon(new int[]{posX + width, posX + width, posX + width + width / 2}, new int[]{posY, posY + height, posY + height / 2}, 3));
                break;
            case UP:
                g.drawPolygon(new Polygon(new int[]{posX, posX + width, posX + width / 2}, new int[]{posY, posY, posY - height / 2}, 3));
                break;
            case DOWN:
                g.drawPolygon(new Polygon(new int[]{posX, posX + width, posX + width / 2}, new int[]{posY + height, posY + height, posY + height + height / 2}, 3));
                break;
        }

    }
    private void drawSlash(Graphics g, int x, int y, int sizeX, int sizeY){
        g.setColor(Color.CYAN);
        g.drawRect(x - sizeX / 2, y - sizeY / 2, sizeX, sizeY);
    }
}

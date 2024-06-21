package model.Enemies;

import model.Direction;
import model.World;
import values.path;
import values.pathCoordinate;

import java.util.List;

/**
 * Hunts down the player only if it sees them. The sight is limited to either straight lines or paths equal or shorter to a value specified in world. for performance reasons.
 */
public class Dijkstremy implements Enemies{

    /** X-coordinate of the enemy.*/
    private int _X;
    /** Y-coordinate of the enemy.*/
    private int _Y;
    /** Starting X-coordinate of the enemy.*/
    private final int _startX;
    /** Starting Y-coordinate of the enemy.*/
    private final int _startY;
    /** Satus of the enemy*/
    private boolean _activated;
    /** Enemy dead or alive?*/
    private boolean _dead;
    /** The direction the enemy is facing.*/
    private Direction _direction;
    /**
     *
     * @param x X-coordinate of the created enemy
     * @param y Y-coordinate of the created enemy
     * @param activated status of the enemy
     * @param dead enemy dead or alive?
     */
    public Dijkstremy(int x, int y, Boolean activated, Boolean dead) {
        _X = x;
        _Y = y;
        _startX = x;
        _startY = y;
        _activated = activated;
        _dead = dead;
        _direction = Direction.NONE;
    }

    /**
     *
     * @param world
     */
    @Override
    public void update(World world){
        if(_activated && !_dead){
            if(world.getPaths().containsKey(new pathCoordinate(_X, _Y))){
                path path = world.getPaths().get(new pathCoordinate(_X, _Y));
                _X += path.getPath().get(path.getLength()-1).deltaX;
                _Y += path.getPath().get(path.getLength()-1).deltaY;
            }
            //If the Enemy kills the Player (Moved onto the field of the player)
            if(_X == world.getPlayerX() && _Y == world.getPlayerY()){
                world.levelReset();
            }
        }
    }

    @Override
    public void reset(){
        _direction = Direction.NONE;
        _X = _startX;
        _Y = _startY;
    }
    @Override
    public int getX(){
        return _X;
    }
    @Override
    public int getY(){
        return _Y;
    }
    @Override
    public Direction getDirection(){
        return _direction;
    }
    @Override
    public Boolean isDead(){
        return _dead;
    }

    public Boolean isActivated(){
        return _activated;
    }

    public void setActivated(boolean activated){
        _activated = activated;
    }
}

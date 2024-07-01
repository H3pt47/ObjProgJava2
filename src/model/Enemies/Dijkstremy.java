package model.Enemies;

import controller.Labyrinth;
import values.Direction;
import model.World;
import values.path;
import values.coordinate;

import java.util.Random;

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

    private int _coolDown;

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
        _coolDown = 0;
    }

    /**
     * The update method for moving the enemy.
     * @param world The world in which the Enemy lives. It gets the information about its surroundings from that and acts accordingly.
     */
    @Override
    public void update(World world){
        if(_activated && !_dead){
            if(world.getPaths().containsKey(new coordinate(_X, _Y))){
                path path = world.getPaths().get(new coordinate(_X, _Y));
                // checks for valid field
                if (!world.enemyChecker(_X + path.getDirection().deltaX, _Y + path.getDirection().deltaY)){
                    _X += path.getDirection().deltaX;
                    _Y += path.getDirection().deltaY;
                }
                overHeating();
            }
            //If the Enemy kills the Player (Moved onto the field of the player)
            if(_X == world.getPlayerX() && _Y == world.getPlayerY()){
                world.levelReset();
            }
        }
        else if(!_activated && !_dead){
            _coolDown--;
            if(_coolDown == 0){
                _activated = true;
            }
        }
    }

    /**
     * Resets the state and coordinates of the enemy.
     */
    @Override
    public void reset(){
        _direction = Direction.NONE;
        _X = _startX;
        _Y = _startY;
        _activated = true;
        _dead = false;
        _coolDown = 0;
    }

    /**
     * Get X coordinate
     * @return The X coordinate of the enemy.
     */
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

    public void kill(){
        _dead = true;
    }

    /**
     * A mechanic in which the enemy randomly overheats and deactivates for a set amount of moves. It
     */
    private void overHeating(){
        Random r = new Random();
        if (r.nextInt(0, Labyrinth.getDifficulty() * 2 + 5) == 0){
            _direction = Direction.NONE;
            _activated = false;
            _coolDown = 4 - Labyrinth.getDifficulty();
        }
    }
}

package model.Enemies;

import model.Direction;
import model.World;

import java.util.concurrent.ThreadLocalRandom;

/**
 *  An enemy that runs randomly across the maze.
 *
 */
public class Randemy implements Enemies{

    private static final int multChance = 50;

    /** The X-coordinate of the enemy.*/
    private int _X;
    /** The Y-coordinate of the enemy.*/
    private int _Y;
    /** The starting X-coordinate of the enemy.*/
    private final int _startX;
    /** The starting Y-coordinate of the enemy.*/
    private final int _startY;
    /** The status of the enemy.*/
    private boolean _activated;
    /** Enemy dead or alive?*/
    private boolean _dead;
    /** The Direction the enemy is facing.*/
    private Direction _direction;

    /**
     *  Creates a enemy that randomly moves across the maze.
     * @param x x-coordinate of the enemy.
     * @param y y-coordinate of the enemy.
     * @param activated status of the enemy.
     * @param dead enemy dead or alive?
     */
    public Randemy (int x, int y, Boolean activated, Boolean dead){
        _X = x;
        _Y = y;
        _startX = x;
        _startY = y;
        _direction = Direction.NONE;
        _activated = activated;
        _dead = dead;
    }

    /**
     * updates the given world and moves the enemy in the chosen direction.
     * @param world world that is updated
     */
    @Override
    public void update(World world){
        if(_activated && !_dead){
            _direction = Direction.NONE;
            int i = ThreadLocalRandom.current().nextInt(0, 5);
            int j = ThreadLocalRandom.current().nextInt(0, multChance);
            switch(i){
                case 0:
                    _direction = Direction.NONE;
                    break;
                case 1:
                    _direction = Direction.UP;
                    break;
                case 2:
                    _direction = Direction.DOWN;
                    break;
                case 3:
                    _direction = Direction.LEFT;
                    break;
                case 4:
                    _direction = Direction.RIGHT;
                    break;
            }
            if(world.posCheckEnemies(_X + _direction.deltaX, _Y + _direction.deltaY)){
                _X += _direction.deltaX;
                _Y += _direction.deltaY;
                if (j == 0){
                    world.newEnemy(new Randemy(_X - _direction.deltaX, _Y - _direction.deltaY, true, false));
                }
            }
            //If the Randemy kills the Player (Moved onto the field of the player)
            if(_X == world.getPlayerX() && _Y == world.getPlayerY()){
                world.levelReset();
            }
        }
    }

    /**
     * reset the enemy to its starting position.
     */
    @Override
    public void reset(){
        //_activated = false;
        _direction = Direction.NONE;
        _X = _startX;
        _Y = _startY;
    }


    //////////////////////Getter and Setter////////////////////////////////////
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

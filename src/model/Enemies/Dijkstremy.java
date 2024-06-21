package model.Enemies;

import controller.Labyrinth;
import model.Direction;
import model.World;
import values.path;
import values.pathCoordinate;

import java.util.List;
import java.util.Random;

/**
 * Hunts down the player only if it sees them. The sight is limited to either straight lines or paths equal or shorter to a value specified in world. for performance reasons.
 */
public class Dijkstremy implements Enemies{

    private int _X;
    private int _Y;

    private final int _startX;
    private final int _startY;

    private boolean _activated;
    private boolean _dead;

    private Direction _direction;

    private int _coolDown;

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

    @Override
    public void update(World world){
        if(_activated && !_dead){
            if(world.getPaths().containsKey(new pathCoordinate(_X, _Y))){
                path path = world.getPaths().get(new pathCoordinate(_X, _Y));
                // checks for valid field
                if (!world.enemyChecker(_X + path.getPath().get(path.getLength()-1).deltaX, _Y + path.getPath().get(path.getLength()-1).deltaY)){
                    _X += path.getPath().get(path.getLength()-1).deltaX;
                    _Y += path.getPath().get(path.getLength()-1).deltaY;
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

    @Override
    public void reset(){
        _direction = Direction.NONE;
        _X = _startX;
        _Y = _startY;
        _activated = true;
        _dead = false;
        _coolDown = 0;
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

    public void kill(){
        _dead = true;
    }

    private void overHeating(){
        Random r = new Random();
        if (r.nextInt(0, Labyrinth.getDifficulty() * 2 + 5) == 0){
            _direction = Direction.NONE;
            _activated = false;
            _coolDown = 4 - Labyrinth.getDifficulty();
        }
    }
}

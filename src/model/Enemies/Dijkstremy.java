package model.Enemies;

import model.Direction;
import model.World;
import values.path;
import values.pathCoordinate;

import java.util.List;

public class Dijkstremy implements Enemies{

    private int _X;
    private int _Y;

    private final int _startX;
    private final int _startY;

    private boolean _activated;
    private boolean _dead;

    private Direction _direction;


    public Dijkstremy(int x, int y, Boolean activated, Boolean dead) {
        _X = x;
        _Y = y;
        _startX = x;
        _startY = y;
        _activated = activated;
        _dead = dead;
        _direction = Direction.NONE;
    }

    @Override
    public void update(World world){
        if(_activated && !_dead){
            if(world.getPaths().containsKey(new pathCoordinate(_X, _Y))){
                path path = world.getPaths().get(new pathCoordinate(_X, _Y));
                System.out.println(path.getPath().toString());
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

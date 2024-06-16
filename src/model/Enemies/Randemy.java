package model.Enemies;

import model.Direction;
import model.World;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Randemy implements Enemies{

    private int _X;
    private int _Y;

    private final int _startX;
    private final int _startY;

    private boolean _activated;

    private Direction _direction;

    public Randemy (int x, int y, Boolean activated){
        _X = x;
        _Y = y;
        _startX = x;
        _startY = y;
        _direction = Direction.NONE;
        _activated = activated;
    }

    @Override
    public void update(World world){
        if(_activated){
            _direction = Direction.NONE;
            int i = ThreadLocalRandom.current().nextInt(0, 5);
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
            }
            if(_X == world.getPlayerX() && _Y == world.getPlayerY()){
                world.levelReset();
            }
        }
    }

    @Override
    public void reset(){
        //_activated = false;
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

    public Boolean isActivated(){
        return _activated;
    }
    public void setActivated(boolean activated){
        _activated = activated;
    }
}

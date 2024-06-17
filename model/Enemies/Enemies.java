package model.Enemies;

import model.Direction;
import model.World;

public interface Enemies {

    void update(World world);

    void reset();

    int getY();

    int getX();

    Direction getDirection();

}

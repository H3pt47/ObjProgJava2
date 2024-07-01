package model.Enemies;

import values.Direction;
import model.World;

public interface Enemies {

    void update(World world);

    void reset();

    int getY();

    int getX();

    Direction getDirection();

    Boolean isDead();

    Boolean isActivated();

    void setActivated(boolean activated);

    void kill();

}

package values;

/**
 * Represents a direction in the game.
 */
public enum Direction {
    /** No movement. */
    NONE(0, 0),
    /** Up movement. */
    UP(0, -1),
    /** Down movement. */
    DOWN(0, 1),
    /** Left movement. */
    LEFT(-1, 0),
    /** Right movement. */
    RIGHT(1, 0);

    /** The amount to move in the X direction. */
    public final int deltaX;
    /** The amount to move in the Y direction. */
    public final int deltaY;

    /**
     * Creates a new direction with the given movement values.
     *
     * @param deltaX The amount to move in the X direction.
     * @param deltaY The amount to move in the Y direction.
     */
    Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    public static Direction getOppositeDirection(Direction direction) {
        return switch (direction) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            default -> NONE;
        };
    }
}
package model;


/**
 * represents a wall in a two-dimenstional space.
 * The record wall automatically provides:
 * - A constructor with parameters `x` and `y`
 * - Getter methods `x()` and `y()` to access the coordinates
 * - `equals()`, , and `toString()` methods
 */
public record Wall(int x, int y) {
}

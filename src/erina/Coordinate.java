package erina;

/**
 * Represents a Coordinate with 2 int values.
 *
 * @version 1.0
 * @author Eric
 */
public class Coordinate extends Pair<Integer, Integer> {

	/**
	 * Constructs a new Coordinate with the specified X and Y values.
	 * @param x	the X coordinate
	 * @param y	the Y coordinate
	 */
	public Coordinate(int x, int y) {
		super(x, y);
	}

	/**
	 * Gets the X value of this Coordinate. This method behaves exactly the same as
	 * {@link Pair#getKey()}.
	 * @return the X value of this Coordinate
	 */
	public int getX() { return getKey(); }

	/**
	 * Gets the Y value of this Coordinate. This method behaves exactly the same as
	 * {@link Pair#getValue()}.
	 * @return the Y value of this Coordinate
	 */
	public int getY() { return getValue(); }
}

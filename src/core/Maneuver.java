package core;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A Maneuver describes a set of turnings and movements.
 *
 * @version alpha
 * @author Eric
 */
public class Maneuver {

	/*
	Note:
	Competitors produce Maneuvers, Erina (specifically ManeuverHandler consumes Maneuvers.
	The former and the latter run on separate threads. Operations on Maneuver objects
	should be synchronized.
	 */

	private final Queue<Action> actions = new ConcurrentLinkedQueue<>();

	private final int initialX, initialY;
	private volatile int x, y, direction;

	/**
	 * Creates a new Maneuver that starts at the specified location direction the specified
	 * direction.
	 * @param x	the x coordinate of the start location
	 * @param y	the y coordinate of the start location
	 * @param direction	the starting direction in degrees
	 */
	public Maneuver(int x, int y, int direction) {
		initialX = x;
		this.x = x;

		initialY = y;
		this.y = y;

		this.direction = direction;
	}

	/**
	 * Creates a new Maneuver that starts at the location of the specified Entity and
	 * faces in the same direction as the Entity.
	 * @param entity	the Entity to acquire location and direction from
	 */
	public Maneuver(Entity<?, ?> entity) {
		this(
				entity.getX(),
				entity.getY(),
				entity.getDirection()
		);
	}


	/**
	 * Issues a move of the specified distance in the current direction.
	 * @param distance	the amount to move
	 * @return	this instance
	 */
	public synchronized Maneuver move(int distance) {
		actions.add(new Advance(distance));

		// negative direction because we are counting clockwise as positive
		final double rads = Math.toRadians(-direction);

		setX(getX() + (int) Math.cos(rads));
		setY(getY() + (int) Math.sin(rads));
		// possible source of bug, losing a lot of precision here

		return this;
	}

	/**
	 * Issues a turn of the specified angle. Positive angles are considered clockwise.
	 * @param degrees	the amount to turn in degrees
	 * @return	this instance
	 */
	public synchronized Maneuver turn(int degrees) {
		actions.add(new Turn(degrees));

		setDirection(getDirection() + degrees);

		return this;
	}

	/**
	 * Turns toward the specified location.
	 * @param x	the x coordinate of the cell to turn to
	 * @param y	the y coordinate of the cell to turn to
	 * @return	this instance
	 */
	public synchronized Maneuver turnTo(int x, int y) {
		final double rads =
				Math.atan(
						(double) (y - getY()) / (x - getX())
				);
		final int degrees = - (int) Math.toDegrees(rads);

		return turn(degrees - getDirection());
	}


	/**
	 * Returns the x location as if previously issued actions were executed.
	 * @return	the x location
	 */
	public int getX() { return x; }

	/**
	 * Returns the x coordinate of the starting location of this Maneuver.
	 * @return	the initial x location
	 */
	public int getInitialX() { return initialX; }

	/**
	 * Returns the y location as if previously issued actions were executed.
	 * @return	the y location
	 */
	public int getY() { return y; }

	/**
	 * Returns the y coordinate of the starting location of this Maneuver.
	 * @return	the initial y location
	 */
	public int getInitialY() { return initialY; }

	/**
	 * Returns the direction as if previously issued actions were executed.
	 * @return	the direction
	 */
	public int getDirection() { return direction; }


	Queue<Action> getActions() { return actions; }


	private void setX(int x) { this.x = x; }
	private void setY(int y) { this.y = y; }
	private void setDirection(int direction) { this.direction = direction; }


	/**
	 * Returns a string representation of this Maneuver.
	 * @return	the string representation
	 */
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();

		final Iterator<Action> it = actions.iterator();

		if (it.hasNext()) {
			builder.append(it.next());

			while (it.hasNext()) {
				final Action action = it.next();
				builder
						.append("\nthen ")
						.append(action);
			}
		}

		return builder.toString();
	}

	/**
	 * Returns a shorter string representation of this Maneuver.
	 * @return	the string representation
	 */
	public String toShortString() {
		return "(" + getInitialX() + ", " + getInitialY() + ") -> ("
				+ getX() + ", " + getY() + ")";
	}
}

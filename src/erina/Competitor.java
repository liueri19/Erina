package erina;

import greenfoot.Actor;
import greenfoot.GreenfootImage;

/**
 * The super class of all competitors.
 *
 * @version alpha
 * @author Eric
 */
public abstract class Competitor {
	private final Actor actor;
	private final String name;

	/**
	 * Constructs a new Competitor containing the specified Actor and name.
	 * @param actor	the Actor for this Competitor to delegate to
	 * @param name	the name of this Competitor
	 */
	public Competitor(Actor actor, String name) {
		this.name = name;
		this.actor = actor;
	}


	////////////////////////////////
	// delegations
	////////////////////////////////

	public final GreenfootImage getImage() { return actor.getImage(); }

	/**
	 * Returns the x-coordinate of the Competitor's current location. The value returned
	 * is the horizontal index of the Competitor's cell in the world.<br>
	 * This method is equivalent to the {@link Actor#getX()} method.
	 * @return	the x-coordinate of the Competitor's current location
	 */
	public final int getX() { return actor.getX(); }

	/**
	 * Returns the y-coordinate of the Competitor's current location. The value returned
	 * is the vertical index of the Competitor's cell in the world.<br>
	 * This method is equivalent to the {@link Actor#getY()} method.
	 * @return	the y-coordinate of the Competitor's current location
	 */
	public final int getY() { return actor.getY(); }

	/**
	 * Returns the direction this Competitor is currently facing in degrees.<br>
	 * This method is equivalent ot the {@link Actor#getRotation()} method.
	 * @return	the direction in degrees
	 */
	public final int getDirection() { return actor.getRotation(); }


	final Actor getActor() { return actor; }

	@Override
	public String toString() { return name; }
}

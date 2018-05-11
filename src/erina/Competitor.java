package erina;

import greenfoot.Actor;
import greenfoot.GreenfootImage;

/**
 * The super class of all competitors.
 */
public abstract class Competitor {
	private final Actor actor;
	protected final String name;

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


	final Actor getActor() { return actor; }

	@Override
	public String toString() { return name; }
}

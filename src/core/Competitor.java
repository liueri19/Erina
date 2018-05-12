package core;

import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.World;

/**
 * The super class of all Competitors.
 *
 * <p>Subclasses of Competitors should not need to know about Actors. Competitors have a
 * higher level of abstraction.
 *
 * @version alpha
 * @author Eric
 */
public abstract class Competitor extends Entity<CompetitorActor> implements Maneuverable {
	private final String name;

	/**
	 * Constructs a new Competitor with the specified name.
	 * @param name	the name of this Competitor
	 */
	public Competitor(String name) {
		this.name = name;
	}


	private void validate() {
		if (getActor() == null)
			throw new IllegalStateException("Competitor not properly initialized");
	}


	////////////////////////////////
	// delegations
	////////////////////////////////

	/** @see	Actor#getImage() */
	public final GreenfootImage getImage() { validate(); return getActor().getImage(); }

	/** @see	Actor#getX() */
	public final int getX() { validate(); return getActor().getX(); }

	/** @see	Actor#getY() */
	public final int getY() { validate(); return getActor().getY(); }

	/** @see	Actor#getRotation() */
	public final int getDirection() { validate(); return getActor().getRotation(); }

	/** @see	Actor#addedToWorld(World) */
	protected void addedToWorld(World world) {}

	@Override
	public String toString() { return name; }
}
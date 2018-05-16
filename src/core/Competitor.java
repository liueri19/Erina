package core;

import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.List;

/**
 * The super class of all Competitors.
 *
 * <p>Subclasses of Competitors should not need to know about Actors. Competitors have a
 * higher level of abstraction.
 *
 * @version alpha
 * @author Eric
 */
public abstract class Competitor
		extends Entity<Competitor, CompetitorActor>
		implements Maneuverable {

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
	/*
	getOneIntersectingObject
	getOneObjectAtOffset
	getWorld
	getWorldOfType
	insersects
	isAtEdge
	isTouching
	 */

	/** @see	Actor#addedToWorld(World)  */
	protected void addedToWorld(World world) {}	// optionally overridden by subclasses

	/** @see	Actor#getImage()  */
	public final GreenfootImage getImage() {
		validate(); return getActor().getImage();
	}

	/** @see	Actor#getIntersectingObjects(Class)  */
	protected final <T> List<T> getIntersectingObjects(Class<T> cls) {
		validate();
		Erina.rejectActorType(cls);
		return Erina.getObjectsUsing(
				c -> getActor().getIntersectingObjectsSuper(c), cls
		);
	}

	/** @see	Actor#getNeighbours(int, boolean, Class)  */
	protected final <T> List<T> getNeighbours(int distance, boolean diagonal, Class<T> cls) {
		validate();
		Erina.rejectActorType(cls);
		return Erina.getObjectsUsing(
				c -> getActor().getNeighboursSuper(distance, diagonal, c), cls
		);
	}

	/** @see	Actor#getObjectsAtOffset(int, int, Class)  */
	protected final <T> List<T> getObjectsAtOffset(int dx, int dy, Class<T> cls) {
		validate();
		Erina.rejectActorType(cls);
		return Erina.getObjectsUsing(
				c -> getActor().getObjectsAtOffsetSuper(dx, dy, c), cls
		);
	}

	/** @see	Actor#getObjectsInRange(int, Class)  */
	protected final <T> List<T> getObjectsInRange(int radius, Class<T> cls) {
		validate();
		Erina.rejectActorType(cls);
		return Erina.getObjectsUsing(
				c -> getActor().getObjectsInRangeSuper(radius, c), cls
		);
	}

	/* getOneIntersectingObject */
	/* getOneObjectAtOffset */

	/** @see	Actor#getRotation()  */
	public final int getRotation() { validate(); return getActor().getRotation(); }

	/** @see	Actor#getWorld()  */
	public final World getWorld() { validate(); return getActor().getWorld(); }

	/* getWorldOfType */

	/** @see	Actor#getX() */
	public final int getX() { validate(); return getActor().getX(); }

	/** @see	Actor#getY() */
	public final int getY() { validate(); return getActor().getY(); }

	/**
	 *
	 * @param other
	 * @return
	 * @see	Actor#intersects(Actor)
	 */
	protected final boolean intersects(Competitor other) {
		validate();
		return getActor().intersectsSuper(other.getActor());
	}

	/** @see	Actor#isAtEdge()  */
	public final boolean isAtEdge() { validate(); return getActor().isAtEdge(); }

	/** @see	Actor#isTouching(Class)  */
	protected final boolean isTouching(Class<?> cls) {
		validate();
		return getActor().isTouchingSuper(cls);
	}

	/** @see	Actor#setImage(String)  */
	public final void setImage(String fileName) {
		validate(); getActor().setImage(fileName);
	}

	/** @see	Actor#setImage(GreenfootImage)  */
	public final void setImage(GreenfootImage image) {
		validate(); getActor().setImage(image);
	}

	/** @see	Actor#getRotation() */
	public final int getDirection() { validate(); return getActor().getRotation(); }


	@Override
	public String toString() { return name; }
}

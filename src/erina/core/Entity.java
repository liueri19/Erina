package erina.core;

import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.List;

/**
 * Represents an entity in the Erina. This class is designed to replace Actor.
 *
 * <p>Entities can be converted to EntityActors using {@link Entity#getActor()}. Similarly
 * EntityActors can be converted to Entities using {@link EntityActor#getEntity()}.
 *
 * @version alpha
 * @author Eric
 */
public abstract class Entity<E extends Entity<E, A>, A extends EntityActor<E, A>> {

	private A actor;
	private boolean hasInit;
	private final Erina world;


	public Entity(Erina world) {
		this.world = world;
	}


	/**
	 * Initialize this Entity, binds this Entity with the specified EntityActor.
	 * @param actor	the Actor to bind to
	 * @throws IllegalStateException	if this Entity has already initialized
	 */
	void init(A actor) throws IllegalStateException {
		if (!hasInit) {
			hasInit = true;
			this.actor = actor;
		}
		else {
			throw new IllegalStateException("Cannot re-initialize Entity");
		}

		// init image
		if (image != null)
			getActor().setImage(image);
	}

	/** Gets the Actor bound to this Entity. */
	A getActor() { return actor; }


	private void validate() {
		if (getActor() == null)
			throw new IllegalStateException("Entity not properly initialized");
	}

	////////////////////////////////
	// delegations
	////////////////////////////////

	/** @see	Actor#addedToWorld(World)  */
	protected void addedToWorld(World world) {}	// optionally overridden by subclasses

	/**
	 * Unlike the greenfoot counterpart, this method does not accept null.
	 * @see	Actor#getIntersectingObjects(Class)
	 */
	protected final <T> List<T> getIntersectingObjects(Class<T> cls) {
		validate();
		Erina.rejectActorType(cls);
		return Erina.getObjectsUsing(
				c -> getActor().getIntersectingObjectsActor(c), cls
		);
	}

	/**
	 * Unlike the greenfoot counterpart, this method does not accept null.
	 * @see	Actor#getNeighbours(int, boolean, Class)
	 */
	protected final <T> List<T> getNeighbours(int distance, boolean diagonal, Class<T> cls) {
		validate();
		Erina.rejectActorType(cls);
		return Erina.getObjectsUsing(
				c -> getActor().getNeighboursActor(distance, diagonal, c), cls
		);
	}

	/**
	 * Unlike the greenfoot counterpart, this method does not accept null.
	 * @see	Actor#getObjectsAtOffset(int, int, Class)
	 */
	protected final <T> List<T> getObjectsAtOffset(int dx, int dy, Class<T> cls) {
		validate();
		Erina.rejectActorType(cls);
		return Erina.getObjectsUsing(
				c -> getActor().getObjectsAtOffsetActor(dx, dy, c), cls
		);
	}

	/**
	 * Unlike the greenfoot counterpart, this method does not accept null.
	 * @see	Actor#getObjectsInRange(int, Class)
	 */
	protected final <T> List<T> getObjectsInRange(int radius, Class<T> cls) {
		validate();
		Erina.rejectActorType(cls);
		return Erina.getObjectsUsing(
				c -> getActor().getObjectsInRangeActor(radius, c), cls
		);
	}

	/**
	 * Returns an object of the specified type that intersects this Entity, or null if
	 * no object intersects this Entity.
	 * Unlike the greenfoot counterpart, this method does not accept null.
	 * @see	Actor#getOneIntersectingObject(Class)
	 */
	protected final <T> T getOneIntersectingObject(Class<T> cls) {
		validate();
		final List<T> objects = getIntersectingObjects(cls);
		return objects.isEmpty() ? null : objects.get(0);
	}

	/**
	 * Returns an object of the specified type that is located at the specified cell.
	 * Unlike the greenfoot counterpart, this method does not accept null.
	 * @see	Actor#getOneObjectAtOffset(int, int, Class)
	 */
	protected final <T> T getOneObjectAtOffset(int dx, int dy, Class<T> cls) {
		validate();
		final List<T> objects = getObjectsAtOffset(dx, dy, cls);
		return objects.isEmpty() ? null : objects.get(0);
	}

	/** @see	Actor#getRotation() */
	public final int getDirection() { validate(); return getActor().getRotation(); }

	/** @see	Actor#getWorld()  */
	public final Erina getWorld() { validate(); return world; }

	/** @see	Actor#getX() */
	public final int getX() { validate(); return getActor().getX(); }

	/** @see	Actor#getY() */
	public final int getY() { validate(); return getActor().getY(); }

	/** @see	Actor#setLocation(int, int) */
	final void setLocation(int x, int y) { validate(); getActor().setLocation(x, y); }

	/**
	 * Checks whether this Competitor intersects with the specified Entity.
	 * @param other	the Entity to check for intersection with
	 * @return	true if the objects intersect, false otherwise
	 * @see	Actor#intersects(Actor)
	 */
	protected final boolean intersects(Entity<?, ?> other) {
		validate();
		return getActor().intersectsActor(other.getActor());
	}

	/** @see	Actor#isAtEdge()  */
	public final boolean isAtEdge() { validate(); return getActor().isAtEdge(); }

	/** @see	Actor#isTouching(Class)  */
	protected final boolean isTouching(Class<?> cls) {
		validate();
		return getOneIntersectingObject(cls) != null;
	}


	// special handling for images to allow setImage before Actor init
	private GreenfootImage image;

	/** @see	Actor#getImage()  */
	public final GreenfootImage getImage() { return image; }

	/** @see	Actor#setImage(String)  */
	public final void setImage(String fileName) {
		setImage(new GreenfootImage(fileName));
	}

	/** @see	Actor#setImage(GreenfootImage)  */
	public final void setImage(GreenfootImage image) {
		this.image = image;
		if (getActor() != null)
			getActor().setImage(image);
	}
}

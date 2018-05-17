package core;

import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.GreenfootSound;
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

	/** The amount of damage each collision deals. */
	public static final int HIT_DAMAGE = 5;

	private GreenfootSound killSound, deathSound, horrorSound, sadisticSound;

	/** The identifier of this Competitor. */
	private final String name;

	/** The world this Competitor is in. */
	private final Erina world;

	private static final int INITIAL_ENERGY_LEVEL = 500;

	/** Amount of energy this competitor has. */
	private int energyLevel = INITIAL_ENERGY_LEVEL;


	/**
	 * Constructs a new Competitor with the specified name.
	 * @param world	the world this competitor is in
	 * @param name	the name of this Competitor
	 */
	public Competitor(Erina world, String name) {
		this.world = world;
		this.name = name;
	}


	/**
	 * Modifies the energy level of this Competitor by the specified amount. Negative
	 * values can be used to remove energy.
	 * @param amount	the amount of energy to add to this Competitor
	 * @return	current energy level after modification
	 */
	final int changeEnergy(int amount) {
		return energyLevel += amount;
	}

	/**
	 * Gets the amount of energy this Competitor currently has.
	 * @return	the current energy level
	 */
	public final int getEnergyLevel() { return energyLevel; }


	/**
	 * Sets the kill sound to the file specified by the path.
	 * The kill sound is played when another Competitor is killed by this Competitor.
	 * @param file	the path to the sound file
	 */
	protected final void setKillSound(String file) {
		killSound = new GreenfootSound(file);
	}

	final void playKillSound() {
		if (killSound != null && !killSound.isPlaying())
			killSound.play();
	}

	/**
	 * Sets the death sound to the file specified by the path.
	 * The death sound is played when this Competitor is killed.
	 * @param file	the path to the sound file
	 */
	protected final void setDeathSound(String file) {
		deathSound = new GreenfootSound(file);
	}

	final void playDeathSound() {
		if (deathSound != null && !deathSound.isPlaying())
			deathSound.play();
	}

	/**
	 * Sets the horror sound to the file specified by the path.
	 * The horror sound is played when this Competitor is being hit.
	 * @param file	the path to the sound file
	 */
	protected final void setHorrorSound(String file) {
		horrorSound = new GreenfootSound(file);
	}

	final void playHorrorSound() {
		if (horrorSound != null && !horrorSound.isPlaying())
			horrorSound.play();
	}

	/**
	 * Sets the sadistic sound to the file specified by the path.
	 * The sadistic sound is played when this Competitor sadistically consumes something.
	 * @param file	the path to the sound file
	 */
	protected final void setSadisticSound(String file) {
		sadisticSound = new GreenfootSound(file);
	}

	final void playSadisticSound() {
		if (sadisticSound != null && !sadisticSound.isPlaying())
			sadisticSound.play();
	}



	private void validate() {
		if (getActor() == null)
			throw new IllegalStateException("Competitor not properly initialized");
	}

	////////////////////////////////
	// delegations
	////////////////////////////////

	/** @see	Actor#addedToWorld(World)  */
	protected void addedToWorld(World world) {}	// optionally overridden by subclasses

	/** @see	Actor#getImage()  */
	public final GreenfootImage getImage() {
		validate(); return getActor().getImage();
	}

	/**
	 * Unlike the greenfoot counterpart, this method does not accept null.
	 * @see	Actor#getIntersectingObjects(Class)
	 */
	protected final <T> List<T> getIntersectingObjects(Class<T> cls) {
		validate();
		Erina.rejectActorType(cls);
		return Erina.getObjectsUsing(
				c -> getActor().getIntersectingObjectsSuper(c), cls
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
				c -> getActor().getNeighboursSuper(distance, diagonal, c), cls
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
				c -> getActor().getObjectsAtOffsetSuper(dx, dy, c), cls
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
				c -> getActor().getObjectsInRangeSuper(radius, c), cls
		);
	}

	/**
	 * Returns an object of the specified type that intersects this Competitor, or null if
	 * no object intersects this Competitor.
	 * Unlike the greenfoot counterpart, this method does not accept null.
	 * @see	Actor#getOneIntersectingObject(Class)
	 */
	protected final <T> T getOneIntersectingObject(Class<T> cls) {
		final List<T> objects = getIntersectingObjects(cls);
		return objects.isEmpty() ? null : objects.get(0);
	}

	/**
	 * Returns an object of the specified type that is located at the specified cell.
	 * Unlike the greenfoot counterpart, this method does not accept null.
	 * @see	Actor#getOneObjectAtOffset(int, int, Class)
	 */
	protected final <T> T getOneObjectAtOffset(int dx, int dy, Class<T> cls) {
		final List<T> objects = getObjectsAtOffset(dx, dy, cls);
		return objects.isEmpty() ? null : objects.get(0);
	}

	/** @see	Actor#getRotation()  */
	public final int getRotation() { validate(); return getActor().getRotation(); }

	/** @see	Actor#getWorld()  */
	public final Erina getWorld() { validate(); return world; }

	/** @see	Actor#getX() */
	public final int getX() { validate(); return getActor().getX(); }

	/** @see	Actor#getY() */
	public final int getY() { validate(); return getActor().getY(); }

	/**
	 * Checks whether this Competitor intersects with the specified Entity.
	 * @param other	the Entity to check for intersection with
	 * @return	true if the objects intersect, false otherwise
	 * @see	Actor#intersects(Actor)
	 */
	protected final boolean intersects(Entity<?, ?> other) {
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

	/** Returns the identifier of this Competitor. */
	public final String getName() { return name; }


	@Override
	public String toString() { return name; }
}

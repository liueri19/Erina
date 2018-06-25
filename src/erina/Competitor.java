package erina;

import greenfoot.GreenfootSound;

import java.util.*;

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

	private static final int INITIAL_ENERGY_LEVEL = 500;

	/** Amount of energy this competitor has. */
	private int energyLevel = INITIAL_ENERGY_LEVEL;

	private CompetitorStats stats;

	private final NameTag nameTag;


	/**
	 * Gets a CompetitorStats object holding the statistics about this Competitor.
	 * @see	CompetitorStats
	 */
	public final CompetitorStats getStats() {
		return stats;
	}

	/**
	 * Updates the statistics of this Competitor to the specified values.
	 * @see	CompetitorStats
	 */
	final void updateStats(CompetitorStats stats) {
		this.stats = stats;
	}


	/**
	 * Constructs a new Competitor with the specified name.
	 * @param world	the world this competitor is in
	 * @param name	the name of this Competitor
	 */
	public Competitor(Erina world, String name) {
		super(world);
		stats = new CompetitorStats();
		this.name = name;
		nameTag = new NameTag(name);
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
		killSound = (file == null) ? null : new GreenfootSound(file);
	}

	/** Plays the kill sound if it exists and is not already playing. */
	public final void playKillSound() { Erina.tryPlaySound(killSound); }

	/**
	 * Sets the death sound to the file specified by the path.
	 * The death sound is played when this Competitor is killed.
	 * @param file	the path to the sound file
	 */
	protected final void setDeathSound(String file) {
		deathSound = (file == null) ? null : new GreenfootSound(file);
	}

	/** Plays the death sound if it exists and is not already playing. */
	public final void playDeathSound() { Erina.tryPlaySound(deathSound); }

	/**
	 * Sets the horror sound to the file specified by the path.
	 * The horror sound is played when this Competitor is being hit.
	 * @param file	the path to the sound file
	 */
	protected final void setHorrorSound(String file) {
		horrorSound = (file == null) ? null : new GreenfootSound(file);
	}

	/** Plays the horror sound if it exists and is not already playing. */
	public final void playHorrorSound() { Erina.tryPlaySound(horrorSound); }

	/**
	 * Sets the sadistic sound to the file specified by the path.
	 * The sadistic sound is played when this Competitor sadistically consumes something.
	 * @param file	the path to the sound file
	 */
	protected final void setSadisticSound(String file) {
		sadisticSound = (file == null) ? null : new GreenfootSound(file);
	}

	/** Plays the sadistic sound if it exists and is not already playing. */
	public final void playSadisticSound() { Erina.tryPlaySound(sadisticSound); }


	/** Returns the identifier of this Competitor. */
	public final String getName() { return name; }

	/** Returns the NameTag associated with this Competitor. */
	final NameTag getNameTag() { return nameTag; }


	/**
	 * Consumes the Nugget. Removes the nugget from the Erina, updates the energy level of
	 * this Competitor, and updates the stats.
	 */
	final void consume(Nugget nugget) {
		/*
		Nugget is consumed when this Competitor comes in contact with the Nugget.
		 */
		getWorld().removeEntity(nugget);
		changeEnergy(nugget.getNuggetValue());
		getStats().incrementNuggets(nugget);
		playSadisticSound();

		Logger.log("Nugget Consumption: %s consumed %s%n",
				this, nugget);
	}

	/**
	 * Consumes the Sauce. Updates the energy level of this Competitor and updates the
	 * stats.
	 */
	final void consume(Sauce sauce, int multiplier) {
		/*
		Sauce is not removed because consumption occurs when the Sauce expires.
		 */
		changeEnergy(sauce.getSauceValue() * multiplier);
		getStats().incrementSauces(sauce, multiplier);
		playSadisticSound();

		Logger.log("Sauce Consumption: %s consumed %s with multiplier of %d%n",
				this, sauce, multiplier);
	}


	/**
	 * Apply the specified amount of damage on this Competitor. Plays the horror sound if
	 * defined.
	 */
	final void takeDamage(int damage) {
		if (damage < 0) throw new IllegalArgumentException("damage cannot be negative");

		changeEnergy(-damage);
		playHorrorSound();
	}


	/**
	 * Signals the death of this Competitor. Removes this Competitor from the Erina and
	 * plays the death sound if defined.
	 */
	final void die() {
		getWorld().removeEntity(this);
		playDeathSound();

		Logger.log("Death: %s died%n", this);
	}



	////////////////////
	// collision state stuff

	/** Competitors intersecting with this Competitor in the previous update. */
	private final Set<Competitor> previousContacts = new HashSet<>();
	/** Competitors currently intersecting with this Competitor. */
	private final Set<Competitor> currentContacts = new HashSet<>();

	/**
	 * Updates the contact states of this Competitor.
	 */
	final void updateContacts() {
		previousContacts.clear();
		previousContacts.addAll(currentContacts);

		currentContacts.clear();
		currentContacts.addAll(getIntersectingObjects(Competitor.class));
	}

	/**
	 * Returns a List of intersecting Competitors that was not intersecting before the
	 * previous call of {@link Competitor#updateContacts()}.
	 */
	final List<Competitor> getNewContacts() {
		final List<Competitor> newContacts = new ArrayList<>(currentContacts);
		newContacts.removeAll(previousContacts);
		return newContacts;
	}

	////////////////////


	@Override
	public String toString() { return name; }
}



/**
 * A concrete implementation of Actor used in Competitors.
 *
 * <p>Since Competitors are not Actors, they are not updated by the Greenfoot framework.
 * This class allows Competitors to be represented in the Erina as Actors.
 *
 * @version alpha
 * @author Eric
 */
class CompetitorActor extends EntityActor<Competitor, CompetitorActor> {

	CompetitorActor(Competitor entity) {
		super(entity);
	}

	@Override
	public void move(int distance) {
		super.move(distance);

		// make the NameTag follow
		final NameTag nameTag = getEntity().getNameTag();
		nameTag.setRotation(getRotation());
		nameTag.move(distance);
		nameTag.setRotation(0);
	}
}

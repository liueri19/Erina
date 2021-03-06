package erina.core;

import erina.Pair;
import greenfoot.GreenfootSound;

import java.util.*;

/**
 * The super class of all Competitors.
 *
 * <p>Subclasses of Competitors should not need to know about Actors. Competitors have a
 * higher level of abstraction.
 *
 * @version 1.0
 * @author Eric
 */
public abstract class Competitor
		extends Entity<Competitor, CompetitorActor>
		implements Maneuverable {

	/** The minimum amount of damage each collision deals. */
	public static final int MIN_DAMAGE = 1;
	/** The maximum amount of damage each collision deals. */
	public static final int MAX_DAMAGE = 6;

	private GreenfootSound killSound, deathSound, horrorSound, sadisticSound;

	/** The identifier of this Competitor. */
	private final String name;

	private static final int INITIAL_ENERGY_LEVEL = 500;

	/** Amount of energy this competitor has. */
	private int energyLevel = INITIAL_ENERGY_LEVEL;

	private final CompetitorStats stats;

	private final NameTag nameTag;


	/**
	 * Gets a CompetitorStats object holding the statistics about this Competitor.
	 * @see	CompetitorStats
	 */
	public final CompetitorStats getStats() {
		return stats;
	}


	/**
	 * Constructs a new Competitor with the specified name.
	 * @param world	the world this competitor is in
	 * @param name	the name of this Competitor
	 */
	public Competitor(Erina world, String name) {
		super(world);
		stats = new CompetitorStats(this);
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

		Logger.logLine("[%5d] Nugget Consumption: %15s consumed %15s",
				getWorld().getCurrentCycle(), this, nugget);
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

		Logger.logLine("[%5d] Sauce Consumption: %15s consumed %15s with multiplier of %3d",
				getWorld().getCurrentCycle(), this, sauce, multiplier);
	}


	/**
	 * Apply the specified amount of damage on this Competitor. Plays the horror sound if
	 * defined.
	 * @param attacker the Competitor that inflicted damage on this Competitor
	 * @see	Competitor#inflictDamageOn(Competitor, int)
	 */
	final void takeDamageFrom(Competitor attacker, int damage) {
		if (damage < 0) throw new IllegalArgumentException("damage cannot be negative");

		changeEnergy(-damage);
		attacker.inflictDamageOn(this, damage);
		getStats().incrementHitsAbsorbed();
		getStats().incrementDamageAbsorbedBy(damage);
		getStats().setLastAttacker(attacker);
		playHorrorSound();

		Logger.logLine("[%5d] Collision: %15s took %d damage from %15s",
				getWorld().getCurrentCycle(), this, damage, attacker);
	}


	/**
	 * Updates the CompetitorStats of this Competitor to reflect a hit on {@code target}.
	 * This method does not apply any damage; damage is applied in
	 * {@link Competitor#takeDamageFrom(Competitor, int)}.
	 * @see	Competitor#takeDamageFrom(Competitor, int)
	 */
	private void inflictDamageOn(Competitor target, int damage) {
		getStats().incrementHitsInflicted();
		getStats().incrementDamageInflictedBy(damage);
		getStats().setLastVictim(target);
	}


	/**
	 * Checks if this Competitor is still alive. A Competitor is alive as long as it has
	 * non-negative energy.
	 * @return true if this Competitor is dead, false otherwise
	 */
	public final boolean isDead() {
		return getEnergyLevel() < 0;
	}

	/**
	 * Signals the death of this Competitor. Removes this Competitor from the Erina and
	 * plays the death sound if defined. This method logs the death message.
	 * @param attacker the Competitor that killed this Competitor
	 * @see	Competitor#kill(Competitor)
	 */
	final void die(Competitor attacker) {
		getWorld().removeEntity(this);
		attacker.kill(this);
		playDeathSound();

		Logger.logLine("[%5d] Death: %15s is killed by %15s",
				getWorld().getCurrentCycle(), this, attacker);
	}

	/**
	 * Checks if this Competitor is still alive. If dead, this method calls
	 * {@link Competitor#die(Competitor)}.
	 * @param attacker the Competitor that killed this Competitor
	 * @return	true if this Competitor is dead, false otherwise
	 */
	final boolean checkDeath(Competitor attacker) {
		final boolean isDead = isDead();
		if (isDead)
			die(attacker);
		return isDead;
	}


	/**
	 * Signals a kill completed by this Competitor. This method does not remove
	 * {@code victim} from the Erina, it only updates the CompetitorStats of this
	 * Competitor and plays the kill sound if defined.
	 * @see	Competitor#die(Competitor)
	 */
	private void kill(Competitor victim) {
		// no we don't use the parameter in here, but it makes sense to take one
		// it's private anyway whatever...
		getStats().incrementKills();
		playKillSound();
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
		if (isDead()) return;

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


	/** Returns the identifier of this Competitor. */
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

		final Competitor competitor = getEntity();
		final NameTag nameTag = getEntity().getNameTag();
		final Pair<Integer, Integer> offset = Erina.getNameTagOffset(competitor);

		// make the NameTag follow
		nameTag.setLocation(
				competitor.getX() + offset.getKey(),
				competitor.getY() - offset.getValue()
		);
	}
}

package core;

import greenfoot.GreenfootSound;

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


	/**
	 * Constructs a new Competitor with the specified name.
	 * @param world	the world this competitor is in
	 * @param name	the name of this Competitor
	 */
	public Competitor(Erina world, String name) {
		super(world);
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


	@Override
	public String toString() { return name; }
}

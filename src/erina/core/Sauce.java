package erina.core;

/**
 * A Sauce is like a Nugget in that they are both sources Competitors may
 * acquire energy from. A Sauce expires after a certain time, and rewards
 * Competitors touching the Sauce upon expiry. The amount of energy
 * rewarded to each Competitor is the amount of energy stored in the Sauce
 * multiplied by the number of Competitors touching the Sauce at the time
 * of expiry. The goal of this behaviour is to encourage cooperation
 * among Competitors.
 *
 * @version 1.0
 * @author Eric
 */
public final class Sauce extends Entity<Sauce, SauceActor> {

	private static final int ITERATIONS_TO_TIMEOUT = 200;

	private final int sauceValue;
	private volatile int iterationsLeft = ITERATIONS_TO_TIMEOUT;
	private volatile boolean countingDown = false;

	Sauce(Erina world, int sauceValue) {
		super(world);
		this.sauceValue = sauceValue;

		// just in case you find different images for sauces and want to change
		if (sauceValue == 500)
			setImage("images/LaoGanMa2.png");
		else if (sauceValue == 1000)
			setImage("images/LaoGanMa2.png");
		else
			setImage("images/LaoGanMa2.png");
	}

	/**
	 * Returns the amount of energy stored in this Sauce.
	 */
	public int getSauceValue() { return sauceValue; }

	/**
	 * Returns the number of iterations left until this Sauce expires.
	 */
	public int getIterationsLeft() { return iterationsLeft; }

	/**
	 * Checks if this Sauce has timed out.
	 * @return true if this sauce has timed out, false otherwise
	 */
	public boolean hasTimedOut() { return iterationsLeft <= 0; }

	void startCountdown() {
		countingDown = true;
	}

	/**
	 * Updates the timeout of this Sauce. Decreases the amount of iterations left by 1 if
	 * {@link Sauce#startCountdown()} has been called on this Sauce and this Sauce has not
	 * expired.
	 */
	synchronized void updateTimeout() {
		if (countingDown && iterationsLeft > 0)
			iterationsLeft--;
	}

	void resetTimeout() { iterationsLeft = ITERATIONS_TO_TIMEOUT; }


	/**
	 * Returns a string describing the amount of energy this Sauce has.
	 */
	@Override
	public String toString() {
		return "Sauce_" + sauceValue;
	}
}


/** The EntityActor counterpart of the Entity Sauce. */
final class SauceActor extends EntityActor<Sauce, SauceActor> {
	SauceActor(Sauce entity) {
		super(entity);
	}
}

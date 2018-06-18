package erina;

/**
 * A Sauce is like a Nugget in that they are both sources Competitors may
 * acquire energy from. A Sauce expires after a certain time, and rewards
 * Competitors touching the Sauce upon expiry. The amount of energy
 * rewarded to each Competitor is the amount of energy stored in the Sauce
 * multiplied by the number of Competitors touching the Sauce at the time
 * of expiry. The goal of this behaviour is to encourage cooperation
 * among Competitors.
 *
 * @version alpha
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
	}

	/**
	 * Returns the amount of energy stored in this Sauce.
	 */
	public int getSauceValue() { return sauceValue; }

	/**
	 * Returns the number of iterations left until this Sauce expires.
	 */
	public int getIterationsLeft() { return iterationsLeft; }

	public boolean hasTimedOut() { return iterationsLeft <= 0; }

	void startCountdown() {
		countingDown = true;
	}

	/**
	 * Updates this Sauce by decrement the amount of iterations left by 1.
	 * @return	true if this Sauce has not expired, false otherwise
	 */
	synchronized boolean updateTimeout() {
		if (countingDown && iterationsLeft > 0)
			iterationsLeft--;
		return iterationsLeft > 0;
	}
}


final class SauceActor extends EntityActor<Sauce, SauceActor> {
	SauceActor(Sauce entity) {
		super(entity);
	}
}

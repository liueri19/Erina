package core;

/**
 * The Referee provides methods for handling World's and Competitors' states,
 * such as removing Competitors with energy less than 0.
 *
 * <p>Unlike the original Arena, the Referee will have no instance.
 *
 * @version alpha
 * @author Eric
 */
final class Referee {
	private static final int ENERGY_MORSEL = 1;
	private static final int MAX_CYCLES = 10000;

	private int currentCycle = 0;

	private Referee() {}


	/**
	 * Handles the specified Erina.
	 * @return true if Erina has no more Competitors, false otherwise
	 */
	static boolean handleErina(Erina erina) {
		// TODO implement this

		return false;
	}
}

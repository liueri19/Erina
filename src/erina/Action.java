package erina;

/**
 * An Action is a single step in a Maneuver. An Action can be applied to an Entity.
 *
 * @version alpha
 * @author Eric
 */
abstract class Action {
	abstract void applyTo(Competitor competitor);
}


/** Represents an advancement of a certain distance. */
final class Advance extends Action {
	private final int distance, cost;

	Advance(int distance) {
		// if malicious attempt to cause overflow
		if (distance == Integer.MIN_VALUE)
			distance = distance+1;

		this.distance = distance;

		cost = Math.abs(distance);
	}

	@Override
	void applyTo(Competitor competitor) {
		competitor.changeEnergy(-cost);
		competitor.getActor().move(distance);
	}

	@Override
	public String toString() { return "advance " + distance + " units"; }
}


/** Represents a clockwise turn of certain degrees. */
final class Turn extends Action {
	private final int degrees;

	Turn(int degrees) { this.degrees = degrees; }

	@Override
	void applyTo(Competitor competitor) {
		competitor.getActor().turn(degrees);
	}

	@Override
	public String toString() { return "turn clockwise " + degrees + " degrees"; }
}

package core;

/**
 * An Action is a single step in a Maneuver. An Action can be applied to an Entity.
 *
 * @version alpha
 * @author Eric
 */
abstract class Action {
	abstract void applyTo(Entity<?, ?> entity);
}


/** Represents an advancement of a certain distance. */
final class Advance extends Action {
	private final int distance;

	Advance(int distance) { this.distance = distance; }

	@Override
	void applyTo(Entity<?, ?> entity) {
		entity.getActor().move(distance);
	}

	@Override
	public String toString() { return "advance " + distance + " units"; }
}


/** Represents a clockwise turn of certain degrees. */
final class Turn extends Action {
	private final int degrees;

	Turn(int degrees) { this.degrees = degrees; }

	@Override
	void applyTo(Entity<?, ?> entity) {
		entity.getActor().turn(degrees);
	}

	@Override
	public String toString() { return "turn clockwise " + degrees + " degrees"; }
}

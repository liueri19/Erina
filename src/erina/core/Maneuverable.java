package erina.core;

/**
 * A Maneuverable is capable of producing Maneuver objects. This interface has a very
 * similar function to {@link java.util.function.Supplier}.
 *
 * @see Maneuver
 *
 * @version beta
 * @author Eric
 */
@FunctionalInterface
public interface Maneuverable {
	/** Gets a Maneuver. */
	Maneuver doManeuver();
}

package core;

import java.util.Map;

/**
 * This class handles Maneuvers supplied by Competitors.
 *
 * @version alpha
 * @author Eric
 */
final class ManeuverHandler {
	private ManeuverHandler() {}

	/**
	 * Handles the specified Maneuvers and apply it on the specified Entities.
	 */
	static void handle(Map<? extends Entity<?, ?>, ? extends Maneuver> maneuvers) {
		// temporary handling, no validation checking
		// TODO replace with better algorithm
		maneuvers.forEach(
						(entity, maneuver) -> {
							maneuver.getActions()
									.forEach(action -> action.applyTo(entity));
						}
		);
	}
}

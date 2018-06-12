package core;

import java.util.*;

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

		// acquire Iterators from Maneuvers
		final Map<Entity<?, ?>, Iterator<Action>> iterators = new HashMap<>();

		maneuvers.forEach(
				(entity, maneuver) ->
						iterators.put(entity, maneuver.getActions().iterator())
		);

		boolean hasMoreActions = true;

		while (hasMoreActions) {	// until all iterators are depleted
			hasMoreActions = false;

			// iterate over iterators, process one action from each iterator
			for (Map.Entry<Entity<?, ?>, Iterator<Action>> entry : iterators.entrySet()) {
				final Entity<?, ?> entity = entry.getKey();
				final Iterator<Action> iterator = entry.getValue();

				if (iterator.hasNext()) {
					hasMoreActions = true;
					iterator.next().applyTo(entity);
				}
			}
		}


		// TODO handle collisions
	}
}

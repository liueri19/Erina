package erina;

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
	 * Handles the specified Maneuvers and apply them on the corresponding Competitors.
	 */
	static void handle(Map<? extends Competitor, ? extends Maneuver> maneuvers) {
		// temporary handling, no validation checking
		// TODO replace with better algorithm

		// acquire Iterators from Maneuvers
		final Map<Competitor, Iterator<Action>> iterators = new HashMap<>();

		maneuvers.forEach(
				(competitor, maneuver) ->
						iterators.put(competitor, maneuver.getActions().iterator())
		);

		boolean hasMoreActions = true;

		while (hasMoreActions) {	// until all iterators are depleted
			hasMoreActions = false;

			// iterate over iterators, process one action from each iterator
			for (Map.Entry<Competitor, Iterator<Action>> entry : iterators.entrySet()) {
				final Competitor competitor = entry.getKey();
				final Iterator<Action> iterator = entry.getValue();

				if (iterator.hasNext()) {
					hasMoreActions = true;
					iterator.next().applyTo(competitor);


					// handle nuggets
					final List<Nugget> nuggets =
							competitor.getIntersectingObjects(Nugget.class);
					if (!nuggets.isEmpty()) {	// if we found nuggets
						// consume them
						nuggets.forEach(competitor::consume);

						// log consumption
						Logger.log("Nugget Consumption: %s consumed %s%n",
								competitor, nuggets);
					}


					// TODO handle sauce


					// TODO handle collision with other competitors
				}
			}
		}
	}
}

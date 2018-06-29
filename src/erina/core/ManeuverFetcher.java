package erina.core;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * This class helps to get Maneuvers from Maneuverables. Each Maneuverable is handled by
 * a separate thread.
 *
 * @version alpha
 * @author Eric
 */
final class ManeuverFetcher {

	private volatile boolean running = false;

	// one extra thread for dispatching fetchers
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private final ExecutorService fetchers = Executors.newCachedThreadPool();

	// Maneuverables and their next Maneuvers
	// Optional is used because ConcurrentHashMap does not allow null
	private final Map<Maneuverable, Optional<Maneuver>> maneuverableToManeuver =
			new ConcurrentHashMap<>();

	/*
	for checking if we are already fetching for a certain Maneuverable.
	if we are, it would be in this map with a matching future, and we should not submit a
	duplicated clear task.
	 */
	private final Map<Maneuverable, Future<?>> maneuverableToFuture =
			new ConcurrentHashMap<>();

	/**
	 * Causes this ManeuverFetcher to start fetching.
	 * @see	ManeuverFetcher#terminate()
	 */
	void start() {
		// if we are not already running and we are not already shutdown
		if (!running && !executor.isShutdown()) {
			running = true;	// start running
			executor.submit(() -> {	// do the following on a different thread
				// run until terminate
				while (running) {

					// for each entry
					maneuverableToManeuver.forEach((maneuverable, optional) -> {
						// if the maneuverable has not provided its next maneuver
						// and we have not asked it for its next maneuver
						if (!optional.isPresent() &&
								maneuverableToFuture.get(maneuverable) == null) {
							// store the future so we know next time not to ask again
							maneuverableToFuture.put(
									maneuverable,
									fetchers.submit(() -> {
										// ask for the maneuver
										final Maneuver nextManeuver = maneuverable.doManeuver();
										// then put its next maneuver in the map
										maneuverableToManeuver.put(
												maneuverable,
												Optional.of(nextManeuver)
										);
									})
							);

						}
					});
				}
			});
		}
		else
			throw new IllegalStateException("ManeuverFetcher cannot be started twice");
	}

	/**
	 * Causes this ManeuverFetcher to terminate. No more Maneuvers will be fetched and all
	 * threads will be shutdown.
	 * @see	ManeuverFetcher#start()
	 */
	void terminate() {
		running = false;
		executor.shutdown();
	}


	/**
	 * Submits a Maneuverable for continuous fetching.
	 * @param maneuverable	the Maneuverable to be fetched
	 */
	void submit(Maneuverable maneuverable) {
		maneuverableToManeuver.put(maneuverable, Optional.empty());
	}

	/**
	 * Removes the specified Maneuverable from this ManeuverFetcher. Maneuverables removed
	 * will no longer be fetched for Maneuvers.
	 * @param maneuverable	the Maneuverable to remove
	 */
	void remove(Maneuverable maneuverable) {
		maneuverableToFuture.remove(maneuverable);
		maneuverableToManeuver.remove(maneuverable);
	}

	/**
	 * Marks the Maneuverable ready to be fetched for further Maneuvers.
	 * @return the next Maneuver supplied by the specified Maneuverable, or null if the
	 * Maneuverable is not ready or is not being fetched by this ManeuverFetcher.
	 * @see ManeuverFetcher#get(Maneuverable)
	 */
	Maneuver clear(Maneuverable maneuverable) {
		maneuverableToFuture.remove(maneuverable);
		Optional<Maneuver> optional =
				maneuverableToManeuver.put(maneuverable, Optional.empty());
		if (optional != null)
			return optional.orElse(null);
		return null;
	}

	/**
	 * Fetches the next Maneuver of the specified Maneuverable. If the Maneuverable is not
	 * ready for the next Maneuver at the time of invocation, null is returned. However
	 * null may also mean that the specified Maneuverable is not being fetched by this
	 * ManeuverFetcher.
	 * @return	the next Maneuver supplied by the specified Maneuverable, or null if the
	 * Maneuverable is not ready or is not being fetched by this ManeuverFetcher.
	 * @see ManeuverFetcher#clear(Maneuverable)
	 */
	Maneuver get(Maneuverable maneuverable) {
		Optional<Maneuver> optional = maneuverableToManeuver.get(maneuverable);
		if (optional != null)
			return optional.orElse(null);
		return null;
	}
}

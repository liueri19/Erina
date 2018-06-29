package erina.core;

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
		// better algorithm?

		final List<Competitor> competitors = new LinkedList<>(maneuvers.keySet());

		// for each maneuver
		// the ugly mess because we are removing in loop
		outer: while (!competitors.isEmpty()) {

			final Competitor competitor = competitors.remove(0);
			final Maneuver maneuver = maneuvers.get(competitor);

			// apply maneuver to the competitor
			maneuver.applyTo(competitor);

			// update collision states
			maneuvers.keySet().forEach(Competitor::updateContacts);

			// then handle collision with other competitors
			// TODO debug collision states

			/*
			The damages from collisions between Competitors is determined by the
			position of the impact on the Competitors. If the point of impact is
			in the direction the competitor is facing, 0 damage is dealt on this
			competitor. If the POI is in the opposite direction of the facing (the
			hit is on the back of the competitor), full damage is dealt on the
			competitor. The amount of damage taken at any given angle is defined
			as:
			DAMAGE = abs(ANGLE) / PI * HIT_DAMAGE
			Where DAMAGE is the damage on the competitor taking the hit, ANGLE is
			the angle between the heading of the competitor and the ray extending
			from the center of the competitor to the center of the hitter in
			radians, HIT_DAMAGE is the maximum damage.
			Damage is applied to both parties of an impact in a single iteration.
			 */

			final List<Competitor> contacts = competitor.getNewContacts();

			for (Competitor comp : contacts) {	// each new contact is a hit

				int damage = 0;

				// just collided, calculate damage
				// competitor as hitter
//				try {
					damage = (int) Math.round(
							calculateImpactAngle(competitor, comp) / Math.PI * Competitor.HIT_DAMAGE
					);
//				}
//				catch (Exception e) {
//					e.printStackTrace();
//					System.out.println(competitor);
//					System.out.println(comp);
//				}
				comp.takeDamageFrom(competitor, damage);


				// comp as hitter
				damage = (int) Math.round(
						calculateImpactAngle(comp, competitor) / Math.PI * Competitor.HIT_DAMAGE
				);
				competitor.takeDamageFrom(comp, damage);


				// check for death
				// handled here such that death and subsequent removal of Competitor would
				// not interfere with calculation of damage

				if (comp.checkDeath(competitor))
					competitors.remove(comp);    // dead, no need to update later

				if (competitor.checkDeath(comp)) {
					// no remove because competitor is already removed at the beginning
					continue outer;	// dead, no need to handle nuggets
				}
			}


			// handle nuggets
			final List<Nugget> nuggets =
					competitor.getIntersectingObjects(Nugget.class);
			if (!nuggets.isEmpty()) {	// if we found nuggets
				// consume them
				nuggets.forEach(competitor::consume);
			}


			// Sauces handled in the Erina
		}
	}


	/**
	 * Calculates the impact angle. The impact angle is the angle between the heading of
	 * the hittee and the ray extending from the center of the hittee to the center of the
	 * hitter in radians.
	 */
	private static double calculateImpactAngle(Entity<?, ?> hitter, Entity<?, ?> hittee) {
		// TODO use a better source for hitter coordinate?
		// TODO check edge case, what if hitter coordinate equals hittee coordinate?

		final double hitteeHeading = hittee.getDirection();
		// so unicode characters are valid identifiers...
		final double Δx = hitter.getX() - hittee.getX();
		final double Δy = hitter.getY() - hittee.getY();
		final double θ = Math.abs(Math.atan2(Δy, Δx));

		double result = Math.abs(θ - Math.toRadians(hitteeHeading));

		if (result > Math.PI)
			result = 2 * Math.PI - result;

		return result;
	}

}

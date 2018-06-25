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
		// better algorithm?

		// for each maneuver
		for (Map.Entry<? extends Competitor, ? extends Maneuver> entry :
				maneuvers.entrySet()) {
			final Competitor competitor = entry.getKey();
			final Maneuver maneuver = entry.getValue();

			// apply maneuver to the competitor
			maneuver.applyTo(competitor);


			// then handle collision with other competitors

			// update collision states
			maneuvers.keySet().forEach(Competitor::updateContacts);

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

			contacts.forEach(comp1 -> {	// each new contact is a hit

				int damage;

				// comp0 and comp1 just collided, apply damage
				// comp0 as hitter
				damage = (int) Math.round(
						calculateImpactAngle(competitor, comp1) / Math.PI * Competitor.HIT_DAMAGE
				);
				comp1.takeDamageFrom(competitor, damage);


				// comp1 as hitter
				damage = (int) Math.round(
						calculateImpactAngle(comp1, competitor) / Math.PI * Competitor.HIT_DAMAGE
				);
				competitor.takeDamageFrom(comp1, damage);
			});


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
		// ANGLE = abs(atan(Delta_Y / Delta_X) - HITTEE_HEADING)

		final double hitteeHeading = hittee.getDirection();
		// so unicode characters are valid identifiers...
		final double Δx = hitter.getX() - hittee.getX();
		final double Δy = hitter.getY() - hittee.getY();
		final double θ = Math.atan2(Δy, Δx);
		return Math.abs(θ - Math.toRadians(hitteeHeading)) % (2 * Math.PI);
	}

}

package core;

import greenfoot.World;

/**
 * A concrete implementation of Actor used in Competitors.
 *
 * <p>Since Competitors are not Actors, they are not updated by the Greenfoot framework.
 * This class allows Competitors to be represented in the Erina as Actors.
 *
 * @version alpha
 * @author Eric
 */
class CompetitorActor extends EntityActor<Competitor, CompetitorActor> {

	CompetitorActor(Competitor entity) {
		super(entity);
	}

	// The following methods in Actor are overridden to call corresponding Competitor methods
	// this allows subclasses of Competitors provide different implementations by simply overriding

	@Override
	protected void addedToWorld(World world) { getEntity().addedToWorld(world); }
}

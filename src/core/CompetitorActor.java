package core;

import greenfoot.Actor;
import greenfoot.World;

import java.util.List;

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

	// The following methods allow Competitor to access protected methods in Actor

	void addedToWorldSuper(World world) { super.addedToWorld(world); }
	<T> List<T> getIntersectingObjectsSuper(Class<T> cls) { return super.getIntersectingObjects(cls); }
	<T> List<T> getNeighboursSuper(int distance, boolean diagonal, Class<T> cls) {
		return super.getNeighbours(distance, diagonal, cls);
	}
	<T> List<T> getObjectsAtOffsetSuper(int dx, int dy, Class<T> cls) { return super.getObjectsAtOffset(dx, dy, cls); }
	<T> List<T> getObjectsInRangeSuper(int radius, Class<T> cls) { return super.getObjectsInRange(radius, cls); }
	Actor getOneIntersectingObjectSuper(Class<?> cls) { return super.getOneIntersectingObject(cls); }
	Actor getOneObjectAtOffsetSuper(int dx, int dy, Class<?> cls) { return super.getOneObjectAtOffset(dx, dy, cls); }
	boolean intersectsSuper(Actor other) { return super.intersects(other); }
	boolean isTouchingSuper(Class<?> cls) { return super.isTouching(cls); }
}

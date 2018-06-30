package erina.core;

import greenfoot.Actor;
import greenfoot.World;

import java.util.List;

/**
 * An EntityActor can be converted to an Entity.
 *
 * @version 1.0
 * @author Eric
 */
abstract class EntityActor<E extends Entity<E, A>, A extends EntityActor<E, A>>
		extends Actor {

	private final E entity;

	EntityActor(E entity) {
		this.entity = entity;
	}

	E getEntity() { return entity; }


	// The following methods allow classes in erina to access protected methods in Actor


	/**
	 * Calls {@link Entity#addedToWorld(World)}. Override that method to implement custom
	 * behaviour.
	 */
	@Override
	protected final void addedToWorld(World world) { getEntity().addedToWorld(world); }

	<T> List<T> getIntersectingObjectsActor(Class<T> cls) { return super.getIntersectingObjects(cls); }
	<T> List<T> getNeighboursActor(int distance, boolean diagonal, Class<T> cls) {
		return super.getNeighbours(distance, diagonal, cls);
	}
	<T> List<T> getObjectsAtOffsetActor(int dx, int dy, Class<T> cls) { return super.getObjectsAtOffset(dx, dy, cls); }
	<T> List<T> getObjectsInRangeActor(int radius, Class<T> cls) { return super.getObjectsInRange(radius, cls); }
	// what's the point of the Class parameter if we are always returning Actor?
//	Actor getOneIntersectingObjectSuper(Class<?> cls) { return super.getOneIntersectingObject(cls); }
//	Actor getOneObjectAtOffsetSuper(int dx, int dy, Class<?> cls) { return super.getOneObjectAtOffset(dx, dy, cls); }
	boolean intersectsActor(Actor other) { return super.intersects(other); }
	boolean isTouchingActor(Class<?> cls) { return super.isTouching(cls); }
}

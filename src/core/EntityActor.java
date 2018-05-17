package core;

import greenfoot.Actor;
import greenfoot.World;

import java.util.List;

/**
 * An EntityActor can be converted to an Entity.
 *
 * @version alpha
 * @author Eric
 */
abstract class EntityActor<E extends Entity<E, A>, A extends EntityActor<E, A>>
		extends Actor {

	private E entity;

	EntityActor(E entity) {
		this.entity = entity;
	}

	E getEntity() { return entity; }


	// The following methods allow classes in core to access protected methods in Actor

	void addedToWorldSuper(World world) { super.addedToWorld(world); }
	<T> List<T> getIntersectingObjectsSuper(Class<T> cls) { return super.getIntersectingObjects(cls); }
	<T> List<T> getNeighboursSuper(int distance, boolean diagonal, Class<T> cls) {
		return super.getNeighbours(distance, diagonal, cls);
	}
	<T> List<T> getObjectsAtOffsetSuper(int dx, int dy, Class<T> cls) { return super.getObjectsAtOffset(dx, dy, cls); }
	<T> List<T> getObjectsInRangeSuper(int radius, Class<T> cls) { return super.getObjectsInRange(radius, cls); }
	// what's the point of the Class parameter if we are always returning Actor?
//	Actor getOneIntersectingObjectSuper(Class<?> cls) { return super.getOneIntersectingObject(cls); }
//	Actor getOneObjectAtOffsetSuper(int dx, int dy, Class<?> cls) { return super.getOneObjectAtOffset(dx, dy, cls); }
	boolean intersectsSuper(Actor other) { return super.intersects(other); }
	boolean isTouchingSuper(Class<?> cls) { return super.isTouching(cls); }
}

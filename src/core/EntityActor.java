package core;

import greenfoot.Actor;

/**
 * An EntityActor can be converted to an Entity.
 *
 * @version alpha
 * @author Eric
 */
abstract class EntityActor<E extends Entity> extends Actor {
	private E entity;

	E getEntity() { return entity; }
}

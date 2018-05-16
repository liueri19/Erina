package core;

import greenfoot.Actor;

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
}

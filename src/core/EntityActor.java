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

	EntityActor(E entity) {
		this.entity = entity;
		entity.init(this);
	}

	E getEntity() { return entity; }
}

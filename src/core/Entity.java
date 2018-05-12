package core;

/**
 * Represents an entity in the Erina. This class is designed to replace Actor.
 *
 * <p>Entities can be converted to EntityActors using {@link Entity#getActor()}. Similarly
 * EntityActors can be converted to Entities using {@link EntityActor#getEntity()}.
 *
 * @version alpha
 * @author Eric
 */
public abstract class Entity<A extends EntityActor> {
	private A actor;

	A getActor() { return actor; }
}

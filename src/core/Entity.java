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
	private boolean hasInit;

	/**
	 * Initialize this Entity, binds this Entity with the specified EntityActor.
	 * @param actor	the Actor to bind to
	 * @throws IllegalStateException	if this Entity has already initialized
	 */
	void init(A actor) throws IllegalStateException {
		if (!hasInit) {
			hasInit = true;
			this.actor = actor;
		}

		throw new IllegalStateException("Cannot re-initialize Entity");
	}

	A getActor() { return actor; }
}

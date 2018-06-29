package erina.core;

/**
 * A Nugget is an Entity with some energy. Competitors may acquire the energy
 * in a Nugget by touching it.
 *
 * @version alpha
 * @author Eric
 */
public final class Nugget extends Entity<Nugget, NuggetActor> {
	private final int nuggetValue;

	Nugget(Erina world, int value) {
		super(world);
		nuggetValue = value;
	}

	/**
	 * Returns the amount of energy stored in this nugget.
	 */
	public int getNuggetValue() { return nuggetValue; }

	/**
	 * Returns a string describing the amount of energy this Nugget has.
	 */
	@Override
	public String toString() {
		return "Nugget_" + nuggetValue;
	}
}

final class NuggetActor extends EntityActor<Nugget, NuggetActor> {
	NuggetActor(Nugget entity) {
		super(entity);
	}
}

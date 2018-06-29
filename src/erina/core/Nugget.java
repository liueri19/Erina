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

		// set appropriate image
		if (nuggetValue == 500)
			setImage("images/nugget_500.png");
		else if (nuggetValue == 1000)
			setImage("images/nugget_1000.png");
		else if (nuggetValue == 1500)
			setImage("images/nugget_1500.png");
		else
			throw new IllegalArgumentException("Illegal nugget value: " + nuggetValue);
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

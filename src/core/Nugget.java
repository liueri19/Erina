package core;

/**
 * A Nugget is an Entity with some energy.
 *
 * @version alpha
 * @author Eric
 */
public class Nugget extends Entity<Nugget, NuggetActor> {
	private final int nuggetValue;

	public Nugget(Erina world, int value) {
		super(world);
		nuggetValue = value;
	}

	/**
	 * Returns the amount of energy stored in this nugget.
	 */
	public int getNuggetValue() { return nuggetValue; }
}

class NuggetActor extends EntityActor<Nugget, NuggetActor> {
	NuggetActor(Nugget entity) {
		super(entity);
	}
}

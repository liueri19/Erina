import greenfoot.Actor;

/**
 * The super class of all competitors.
 */
public class Competitor {
	private final Actor actor;
	protected final String name;

	public Competitor(String name) {
		this.name = name;

		actor = new SkeletonActor();
	}
}

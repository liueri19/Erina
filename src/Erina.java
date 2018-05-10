import greenfoot.Actor;
import greenfoot.GreenfootSound;
import greenfoot.World;

import java.util.Collections;
import java.util.List;

/**
 * The Erina is the perfected version of the original beloved and wildly popular Arena.
 * This is the World where Competitors compete in.
 * The Erina aims to:
 * Force the Competitors to comply with requirements on method usage,
 * Keep it easy to implement Competitors,
 * Resolve the hitter hittee issue that plagued the original Arena.
 */
public final class Erina extends World {
	
	/*
	 * Note to future maintainers:
	 * In order to limit the Competitors from accessing certain methods, those methods have been
	 * overriden. It is important to pervent Competitors from gaining access to instances of super
	 * types (such as Actor, World), as such instances would enable the Competitors to call illegal 
	 * methods. It is also important to override (or in some other way limit) any additional 
	 * methods that may be added to Greenfoot in a future Greenfoot release.
	 */

	private static final int WORLD_WIDTH = 1024;
	private static final int WORLD_HEIGHT = 720;

	private static final String BGM_FILENAME = "resources/17 Disc Wars 1.wav";
	private static final GreenfootSound BGM = new GreenfootSound(BGM_FILENAME);

	public Erina() {
		super(WORLD_WIDTH, WORLD_HEIGHT, 1);

		System.out.println("Welcome to The Erina!");
	}


	@Override
	public void act() {

	}


	@Override
	public <T> List<T> getObjects(Class<T> cls) {
		// if a type of subtype of actor was passed in
		if (Actor.class.isAssignableFrom(cls))
			return Collections.emptyList();
		return super.getObjects(cls);
	}

	@Override
	public <T> List<T> getObjectsAt(int x, int y, Class<T> cls) {
		if (Actor.class.isAssignableFrom(cls))
			return Collections.emptyList();
		return Collections.emptyList();
	}
}

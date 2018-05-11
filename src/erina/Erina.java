package erina;

import greenfoot.Actor;
import greenfoot.GreenfootSound;
import greenfoot.World;

import java.util.Collections;
import java.util.List;

/**
 * The erina.Erina is the perfected version of the original beloved and wildly popular Arena.
 * This is the World where Competitors compete in.
 * The erina.Erina aims to:
 * Force the Competitors to comply with requirements on method usage,
 * Keep it easy to implement Competitors,
 * Resolve the hitter hittee issue that plagued the original Arena.
 */
public final class Erina extends World {
	
	/*
	 * Note to future maintainers:
	 * In order to limit the Competitors from accessing certain methods, those methods have been
	 * restricted. It is important to prevent Competitors from gaining access to instances of super
	 * types (such as Actor, World), as such instances would enable the Competitors to call illegal 
	 * methods. It is also important to override (or in some other way limit) any additional 
	 * methods that may be added to Greenfoot in future Greenfoot releases.
	 *
	 *
	 * Restricted methods in Actor as of v2.5:
	 * getIntersectingObjects(Class<A> cls)
	 * getNeighbours(int distance, boolean diagonal, Class<A> cls)
	 * getObjectsAtOffset(int dx, int dy, Class<A> cls)
	 * getObjectsInRange(int radius, Class<A> cls)
	 * getOneIntersectingObject(Class<?> cls)
	 * getOneObjectAtOffset(int dx, int dy, Class<?> cls)
	 *
	 * Competitors no longer extend Actors, instead the super class contain a private Actor field
	 * inaccessible to subclasses.
	 *
	 *
	 * Restricted methods in World as of v2.6:
	 * getObjects(Class<A> cls)
	 * getObjectsAt(int x, int y, Class<A> cls)
	 *
	 * These methods are overridden in this class.
	 *
	 *
	 * Restricted methods in MouseInfo as of v2.4:
	 * getActor()
	 *
	 * MouseInfo instances can always be acquired via the static method Greenfoot.getMouseInfo().
	 * There is no way to restrict this method, so it is important to keep the mouse off the erina.Erina
	 * during execution.
	 */

	private static final int WORLD_WIDTH = 1024;
	private static final int WORLD_HEIGHT = 720;

	private static final String BGM_FILENAME = "resources/17 Disc Wars 1.wav";
	private static final GreenfootSound BGM = new GreenfootSound(BGM_FILENAME);

	public Erina() {
		super(WORLD_WIDTH, WORLD_HEIGHT, 1);

		System.out.println("Welcome to The erina.Erina!");
	}


	@Override
	public void act() {

	}


	@Override
	public <T> List<T> getObjects(Class<T> cls) {
		// if a type of subtype of Actor was passed in
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

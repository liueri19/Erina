package core;

import greenfoot.Actor;
import greenfoot.GreenfootSound;
import greenfoot.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The Erina is the perfected version of the original beloved and wildly popular Arena.
 * This is the World where Competitors compete in.
 * <pre>
 * The Erina aims to:
 * 	Force the Competitors to comply with requirements on method usage,
 * 	Keep it easy to implement Competitors,
 * 	Resolve the hitter hittee issue that plagued the original Arena.
 * </pre>
 *
 * @version alpha
 * @author Eric
 */
public final class Erina extends World {
	
	/*
	 * Note to future maintainers:
	 * In order to limit the Competitors from accessing certain methods, those methods
	 * have been restricted. The exact method of restriction may vary. It is important to
	 * prevent Competitors from gaining access to instances of super types (such as Actor,
	 * World), as such instances would enable the Competitors to call illegal methods. It
	 * is also important to override (or in some other way limit) any additional methods
	 * that may be added to Greenfoot in future Greenfoot releases.
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
	 * Competitors no longer extend Actors, instead the super class delegates certain
	 * methods through a private Actor field inaccessible to subclasses.
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
	 * There is no way to restrict this method, so it is important to keep the mouse off
	 * the Erina during execution.
	 */

	private static final int WORLD_WIDTH = 1024;
	private static final int WORLD_HEIGHT = 720;

	private static final String BGM_FILENAME = "17 Disc Wars 1.wav";
	private static final GreenfootSound BGM = new GreenfootSound(BGM_FILENAME);


	private static final List<Competitor> COMPETITORS = new ArrayList<>();


	public Erina() {
		super(WORLD_WIDTH, WORLD_HEIGHT, 1);

		System.out.println("Welcome to The Erina!");
	}


	@Override
	public void act() {

	}



	static <T extends Entity> List<T> getEntitiesUsing(
			Function<Class<? extends EntityActor>, List<? extends EntityActor>> getter,
			Class<T> targetType) {
		return getter.apply(EntityActor.class).stream()
				.map(EntityActor::getEntity)
				.filter(e -> targetType.isAssignableFrom(e.getClass()))
				.map(e -> (T) e)
				.collect(Collectors.toList());
	}


	/**
	 * Returns all objects in this World, or all objects of the specified type if
	 * {@code cls} is not null.
	 *
	 * <p>In order to prevent returning references to Actor objects, this method modifies
	 * the behaviour of {@link World#getObjects(Class)} in two important ways. If an instance
	 * of {@code Class<Actor>} (e.g. {@code Actor.class}) was passed in, this method fails
	 * with IllegalArgumentException. If null was passed in, a List containing all objects
	 * in this World except Actors will be returned.
	 *
	 * @param cls	the Class representing the type of objects to get
	 * @param <T>	the type of objects to get
	 * @return	a List of matching objects except Actors
	 * @throws IllegalArgumentException	if an instance of {@code Class<Actor>}
	 * (e.g. {@code Actor.class}) was passed in
	 */
	@Override
	public <T> List<T> getObjects(Class<T> cls) {

		rejectType(Actor.class, cls,
				new IllegalArgumentException("Type of or subtype of Actor is unaccepted")
		);

		if (Entity.class.isAssignableFrom(cls)) {
			// refactor those?

			// get all objects of type EntityActors
			return super.getObjects(EntityActor.class).stream()
					// map actors to Entities
					.map(EntityActor::getEntity)
					// filter for entities the same type or subtype of T
					.filter(entity -> cls.isAssignableFrom(entity.getClass()))
					// cast to return type T
					.map(entity -> (T) entity)
					// collect to List
					.collect(Collectors.toList());
		}
		else {
			final List<T> results = super.getObjects(cls);
			results.removeIf(obj -> obj instanceof Actor);

			return results;
		}
	}


	/**
	 * Returns all objects at the given location, or all objects at the given location of
	 * the specified type if {@code cls} is not null.
	 *
	 * <p>In order to prevent returning references to Actor objects, this method modifies
	 * the behaviour of {@link World#getObjectsAt(int, int, Class)} in 3 important ways.
	 * If an instance of {@code Class<Actor>} (e.g. {@code Actor.class}) was passed in,
	 * this method fails with IllegalArgumentException. If null was passed in, a List
	 * containing all objects at the given location except Actors will be returned.
	 *
	 * @param x		the x coordinate of the location to find objects at
	 * @param y		the y coordinate of the location to find objects at
	 * @param cls	the Class representing the type of objects to get
	 * @param <T>	the type of objects to get
	 * @return	a List of matching objects except Actors
	 * @throws IllegalArgumentException	if an instance of {@code Class<Actor>}
	 * (e.g. {@code Actor.class}) was passed in
	 */
	@Override
	public <T> List<T> getObjectsAt(int x, int y, Class<T> cls)
			throws IllegalArgumentException {

		rejectType(Actor.class, cls,
				new IllegalArgumentException("Type of or subtype of Actor is unaccepted")
		);

		if (Entity.class.isAssignableFrom(cls)) {
					// get all objects of type EntityActors, convert list to stream
			return super.getObjectsAt(x, y, EntityActor.class).stream()
					// map actors to entities
					.map(EntityActor::getEntity)
					// filter for entities the same type or subtype of T
					.filter(entity -> cls.isAssignableFrom(entity.getClass()))
					// cast to return type T
					.map(entity -> (T) entity)
					// collect to List
					.collect(Collectors.toList());
		}
		else {
			final List<T> results = super.getObjectsAt(x, y, cls);
			results.removeIf(obj -> obj instanceof Actor);

			return results;
		}
	}


	/**
	 * Tests if the {@code testType} is or is subtype of {@code rejectedType}. Throws the
	 * specified exception if true. This method returns normally if {@code testType} is
	 * null.
	 * @param rejectedType	the undesired type
	 * @param testType		the type being tested
	 * @param e				the exception to throw
	 */
	static void rejectType(Class<?> rejectedType, Class<?> testType, RuntimeException e) {
		if (testType != null && rejectedType.isAssignableFrom(testType))
			throw e;
	}
}

package core;

import competitors.*;
import greenfoot.Actor;
import greenfoot.GreenfootSound;
import greenfoot.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

	private final String bgmFilename = "sounds/17 Disc Wars 1.wav";
	private final GreenfootSound bgm = new GreenfootSound(bgmFilename);


	private static final List<Entity<?, ?>> ENTITIES = new ArrayList<>();


	public Erina() {
		super(WORLD_WIDTH, WORLD_HEIGHT, 1);

		System.out.println("Welcome to The Erina!");


		// competitor examples
		/*
		Yes it is annoying to create Competitors like this.
		The reason is that Entity constructors must not take Actors. If they do,
		Competitors and subclasses must also take Actor to call super, which leaks
		Actor references to Competitors.
		 */

		final List<Competitor> competitors = new ArrayList<>();

		competitors.add(new TestCompetitor2(this, "TC_2"));
		competitors.add(new TestCompetitor3(this, "TC_3"));
		competitors.add(new TestCompetitor4(this, "TC_4"));
		competitors.add(new TestCompetitor5(this, "TC_5"));
		competitors.add(new TestCompetitor6(this, "TC_6"));
		competitors.add(new TestCompetitor7(this, "TC_7"));

		competitors.forEach(c -> c.init(new CompetitorActor(c)));

		ENTITIES.addAll(competitors);
	}


	@Override
	public void act() {
//		Actor a = new Actor() {};
//		a.setImage("images/ball.png");
//		addObject(a, 0, 0);
//
//		System.out.println("get Actor");
//		System.out.println(super.getObjects(Actor.class));
//
//		System.out.println("get String");
//		System.out.println(super.getObjects(String.class));
//
//		System.out.println("get null");
//		System.out.println(super.getObjects(null));
//
//		System.out.println("get null explicit");
//		System.out.println(super.<Integer>getObjects(null));
//
//
//		System.out.println("====");
//
//		Integer i = super.<Integer>getObjects(null).get(0);
//		System.out.println(i);
		// getObjects probably using raw Lists internally?


		tryPlaySound(bgm);
	}


	/**
	 * Attempts to play the specified sound. Do not play if the sound is null
	 * or is already playing.
	 * @param sound	the sound to play
	 */
	public static void tryPlaySound(GreenfootSound sound) {
		if (sound != null && !sound.isPlaying())
			sound.play();
	}


	/**
	 * Get objects of target type using the specified Function.
	 * If the targetType is or is subtype of Entity, the specified Function is invoked with EntityActor class.
	 * The returned List should never contain Actor objects.
	 * @param getter	the Function to get objects from
	 * @param targetType	the Class representing the desired type
	 * @param <T>	the desired type
	 * @return	a List of type T's returned by the specified Function
	 */
	static <T> List<T> getObjectsUsing(Function<Class<?>, List<?>> getter, Class<T> targetType) {
		// unlike greenfoot's getObjects, we don't allow null
		Objects.requireNonNull(targetType);

		// if targetType is or is subtype of Entity
		if (Entity.class.isAssignableFrom(targetType)) {
			// apply function to Entity
			return getter.apply(EntityActor.class).stream()
					// filter for EntityActors
					.filter(obj -> obj instanceof EntityActor)
					// cast to EntityActors
					.map(actor -> (EntityActor) actor)
					// get Entities from EntityActors
					.map(EntityActor::getEntity)
					// filter for Entities of the same type or subtype of T
					.filter(entity -> targetType.isAssignableFrom(entity.getClass()))
					// cast to return type
					.map(entity -> (T) entity)
					// collect to List
					.collect(Collectors.toList());
		}
		else {
			// understanding this portion is left as an exercise to the reader :)
			return getter.apply(targetType).stream()
					.filter(obj -> !(obj instanceof Actor))
					.filter(obj -> targetType.isAssignableFrom(obj.getClass()))
					.map(obj -> (T) obj)
					.collect(Collectors.toList());
		}
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

		return getObjectsUsing(super::getObjects, cls);
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

		return getObjectsUsing(
				c -> super.getObjectsAt(x, y, c),
				cls
		);
	}


	/**
	 * Adds the specified Entity to this Erina, automatically adding the linked
	 * Actor to this World as well.
	 * @param entity	the Entity to add
	 * @param x	the x position to add the specified Entity to
	 * @param y	the y position to add the specified Entity to
	 */
	public void addEntity(Entity<?, ?> entity, int x, int y) {
		ENTITIES.add(entity);
		addObject(entity.getActor(), x, y);
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

	static void rejectActorType(Class<?> cls) {
		rejectType(Actor.class, cls, new IllegalArgumentException("Type of or subtype of Actor is unaccepted"));
	}
}

package core;

import competitors.*;
import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootSound;
import greenfoot.World;

import java.util.*;
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
	 * Note to maintainers:
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

	/** Width of the Erina. */
	public static final int WORLD_WIDTH = 1024;
	/** Height of the Erina. */
	public static final int WORLD_HEIGHT = 720;

	/** Maximum number of nuggets allowed to exist simultaneously in the Erina. */
	public static final int MAX_NUGGETS = 15;

	// list of nuggets, instances in this list are reused
	private final List<Nugget> NUGGETS;

	// the BGM played through out the game
	private final String bgmFilename = "sounds/17 Disc Wars 1.wav";
	private final GreenfootSound bgm = new GreenfootSound(bgmFilename);

	// the sounds made only once at the start
	private final List<GreenfootSound> startSounds = initSounds(
			"sounds/WHOOSH_Camera_Flash.wav",
			"sounds/It's time to Duel [HQ] - YouTube.mp3"
	);

	private boolean isFirstAct = true;

	// not static because each reset in greenfoot constructs a new Erina
	private final ManeuverFetcher FETCHER = new ManeuverFetcher();

	private final List<Entity<?, ?>> ENTITIES = new ArrayList<>();


	public Erina() {
		super(WORLD_WIDTH, WORLD_HEIGHT, 1);

		System.out.println("Welcome to The Erina!");


		{
			// init nuggets, 10% are 1500, 30% are 1000, 60% are 500
			final List<Nugget> nuggets = new ArrayList<>();

			for (int i = 0; i < MAX_NUGGETS; i++) {
				if (i < 0.1 * MAX_NUGGETS)	// 10%
					nuggets.add(new Nugget(this, 1500));
				else if (i < 0.3 * MAX_NUGGETS)	// 30%
					nuggets.add(new Nugget(this, 1000));
				else	// the rest
					nuggets.add(new Nugget(this, 500));
			}

			// init NuggetActors
			nuggets.forEach(nugget -> nugget.init(new NuggetActor(nugget)));

			for (Nugget nugget : nuggets) {
				// set appropriate image
				if (nugget.getNuggetValue() == 500)
					nugget.setImage("images/nugget_500.png");
				else if (nugget.getNuggetValue() == 1000)
					nugget.setImage("images/nugget_1000.png");
				else if (nugget.getNuggetValue() == 1500)
					nugget.setImage("images/nugget_1500.png");
				else
					throw new IllegalArgumentException("Illegal nugget value: " + nugget.getNuggetValue());
			}

			NUGGETS = Collections.unmodifiableList(nuggets);
		}


		{
			// competitors
			/*
			Yes it is annoying to create Competitors like this.
			The reason is that Entity constructors must not take Actors. If they do,
			Competitors and subclasses must also take Actor to call super, which leaks
			Actor references to Competitors.
			 */

			final List<Competitor> competitors = new ArrayList<>();

			// competitors added here, follow this pattern to add/remove competitors
			competitors.add(new TestCompetitor2(this, "TC_2"));
			competitors.add(new TestCompetitor3(this, "TC_3"));
			competitors.add(new TestCompetitor4(this, "TC_4"));
			competitors.add(new TestCompetitor5(this, "TC_5"));
			competitors.add(new TestCompetitor6(this, "TC_6"));
			competitors.add(new TestCompetitor7(this, "TC_7"));

			final List<Coordinate> coordinates =
					getCoordinatesFor(competitors.size(), WORLD_WIDTH, WORLD_HEIGHT);
			// calculated coordinates are centered at the center of the world, need to convert
			coordinates.replaceAll(c -> new Coordinate(
					WORLD_WIDTH / 2 + c.x,
					WORLD_HEIGHT / 2 - c.y
			));

			// finish init, add to world, submit for updating
			for (int i = 0; i < competitors.size(); i++) {
				final Competitor competitor = competitors.get(i);
				final Coordinate coordinate = coordinates.get(i);

				competitor.init(new CompetitorActor(competitor));
				addEntity(competitor, coordinate.x, coordinate.y);
				FETCHER.submit(competitor);
			}

			FETCHER.start();
		}
	}


	@Override
	public void act() {

//		if (isFirstAct) {
//			isFirstAct = false;
//			tryPlaySounds(startSounds);
//
//			while (startSounds.get(startSounds.size()-1).isPlaying())
//				Greenfoot.delay(30);
//		}
//
//		tryPlaySound(bgm);


		{
			// add random nugget
			/*
			When and how are nuggets added:
			On each cycle, a number is calculated to represent the probability of a nugget
			being added. This probability is determined by the number of nuggets already
			existing in the world and the maximum number of nuggets allowed (MAX_NUGGETS).
			The probability is 0.01 when the current number of nuggets is 0, is 0 when the
			current number of nuggets equals to MAX_NUGGETS, and decreases linearly in
			between.
			This probability has nothing to do with what type of nugget being added; it
			represents the likelihood of any nugget being added.
			If it is determined that a nugget should be added, the specific nugget is selected
			randomly from a List of Nuggets, then added to a random location. The list is
			initialized to contain the different types of nuggets at the specified
			percentages. (10% are 1500, 30% are 1000, 60% are 500)
			 */

			final double PROBABILITY =
					(MAX_NUGGETS - getObjects(Nugget.class).size()) * 0.01 / MAX_NUGGETS;

			if (Math.random() < PROBABILITY) {	// if add nugget
				addEntity(
						NUGGETS.get(Greenfoot.getRandomNumber(MAX_NUGGETS)),
						Greenfoot.getRandomNumber(WORLD_WIDTH),
						Greenfoot.getRandomNumber(WORLD_HEIGHT)
				);
			}
		}


		{
			// TODO add random sauce

		}


		{
			// handle maneuvers

			final Map<Competitor, Maneuver> maneuvers = new HashMap<>();

			for (Entity<?, ?> entity : ENTITIES) {
				final Competitor competitor;

				if (entity instanceof Competitor)
					competitor = (Competitor) entity;
				else continue;

				final Maneuver maneuver = FETCHER.fetch(competitor);

				if (maneuver != null)
					maneuvers.put(competitor, maneuver);
			}

			// move, handle nuggets, handle sauces
			ManeuverHandler.handle(maneuvers);
		}
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
	 * Attempts to play the specified sounds in the iteration order of the Collection. Any
	 * sound that is null or is already playing is skipped.
	 * @param sounds	the sounds to play
	 */
	public static void tryPlaySounds(Collection<? extends GreenfootSound> sounds) {
		sounds.forEach(Erina::tryPlaySound);
	}

	/**
	 * Creates GreenfootSound objects for each of the sound files.
	 * @param soundFiles	filenames of the sound files
	 * @return	a List containing the created GreenfootSounds
	 */
	public static List<GreenfootSound> initSounds(String... soundFiles) {
		final List<GreenfootSound> sounds = new ArrayList<>();

		for (String file : soundFiles)
			sounds.add(new GreenfootSound(file));

		return sounds;
	}



	/** Simple data class representing a Coordinate. */
	private static class Coordinate {
		private final int x, y;
		private Coordinate(int x, int y) { this.x = x; this.y = y; }
	}

	/**
	 * Generates the Coordinates for the specified number of competitors in a world of
	 * specified width and height.
	 * The generated Coordinates are on a symmetrical shape centered at the center of the
	 * world. Each Coordinate is defined such that the distance from the Coordinate to the
	 * origin is 90% of the length of the line segment from the origin to the point of
	 * intersection formed by the bounds of the world and the line passing through the
	 * origin and the Coordinate.
	 */
	private static List<Coordinate> getCoordinatesFor(
			int numEntities,
			double width,
			double height) {
		// all angles in radians, 0 facing right, positive in counterclockwise direction
		final double anglePerEntity = 2 * Math.PI / numEntities;

		// angles from center to each corner for each quadrant
		final double quadrant1 = Math.atan(height / width);
		final double quadrant2 = Math.PI - quadrant1;
		final double quadrant3 = Math.PI + quadrant1;
		final double quadrant4 = 2 * Math.PI - quadrant1;

		final List<Coordinate> coordinates = new ArrayList<>();

//		// for each entity
		for (int i = 0; i < numEntities; i++) {
			final double angle = anglePerEntity * i;
			double opposite, adjacent, hypotenuse;

			if (angle < quadrant1 || angle > quadrant4) {	// on right side
				adjacent = width / 2;
				hypotenuse = 1 / Math.cos(angle) * adjacent;
			}
			else if (angle > quadrant1 && angle < quadrant2) {	// on top
				opposite = height / 2;
				hypotenuse = 1 / Math.sin(angle) * opposite;
			}
			else if (angle > quadrant2 && angle < quadrant3) {	// on left side
				adjacent = - width / 2;
				hypotenuse = 1 / Math.cos(angle) * adjacent;
			}
			else {	// on bottom
				opposite = - height / 2;
				hypotenuse = 1 / Math.sin(angle) * opposite;
			}

			coordinates.add(new Coordinate(
					(int) (0.9 * hypotenuse * Math.cos(angle)),
					(int) (0.9 * hypotenuse * Math.sin(angle))
			));
		}

		return coordinates;
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
	 * Returns all objects of the specified type.
	 *
	 * <p>In order to prevent returning references to Actor objects, this method modifies
	 * the behaviour of {@link World#getObjects(Class)} in two important ways. If an instance
	 * of {@code Class<Actor>} (e.g. {@code Actor.class}) was passed in, this method fails
	 * with IllegalArgumentException. If null was passed in, this method fails with
	 * NullPointerException.
	 *
	 * @param cls	the Class representing the type of objects to get
	 * @param <T>	the type of objects to get
	 * @return	a List of matching objects except Actors
	 * @throws IllegalArgumentException	if an instance of {@code Class<Actor>}
	 * (e.g. {@code Actor.class}) was passed in
	 * @throws NullPointerException	if null was passed in
	 */
	@Override
	public <T> List<T> getObjects(Class<T> cls) {

		rejectType(Actor.class, cls,
				new IllegalArgumentException("Type of or subtype of Actor is unaccepted")
		);

		return getObjectsUsing(super::getObjects, cls);
	}


	/**
	 * Returns all objects at the given location of the specified type.
	 *
	 * <p>In order to prevent returning references to Actor objects, this method modifies
	 * the behaviour of {@link World#getObjectsAt(int, int, Class)} in two important ways.
	 * If an instance of {@code Class<Actor>} (e.g. {@code Actor.class}) was passed in,
	 * this method fails with IllegalArgumentException. If null was passed in, this method
	 * fails with NullPointerException.
	 *
	 * @param x		the x coordinate of the location to find objects at
	 * @param y		the y coordinate of the location to find objects at
	 * @param cls	the Class representing the type of objects to get
	 * @param <T>	the type of objects to get
	 * @return	a List of matching objects except Actors
	 * @throws IllegalArgumentException	if an instance of {@code Class<Actor>}
	 * (e.g. {@code Actor.class}) was passed in
	 * @throws NullPointerException	if null was passed in
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
		super.addObject(entity.getActor(), x, y);
	}


	/**
	 * Consider using {@link Erina#addEntity(Entity, int, int)} instead.
	 */
	@Override @Deprecated
	public void addObject(Actor object, int x, int y) {
		super.addObject(object, x, y);
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

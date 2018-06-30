package erina.core;

import competitors.*;
import erina.Coordinate;
import erina.Pair;
import greenfoot.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The World where Competitors compete in.
 * The Erina is the perfected version of the original beloved and wildly popular Arena.
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

	/** Maximum number of cycles before the competition ends. */
	public static final int MAX_CYCLES = 10000;

	/** Energy rewarded to surviving Competitors each cycle. */
	public static final int ENERGY_PER_CYCLE = 1;

	// list of nuggets, instances in this list are reused
	private final List<Nugget> NUGGETS;
	// same for sauces
	private final List<Sauce> SAUCES;

	// the BGM played through out the game
	private final String BGM_FILE = "sounds/17 Disc Wars 1.wav";
	private final GreenfootSound BGM = new GreenfootSound(BGM_FILE);

	// the sounds made only once at the start
	private final List<GreenfootSound> START_SOUNDS = initSounds(
			"sounds/WHOOSH_Camera_Flash.wav",
			"sounds/It's time to Duel [HQ] - YouTube.mp3"
	);

	// the sound played at the end of game
	private final String EOG_SOUND_FILE = "sounds/RISE-SHORT_Snap.wav";
	private final GreenfootSound EOG_SOUND = new GreenfootSound(EOG_SOUND_FILE);

	private boolean isFirstAct = true;

	private long currentCycle = 0;

	// not static because each reset in greenfoot constructs a new Erina
	private final ManeuverFetcher FETCHER = new ManeuverFetcher();

	/*
	ENTITIES contain all Entities in the Erina, including Nuggets, Competitors, etc.
	COMPETITORS only contain Competitors. It is a subset of ENTITIES.
	 */
	private final List<Entity<?, ?>> ENTITIES = new LinkedList<>();

	private final List<Competitor> COMPETITORS = new LinkedList<>();
	private final List<Competitor> DECEASED_COMPS = new LinkedList<>();


	public Erina() {
		super(WORLD_WIDTH, WORLD_HEIGHT, 1);

		setBackground("images/bathroom-tile.jpg");

		System.out.println("Welcome to The Erina!");


		{
			// init nuggets, 10% are 1500, 30% are 1000, 60% are 500
			final List<Nugget> nuggets = new ArrayList<>();

			// doesn't really matter, 10 is a nice number
			final int someNum = 10;
			for (int i = 0; i < someNum; i++) {
				if (i < 0.1 * someNum)	// 10%
					nuggets.add(new Nugget(this, 1500));
				else if (i < 0.3 * someNum)	// 30%
					nuggets.add(new Nugget(this, 1000));
				else	// the rest
					nuggets.add(new Nugget(this, 500));
			}

			// init NuggetActors
			nuggets.forEach(nugget -> nugget.init(new NuggetActor(nugget)));

			NUGGETS = Collections.unmodifiableList(nuggets);
		}


		{
			// init sauces, very similar to nuggets
			final List<Sauce> sauces = new ArrayList<>();

			final int someNum = 10;
			for (int i = 0; i < someNum; i++) {
				if (i < 0.1 * someNum)
					sauces.add(new Sauce(this, 1500));
				else if (i < 0.3 * someNum)
					sauces.add(new Sauce(this, 1000));
				else
					sauces.add(new Sauce(this, 500));
			}

			// init actors
			sauces.forEach(sauce -> sauce.init(new SauceActor(sauce)));

			SAUCES = Collections.unmodifiableList(sauces);
		}


		{
			// competitors
			/*
			Yes it is annoying to create Competitors like this.
			The reason is that Entity constructors must not take Actors. If they do,
			Competitors and subclasses must also take Actor to call super, which leaks
			Actor references to Competitors.
			 */

			// COMPETITORS not used because Erina#addEntity also adds Competitor to COMPETITORS
			final List<Competitor> competitors = new ArrayList<>();

			// competitors added here, follow this pattern to add/remove competitors
			competitors.add(new TestCompetitor2(this, "TC_2"));
			competitors.add(new TestCompetitor3(this, "TC_3"));
			competitors.add(new TestCompetitor4(this, "TC_4"));
			competitors.add(new TestCompetitor5(this, "TC_5"));
			competitors.add(new EvanSchimberg(this, "Evan"));
			competitors.add(new Jstew(this, "Jstew"));

			final List<Coordinate> coordinates =
					getCoordinatesFor(competitors.size(), WORLD_WIDTH, WORLD_HEIGHT);
			// calculated coordinates are centered at the center of the world, need to convert
			coordinates.replaceAll(c -> new Coordinate(
					WORLD_WIDTH / 2 + c.getX(),
					WORLD_HEIGHT / 2 - c.getY()
			));

			for (int i = 0; i < competitors.size(); i++) {
				final Competitor competitor = competitors.get(i);
				final Coordinate coordinate = coordinates.get(i);

				// finish init
				competitor.init(new CompetitorActor(competitor));
				// add to world, this also adds Competitor to COMPETITORS
				addEntity(competitor, coordinate.getX(), coordinate.getY());
				// add NameTags
				final Pair<Integer, Integer> offset = getNameTagOffset(competitor);
				addObject(competitor.getNameTag(),
						competitor.getX() + offset.getKey(),
						competitor.getY() - offset.getValue()
				);

				// submit for updating
				FETCHER.submit(competitor);
			}

			FETCHER.start();
		}
	}


	@Override
	public void act() {

		if (isFirstAct) {
			isFirstAct = false;
			tryPlaySounds(START_SOUNDS);

			while (START_SOUNDS.get(START_SOUNDS.size()-1).isPlaying())
				Greenfoot.delay(30);
		}

		tryPlaySound(BGM);

		tryAddNugget();
		tryAddSauce();

		updateManeuvers();
		updateSauces();

		COMPETITORS.forEach(competitor -> {
			competitor.changeEnergy(ENERGY_PER_CYCLE);
			competitor.getStats().incrementCyclesSurvived();
		});


		currentCycle++;

		// if game ended
		if (COMPETITORS.size() < 2 || currentCycle >= MAX_CYCLES) {
			FETCHER.shutdown();

			logGameEnded();

			// play end of game sound
			tryPlaySound(EOG_SOUND);
			Greenfoot.delay(1000);	// not sure what's this for but it's in the original

			final List<Competitor> ALL_COMPS = new ArrayList<>(COMPETITORS);
			ALL_COMPS.addAll(DECEASED_COMPS);

			// calculate scores
			// I don't see why we should calculate scores differently for alive and dead
			// competitors...
			ALL_COMPS.forEach(competitor -> competitor.getStats().calculateScore());

			// rank Competitors
			final Comparator<Competitor> compareByScore =
					Comparator.comparingInt(comp -> comp.getStats().getScore());
			ALL_COMPS.sort(compareByScore.reversed());	// reversed for highest to lowest


			// display score board
			addObject(
					new ScoreBoard(1000, 700, ALL_COMPS),
					WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
		}


		else if (currentCycle % 500 == 0)	// log cycle milestones
			Logger.logLine("Cycle: %d", currentCycle);
	}


	/**
	 * Attempts to add a random Nugget to this Erina. A Nugget may or may not be added as
	 * the result.
	 *
	 * <p>When and how are nuggets added:
	 * <p>On each cycle, a number is calculated to represent the probability of a nugget
	 * being added. This probability is determined by the number of nuggets already
	 * existing in the world and the maximum number of nuggets allowed (MAX_NUGGETS).
	 * The probability is 0.01 when the current number of nuggets is 0, is 0 when the
	 * current number of nuggets equals to MAX_NUGGETS, and decreases linearly in
	 * between.
	 * <p>This probability has nothing to do with what type of nugget being added; it
	 * represents the likelihood of any nugget being added.
	 * <p>If it is determined that a nugget should be added, the specific nugget is
	 * selected randomly from a List of Nuggets, then added to a random location. The list
	 * is initialized to contain the different types of nuggets at the specified
	 * percentages. (10% are 1500, 30% are 1000, 60% are 500)
	 */
	private void tryAddNugget() {
		final double PROBABILITY =
				(MAX_NUGGETS - getObjects(Nugget.class).size()) * 0.01 / MAX_NUGGETS;

		if (Math.random() < PROBABILITY) {	// if add nugget
			addEntity(
					NUGGETS.get(Greenfoot.getRandomNumber(NUGGETS.size())),
					Greenfoot.getRandomNumber(WORLD_WIDTH),
					Greenfoot.getRandomNumber(WORLD_HEIGHT)
			);
		}
	}

	/**
	 * Attempts to add a random Sauce to this Erina. A Sauce may or may not be added as
	 * the result.
	 */
	private void tryAddSauce() {
		/*
		In the original Arena, sauce was added every 200 cycles plus a random number
		from 0 to 299, so average to 1 sauce every 350 cycles. This probability should
		give about the same frequency.
		 */
		final double PROBABILITY = 1d / 350;

		if (Math.random() < PROBABILITY) {
			addEntity(
					SAUCES.get(Greenfoot.getRandomNumber(SAUCES.size())),
					Greenfoot.getRandomNumber(WORLD_WIDTH),
					Greenfoot.getRandomNumber(WORLD_HEIGHT)
			);
		}
	}


	/**
	 * Updates Maneuver related stuff, including Nugget consumptions.
	 * This method collects and handles maneuvers.
	 */
	private void updateManeuvers() {
		// collect maneuvers from competitors
		final Map<Competitor, Maneuver> maneuvers = new HashMap<>();

		for (Competitor competitor : COMPETITORS) {
			final Maneuver maneuver = FETCHER.get(competitor);

			if (maneuver != null)
				maneuvers.put(competitor, maneuver);
		}

		// move, handle nuggets
		ManeuverHandler.handle(maneuvers);

		// fetch next maneuvers
		COMPETITORS.forEach(FETCHER::clear);
	}

	/**
	 * Updates Sauce related stuff.
	 * Sauce has a timeout, therefore not handled in ManeuverHandler.
	 */
	private void updateSauces() {
		// sauces need to be updated so they are not handled with maneuvers
		SAUCES.forEach(sauce -> {
			// if this sauce is in the world
			if (this.getObjects(Sauce.class).contains(sauce)) {
				// if contact with competitor, start countdown
				if (!sauce.getIntersectingObjects(Competitor.class).isEmpty())
					sauce.startCountdown();

				sauce.updateTimeout();

				if (sauce.hasTimedOut()) {
					// timed out, give energy to intersecting competitors
					final List<Competitor> intersectingCompetitors =
							sauce.getIntersectingObjects(Competitor.class);
					final int multiplier = intersectingCompetitors.size();
					intersectingCompetitors.forEach(
							competitor -> competitor.consume(sauce, multiplier)
					);

					removeEntity(sauce);
					sauce.resetTimeout();
				}
			}
		});
	}



	/**
	 * Attempts to play the specified sound. Do not play if the sound is null
	 * or is already playing.
	 * @param sound	the sound to play
	 */
	public static void tryPlaySound(GreenfootSound sound) {
		// TODO uncomment
//		if (sound != null && !sound.isPlaying())
//			sound.play();
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


	/**
	 * Generates the Coordinates for the specified number of competitors in a world of
	 * specified width and height.
	 * The generated Coordinates are on a symmetrical shape centered at the center of the
	 * world. Each Pair is defined such that the distance from the Pair to the
	 * origin is 90% of the length of the line segment from the origin to the point of
	 * intersection formed by the bounds of the world and the line passing through the
	 * origin and the Pair.
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
	 * Calculates the offsets for displaying the NameTag that follows the Competitor.
	 * @return a Pair holding the x and y offsets from the location of the Competitor
	 */
	static Pair<Integer, Integer> getNameTagOffset(Competitor competitor) {
		final GreenfootImage image = competitor.getImage();

		if (image != null) {
			return new Pair<>(image.getWidth() / 2, image.getHeight() / 2);
		}
		else {
			return new Pair<>(10, 10);
		}
	}


	/**
	 * Log some information about the end of the game.
	 */
	private void logGameEnded() {
		// game ended
		Logger.logLine("********** Game Ended! **********");
		Logger.logLine("Cycle: %d", currentCycle);

		Logger.logLine();

		// show Competitors left
		Logger.logLine("%d Competitors left in play...", COMPETITORS.size());
		COMPETITORS.forEach(competitor ->
				Logger.logLine(competitor.getStats().toString()));

		Logger.logLine();

		// show dead Competitors
		Logger.logLine("%d Competitors were killed in the Erina...", DECEASED_COMPS.size());
		DECEASED_COMPS.forEach(competitor -> {
			final CompetitorStats stats = competitor.getStats();
			Logger.log(stats.toString());
			Logger.logLine(", KilledBy:%15s", stats.getLastAttacker());
		});
	}


	/**
	 * Gets the number of times the {@link Erina#act()} method was run.
	 */
	public final long getCurrentCycle() { return currentCycle; }

	/**
	 * Get objects of target type using the specified Function, removing all Actor
	 * instances from the result.
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
			return getter.apply(targetType).stream()
					.filter(obj -> !(obj instanceof Actor))
					.filter(obj -> targetType.isAssignableFrom(obj.getClass()))
					.map(obj -> (T) obj)
					.collect(Collectors.toList());
		}
	}


	/**
	 * Returns all objects of the specified type except Actor instances.
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
	 * Returns all objects at the given location of the specified type except Actor
	 * instances.
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
	 * @see World#addObject(Actor, int, int)
	 */
	void addEntity(Entity<?, ?> entity, int x, int y) {
		ENTITIES.add(entity);
		addObject(entity.getActor(), x, y);

		if (entity instanceof Competitor)
			COMPETITORS.add((Competitor) entity);
	}


	/**
	 * Removes the specified Entity from the world.
	 * @param entity	the Entity to remove
	 * @see World#removeObject(Actor)
	 */
	void removeEntity(Entity<?, ?> entity) {
		ENTITIES.remove(entity);
		removeObject(entity.getActor());

		if (entity instanceof Maneuverable)	// includes null check
			FETCHER.remove((Maneuverable) entity);

		if (entity instanceof Competitor) {
			final Competitor competitor = (Competitor) entity;
			COMPETITORS.remove(competitor); DECEASED_COMPS.add(competitor);
			removeObject(competitor.getNameTag());
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

	static void rejectActorType(Class<?> cls) {
		rejectType(Actor.class, cls, new IllegalArgumentException("Type of or subtype of Actor is unaccepted"));
	}
}

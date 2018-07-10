import erina.core.Competitor;
import erina.core.Erina;
import erina.core.Maneuver;
import greenfoot.Greenfoot;

/**
 * Class TestCompetitor2
 *
 * @author Brian Arnold
 * @version April 2017
 * <p>
 * This is a limited-functionality example of an instance of a Competitor class.
 * Each subclass of the Competitor class is differentiated by the contents of the
 * doManuever() method. This is the method that contains the AI/heuristics/smarts
 * intended to maximize hits and damage to other Competitors in the Arena, while
 * minimizing the hits/damage to itself.
 * <p>
 * doManuever() is where you create the secret sauce, but you must follow the
 * stated rules for what is and is not acceptable in doManuever().
 */
public class TestCompetitor2 extends Competitor {

	private int moveDistance = 3;                       // Default number of cells to move each time
	private static final int ENERGY_NORMAL_RESERVE = 10;    // Used in logic for energy management
	private static final int SEEK_RANGE = 450;              // Used in logic to learn who is out there...
	private static final int EDGE_MARGIN = 10;
	int turnCountDown;                                  // Don't want to turn every act().  Space it out.

	public TestCompetitor2(Erina world, String thisName) {
		super(world, thisName);

		setImage("images/jellybean1.png");

		// if you want a sound for getting hit or making a hit, you need to
		// assign two string variables to the name of the .wav or .mp3 file
		// stringSoundHorror and stringSoundSadistic.
		// Then the rest of the code below should work.

		setHorrorSound("sounds/Slurping+1.wav");
		setSadisticSound("sounds/Smirk+1.wav");
		setKillSound("sounds/Yell+Male+Wahhh.wav");
		setDeathSound("sounds/Scream+14.wav");

//		stringSoundHorror = "Slurping+1.wav";
//		stringSoundSadistic = "Smirk+1.wav";
//		stringSoundKill = "Yell+Male+Wahhh.wav";
//		stringSoundGotKilled = "Scream+14.wav";
//
//
//		if (stringSoundHorror != null) {
//			soundHorror = new GreenfootSound(stringSoundHorror);
//		}
//		if (stringSoundSadistic != null) {
//			soundSadistic = new GreenfootSound(stringSoundSadistic);
//		}
//		if (stringSoundKill != null) {
//			soundKill = new GreenfootSound(stringSoundKill);
//		}
//		if (stringSoundGotKilled != null) {
//			soundGotKilled = new GreenfootSound(stringSoundGotKilled);
//		}


	}

	/*
	 * doManeuver()
	 *
	 * This method performs the special maneuvering needed to avoid getting hit while
	 * increasing getting hits on other Competitors.
	 *
	 * Follow the rules on what is and is not legal to use in thie method.
	 */
	@Override
	public Maneuver doManeuver() {
		final Maneuver maneuver = new Maneuver(this);

		int newNum;
		int worldHeight;        // need world dimensions to help with determining coordinates
		int worldWidth;

		// update the turn counter.  Can't turn normally for this many cycles...
		// Some say it avoids churn.  Some say it's unnecessary.  It's just here as an example.
		turnCountDown--;

		// We're here because there weren't any Competitors within SEEK_RANGE of us,
		// so we're going to hunt around for one.
		// Need to handle when we're at the edge of the world, though...
		worldHeight = getWorld().getHeight() - 1;
		worldWidth = getWorld().getWidth() - 1;

		// where are we now?
		int xNow = getX();
		int yNow = getY();

		// if we're at one of the edges, turn towards some random degree...
		if ((xNow <= EDGE_MARGIN) || (yNow <= EDGE_MARGIN) || (xNow >= worldWidth - EDGE_MARGIN) || (yNow >= worldHeight - EDGE_MARGIN)) {

			newNum = Greenfoot.getRandomNumber(360);    // some random degrees...


			maneuver.turn(newNum);

		}
		else if (turnCountDown <= 0) {
			newNum = Greenfoot.getRandomNumber(181) - 90;  // within 90 degrees left or right
			maneuver.turn(newNum);
			turnCountDown = Greenfoot.getRandomNumber(10);  // renew turn counter
		}

		maneuver.move(moveDistance);

		return maneuver;
	}
}

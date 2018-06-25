package competitors;

import erina.Competitor;
import erina.Erina;
import erina.Maneuver;
import greenfoot.Greenfoot;

import java.util.List;

/**
 * Class TestCompetitor4
 *
 * @author Brian Arnold, Eric
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
 * <p>
 * <p>
 */
public class TestCompetitor4 extends Competitor {

	private static final int EDGE_MARGIN = 10;
	private static final int ENERGY_NORMAL_RESERVE = 10;    // Used in logic for energy management
	private static final int SEEK_RANGE = 150;              // Used in logic to learn who is out there...
	int turnCountDown;                                  // Don't want to turn every act().  Space it out.
	private int moveDistance = 3;                       // Default number of cells to move each time

	public TestCompetitor4(Erina world, String thisName) {
		super(world, thisName);

		setImage("images/YellowArrow1.png");

		// if you want a sound for getting hit or making a hit, you need to
		// assign two string variables to the name of the .wav or .mp3 file
		// stringSoundHorror and stringSoundSadistic.
		// Then the rest of the code below should work.

//		stringSoundHorror = "Gasp+3.wav";
//		stringSoundSadistic = "Bite+3.wav";
//		stringSoundKill = "Laugh+1.wav";
//		stringSoundGotKilled = "Scream+Female+4.wav";

		setHorrorSound("sounds/Gasp+3.wav");
		setSadisticSound("sounds/Bite+3.wav");
		setKillSound("sounds/Laugh+1.wav");
		setDeathSound("sounds/Scream+Female+4.wav");


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
	public Maneuver doManeuver() {
//		super.doManeuver();

		final Maneuver maneuver = new Maneuver(this);

		int newNum;

		// local variables to help in coordinate logic
		int worldHeight;        // need world dimensions to help with determining coordinates
		int worldWidth;
		int targetX = 0;        // this is where I'm heading
		int targetY = 0;
		int newX = 0;
		int newY = 0;
		int newDistance;

		Competitor oneComp;
		Competitor targetCompetitor;

		List<Competitor> listInRange;       // a list of all the Actor objects within range

		// update the cycle count in case we're waiting to turn...
		turnCountDown--;

		// Get a list of all the Competitor objects within range.  May need them later
		listInRange = getObjectsInRange(SEEK_RANGE, Competitor.class);

		// geometry and location.  Need to handle edge positions first.
		worldHeight = getWorld().getHeight() - 1;
		worldWidth = getWorld().getWidth() - 1;

		int xNow = getX();
		int yNow = getY();

		if ((xNow <= EDGE_MARGIN) || (yNow <= EDGE_MARGIN) || (xNow >= worldWidth - EDGE_MARGIN) || (yNow >= worldHeight - EDGE_MARGIN)) {
			// move to the middle...
			//legalTurnTowards((worldWidth/2), (worldHeight/2));
			maneuver.turnTowards((worldWidth / 2), (worldHeight / 2));
			newDistance = Greenfoot.getRandomNumber(100);     // get a new distance
			maneuver.move(newDistance);
		}
		else if (listInRange.size() > 0) {
			targetCompetitor = (Competitor) listInRange.get(0);        // get the first Actor in the list
			//System.out.printf("listInRange.size() is %d,\n", listInRange.size());
			targetX = targetCompetitor.getX();                    // Get its x coordinate
			targetY = targetCompetitor.getY();                    // Get its y coordinate

			// Turn away from the other Competitor!
			if (xNow < targetX)
				newX = xNow - 10;
			else
				newX = xNow + 10;

			if (yNow < targetY)
				newY = yNow - 10;
			else
				newY = yNow + 10;

			if (newX < 0) newX = 0;
			if (newX > worldWidth - 50) newX = worldWidth - 50;
			if (newY < 0) newY = 0;
			if (newY > worldHeight - 50) newY = worldHeight - 50;

			maneuver.turnTowards(newX, newY);
			newDistance = Greenfoot.getRandomNumber(10);     // get a new distance
			maneuver.move(newDistance);

		}
		else if (turnCountDown <= 0) {
			newNum = Greenfoot.getRandomNumber(181) - 30;  // within 90 degrees left or right
			maneuver.turn(newNum);
			//turn(newNum);
			turnCountDown = Greenfoot.getRandomNumber(10);// renew turn counter
			newDistance = Greenfoot.getRandomNumber(10);     // get a new distance
			maneuver.move(newDistance);
		}
		// Turns are complete, now we can move in the direction we turned...
		// Go!

		// return maneuver, submit for execution
		return maneuver;
	}


}

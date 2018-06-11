package competitors;

import core.Competitor;
import core.Erina;
import core.Maneuver;
import greenfoot.Greenfoot;

import java.util.List;

/**
 * Class TestCompetitor5
 *
 * @author Brian Arnold
 * @version March 2016
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
public class TestCompetitor5 extends Competitor {

	private static final int NORMAL_MOVE_DISTANCE = 3;
	private static final int ENERGY_NORMAL_RESERVE = NORMAL_MOVE_DISTANCE * 4;// Used in logic for energy management
	private int moveDistance = NORMAL_MOVE_DISTANCE;        // Default number of cells to move each time
	private static final int SHORT_SEEK_RANGE = 150;              // Used in logic to learn who is out there...
	private static final int MED_SEEK_RANGE = 400;              // Used in logic to learn who is out there...
	private static final int LONG_SEEK_RANGE = 750;              // Used in logic to learn who is out there...
	private static final int GETAWAY_DISTANCE = 160;
	private static final int EDGE_MARGIN = 10;
	int turnCountDown;                                      // Don't want to turn every act().  Space it out.
	private int justInflictedHitCooldown = 10;

	public TestCompetitor5(Erina world, String thisName) {
		super(world, thisName);


		// if you want a sound for getting hit or making a hit, you need to
		// assign two string variables to the name of the .wav or .mp3 file
		// stringSoundHorror and stringSoundSadistic.
		// Then the rest of the code below should work.
//		stringSoundHorror = "Gasp+1.wav";
//		stringSoundSadistic = "Chomp+1.wav";
//		stringSoundKill = "attack.wav";
//		stringSoundGotKilled = "Scream+8.wav";
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


		setHorrorSound("sounds/Gasp+1.wav");
		setSadisticSound("sounds/Chomp+1.wav");
		setKillSound("sounds/attack.wav");
		setDeathSound("sounds/Scream+8.wav");


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
//		super.doManuever();

		final Maneuver maneuver = new Maneuver(this);

		int newNum;

		// local variables to help in coordinate logic
		int worldHeight;        // need world dimensions to help with determining coordinates
		int worldWidth;
		int targetX = 0;        // this is where I'm heading
		int targetY = 0;
		int newDistance;
		Competitor oneComp;
		Competitor targetCompetitor;

		worldHeight = getWorld().getHeight() - 1;
		worldWidth = getWorld().getWidth() - 1;

		if (justInflictedHitCooldown > 0) {
			justInflictedHitCooldown--;
			//turnTowards((worldWidth/2), (worldHeight/2));
			return maneuver.turnTo((worldWidth / 2), (worldHeight / 2))
					.move(10);
		}

		List<Competitor> listInRange;   // a list of all the Actor objects within range
		listInRange = getObjectsInRange(SHORT_SEEK_RANGE, Competitor.class);  // Get a list of all the Actor objects within range

		// If there's another Actor out there within range, turn towards it
		// and start moving in that direction.
		// If not, move randomly (seek).

		turnCountDown--;
		if (listInRange.size() > 0) {
			targetCompetitor = (Competitor) listInRange.get(0);     // get the [unlucky] first Actor
			// in the list...
			//System.out.printf("listInRange.size() is %d,\n", listInRange.size());
			targetX = targetCompetitor.getX();                      // Get its x coordinate
			targetY = targetCompetitor.getY();                      // Get its y coordinate
			maneuver.turnTo(targetX, targetY);                     // Got its location, now go after 'em!
			//turnTowards(targetX, targetY);

			// where are we now?
			int xNow = getX();
			int yNow = getY();
			int dx = Math.abs(targetX - xNow);
			int dy = Math.abs(targetY - yNow);

			newDistance = (int) (Math.sqrt((dx * dx) + (dy * dy)));

			//newDistance = Greenfoot.getRandomNumber(6);             // ...a  random distance, hopefully...
			maneuver.move(newDistance);                                 // If we have enough energy, legalMove()
			// will allow it.  Otherwise we have
			// to wait another cycle or more...
			// should have been a hit!
			// if it was, move away next cycle and wait for cooldown
			//

			if (isTouching(Competitor.class) == true) {
				justInflictedHitCooldown = 10;
			}


		}
		else {

			// We're here because there weren't any Competitors within SEEK_RANGE of us,
			// so we're going to hunt around for one.
			// Need to handle when we're at the edge of the world, though...

			// where are we now?
			int xNow = getX();
			int yNow = getY();

			// if we're at one of the edges, turn towards the middle of the world...
			if ((xNow <= EDGE_MARGIN) || (yNow <= EDGE_MARGIN) || (xNow >= worldWidth - EDGE_MARGIN) || (yNow >= worldHeight - EDGE_MARGIN)) {
				// turn if we have energy
				maneuver.turnTo((worldWidth / 2), (worldHeight / 2));
				//turnTowards((worldWidth/2), (worldHeight/2));
				if (getEnergyLevel() > ENERGY_NORMAL_RESERVE)
					maneuver.move(moveDistance);

			}
			else if (turnCountDown <= 0) {
				// we're not at the edge, and the turning countdown has expired, so turn if we need to
				newNum = Greenfoot.getRandomNumber(181) - 90;
				maneuver.turn(newNum);
				//turn(newNum);
				if (getEnergyLevel() > ENERGY_NORMAL_RESERVE)
					maneuver.move(moveDistance);

				turnCountDown = 40;    // reset the turn counter
			}

			// we've figured out which direction to turn, now figure out how much to move..
			//if (energyLevel > ENERGY_NORMAL_RESERVE)
			//    legalMove(moveDistance);
		}

		return maneuver;
	}


}

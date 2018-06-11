package competitors;

import core.Competitor;
import core.Erina;
import core.Maneuver;
import core.Nugget;
import greenfoot.Greenfoot;

import java.util.List;

/**
 * Class TestCompetitor6
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
public class TestCompetitor6 extends Competitor {


	private static final int NORMAL_MOVE_DISTANCE = 3;
	private static final int ENERGY_NORMAL_RESERVE = NORMAL_MOVE_DISTANCE * 4;// Used in logic for energy management
	private int moveDistance = NORMAL_MOVE_DISTANCE;        // Default number of cells to move each time
	private static final int SEEK_RANGE = 450;              // Used in logic to learn who is out there...
	private static final int EDGE_MARGIN = 10;
	int turnCountDown;                                      // Don't want to turn every act().  Space it out.
	private int justInflictedHitCooldown = 10;


	public TestCompetitor6(Erina world, String thisName) {
		super(world, thisName);


		// if you want a sound for getting hit or making a hit, you need to
		// assign two string variables to the name of the .wav or .mp3 file
		// stringSoundHorror and stringSoundSadistic.
		// Then the rest of the code below should work.

//		stringSoundHorror = "Yell+3.wav";
//		stringSoundSadistic = "PUNCH.wav";
//		stringSoundKill = "GotchaHah.wav";
//		stringSoundGotKilled = "Scream+5.wav";
//		//stringSoundGotNugget = "Tambourine1.wav";
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
		//if (stringSoundGotNugget != null)
		//{
		//    soundGotNugget =new GreenfootSound(stringSoundGotNugget);
		//}

		setHorrorSound("sounds/Yell+3.wav");
		setSadisticSound("sounds/PUNCH.wav");
		setKillSound("sounds/GotchaHah.wav");
		setDeathSound("sounds/Scream+5.wav");

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
		Nugget oneCompNugget;
		Nugget targetCompetitorNugget;

		worldHeight = getWorld().getHeight() - 1;
		worldWidth = getWorld().getWidth() - 1;

		List<Competitor> listInRange;   // a list of all the Actor objects within range
		List<Nugget> listInRangeNugget;   // a list of all the Actor objects within range
		listInRange = getObjectsInRange(SEEK_RANGE, Competitor.class);  // Get a list of all the Actor objects within range
		listInRangeNugget = getObjectsInRange(SEEK_RANGE, Nugget.class);  // Get a list of all the Actor objects within range

		// If there's another Actor out there within range, turn towards it
		// and start moving in that direction.
		// If not, move randomly (seek).

		turnCountDown--;
		if (getEnergyLevel() < ENERGY_NORMAL_RESERVE)
			return maneuver;      // don't even attempt to move or turn unless we have lots of energy

		if (listInRangeNugget.size() > 0) {
			//targetCompetitor = (Competitor) listInRange.get(0);     // get the [unlucky] first Actor
			targetCompetitorNugget = (Nugget) listInRangeNugget.get(0);     // get the [unlucky] first Actor
			// in the list...
			//System.out.printf("listInRange.size() is %d,\n", listInRange.size());
			targetX = targetCompetitorNugget.getX();                      // Get its x coordinate
			targetY = targetCompetitorNugget.getY();                      // Get its y coordinate
			//legalTurnTowards(targetX, targetY);                     // Got its location, now go after 'em!
			int variationX = Greenfoot.getRandomNumber(30) - 15;
			int variationY = Greenfoot.getRandomNumber(30) - 15;

			maneuver.turnTo(targetX + variationX, targetY + variationY);

			newDistance = Greenfoot.getRandomNumber(6);             // ...a  random distance, hopefully...
			if (getEnergyLevel() > ENERGY_NORMAL_RESERVE)
				maneuver.move(newDistance);                             // If we have enough energy, legalMove()
			// will allow it.  Otherwise we have
			// to wait another cycle or more...
			// Reset this cooldown timer if we're going for Nuggets...
			justInflictedHitCooldown = 0;

		}
		else if (justInflictedHitCooldown > 0) {
			justInflictedHitCooldown--;
			//turnTowards((worldWidth/2), (worldHeight/2));
			maneuver.turnTo((worldWidth / 2), (worldHeight / 2));
			if (getEnergyLevel() > ENERGY_NORMAL_RESERVE)
				maneuver.move(5);
			return maneuver;
		}
		else if (listInRange.size() > 0) {
			//targetCompetitor = (Competitor) listInRange.get(0);     // get the [unlucky] first Actor
			targetCompetitor = (Competitor) listInRange.get(0);     // get the [unlucky] first Actor
			// in the list...
			//System.out.printf("listInRange.size() is %d,\n", listInRange.size());
			targetX = targetCompetitor.getX();                      // Get its x coordinate
			targetY = targetCompetitor.getY();                      // Get its y coordinate
			//legalTurnTowards(targetX, targetY);                     // Got its location, now go after 'em!

			int myX = getX();
			int myY = getY();


			if (isTouching(Competitor.class) == true) {
				// get away for awhile
				//int variationX = Greenfoot.getRandomNumber(90)-45;
				//int variationY = Greenfoot.getRandomNumber(90)-45;
				//legalTurnTowards(targetX+300, targetY+300);
				//legalTurnTowards((getRotation() + 360) - 180);
//				setRotation((getRotation() + 360) - 180);
				maneuver.turn(180);
			}
			else {

				int variationX = Greenfoot.getRandomNumber(30) - 15;
				int variationY = Greenfoot.getRandomNumber(30) - 15;

				maneuver.turnTo(targetX + variationX, targetY + variationY);


			}
			newDistance = Greenfoot.getRandomNumber(6);             // ...a  random distance, hopefully...            if (energyLevel > ENERGY_NORMAL_RESERVE)
			maneuver.move(newDistance);                             // If we have enough energy, legalMove()
			// will allow it.  Otherwise we have


		}
		else {

			// We're here because there weren't any Competitors or Nuggets within SEEK_RANGE of us,
			// so we're going to hunt around for one.
			// Need to handle when we're at the edge of the world, though...
			worldHeight = getWorld().getHeight() - 1;
			worldWidth = getWorld().getWidth() - 1;

			// where are we now?
			int xNow = getX();
			int yNow = getY();

			// if we're at one of the edges, turn towards the middle of the world...
			if ((xNow <= EDGE_MARGIN) || (yNow <= EDGE_MARGIN) || (xNow >= worldWidth - EDGE_MARGIN) || (yNow >= worldHeight - EDGE_MARGIN)) {
				// turn if we have energy
				maneuver.turnTo((worldWidth / 2), (worldHeight / 2));
				//turnTowards((worldWidth/2), (worldHeight/2));

			}
			else if (turnCountDown <= 0) {
				// we're not at the edge, and the turning countdown has expired, so turn if we need to
				newNum = Greenfoot.getRandomNumber(31) - 15;
				maneuver.turn(newNum);
				//turn(newNum);
				turnCountDown = Greenfoot.getRandomNumber(10);
			}

			// we've figured out which direction to turn, now figure out how much to move..
			if (getEnergyLevel() > ENERGY_NORMAL_RESERVE)
				maneuver.move(moveDistance);
		}

		return maneuver;
	}


}

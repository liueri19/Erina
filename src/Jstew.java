import erina.core.Competitor;
import erina.core.Erina;
import erina.core.Maneuver;
import erina.core.Nugget;
import greenfoot.Greenfoot;
//import greenfoot.GreenfootSound;

import java.util.List;

/**
 * This competitor does a rest and attack strategy, it waits until it has 3000 energy
 * then it attacks with a vengence. I'm going to try and implement another strategy that will
 * attack the weak even if we don't have a crap ton of energy..
 *
 * @author (John Stewart)
 * @version (April 21, 2017)
 * @assignment: competitor checkpoint
 */
public class Jstew extends Competitor {


	private static final int NORMAL_MOVE_DISTANCE = 2;
	private static final int ATTACK_DISTANCE = 5;
	private static final int ENERGY_NORMAL_RESERVE = 3000;
	private static final int SEEK_RANGE = 450;
	private static final int EDGE_MARGIN = 10;
	int cycleCount = 0;
	int betterMove = 19;
	int turnCountDown;
	private int moveDistance = NORMAL_MOVE_DISTANCE;


	public Jstew(Erina world, String thisName) {
		super(world, thisName);


//		stringSoundHorror = "suhdude.mp3";
//		stringSoundSadistic = "hypnosis.mp3";
//		stringSoundKill = "ethantrap.mp3";
//		stringSoundGotKilled = "bestgreat1.wav";
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

		setHorrorSound("sounds/suhdude.mp3");
		setSadisticSound("sounds/hypnosis.mp3");
		setKillSound("sounds/ethantrap.mp3");
		setDeathSound("sounds/bestgreat1.wav");

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

		List<Competitor> listInRange;   // a list of all the Actor objects within range
		listInRange = getObjectsInRange(SEEK_RANGE, Competitor.class);  // Get a list of all the Actor objects within range

		turnCountDown--;

		//The following just keeps the actor moving towards the world while saving energy, also avoiding the moving penalty
//		cycleCount = getCycles();
		cycleCount = getStats().getCyclesSurvived();
		if (cycleCount == betterMove) {
			worldHeight = getWorld().getHeight() - 1;
			worldWidth = getWorld().getWidth() - 1;

			// where are we now?
			int xNow = getX();
			int yNow = getY();

			if ((xNow <= EDGE_MARGIN) || (yNow <= EDGE_MARGIN) || (xNow >= worldWidth - EDGE_MARGIN) || (yNow >= worldHeight - EDGE_MARGIN)) {
				// turn if we have energy
				maneuver.turnTowards((worldWidth / 2), (worldHeight / 2));
				//turnTowards((worldWidth/2), (worldHeight/2));

			}
			else if (turnCountDown <= 0) {
				// we're not at the edge, and the turning countdown has expired, so turn if we need to
				newNum = Greenfoot.getRandomNumber(31) - 15;
				maneuver.turn(newNum);
				//turn(newNum);
				turnCountDown = Greenfoot.getRandomNumber(10);
			}
			betterMove += 20;
			maneuver.move(1);
			return maneuver;
		}

		Nugget targetNugget;
		List listInRange2;
		listInRange2 = getObjectsInRange(1000, Nugget.class);


		if (listInRange2.size() > 0) {
			targetNugget = (Nugget) listInRange2.get(0);
			targetX = targetNugget.getX();
			targetY = targetNugget.getY();

			return maneuver.turnTowards(targetX, targetY)
					.move(16);

		}


		if (isTouching(Competitor.class) == true) {
			targetCompetitor = (Competitor) listInRange.get(0);
			targetX = targetCompetitor.getX();
			targetY = targetCompetitor.getY();
//			legalTurnTowards(targetX + 180, targetY + 180);
//			legalMove(3);      // don't even attempt to move or turn unless we have lots of energy (3000)
//			return;

			return maneuver.turnTowards(targetX + 180, targetY + 180)
					.move(3);
		}


		if (listInRange.size() > 0) {
			targetCompetitor = (Competitor) listInRange.get(0);     // get the [unlucky] first Actor
			// in the list...
			//System.out.printf("listInRange.size() is %d,\n", listInRange.size());
			targetX = targetCompetitor.getX();                      // Get its x coordinate
			targetY = targetCompetitor.getY();                      // Get its y coordinate
			//legalTurnTowards(targetX, targetY);                     // Got its location, now go after 'em!
			int variationX = Greenfoot.getRandomNumber(30) - 15;
			int variationY = Greenfoot.getRandomNumber(30) - 15;

			maneuver.turnTowards(targetX + variationX, targetY + variationY);


			if (getEnergyLevel() > ENERGY_NORMAL_RESERVE)
				maneuver.move(ATTACK_DISTANCE);
			return maneuver;


		}
		else {

			// We're here because there weren't any Competitors within SEEK_RANGE of us,
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
				maneuver.turnTowards((worldWidth / 2), (worldHeight / 2));
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




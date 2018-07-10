import erina.core.Competitor;
import erina.core.Erina;
import erina.core.Maneuver;
import greenfoot.Greenfoot;

import java.util.List;

/**
 * Write a description of class EvanSchimberg here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class EvanSchimberg extends Competitor {

	private static final int NORMAL_MOVE_DISTANCE = 3;
	private static final int ENERGY_NORMAL_RESERVE = 50;// Used in logic for energy management
	private static final int SEEK_RANGE = 500;              // Used in logic to learn who is out there...
	int turnCountDown;                                      // Don't want to turn every act().  Space it out.
	int stalkerCheck = 0;
	int loiteringTax;
	private int moveDistance = NORMAL_MOVE_DISTANCE;        // Default number of cells to move each time


	public EvanSchimberg(Erina world, String thisName) {
		super(world, thisName);


		// if you want a sound for getting hit or making a hit, you need to
		// assign two string variables to the name of the .wav or .mp3 file
		// stringSoundHorror and stringSoundSadistic.
		// Then the rest of the code below should work.

//		stringSoundHorror = "OhHiMark.mp3";
//		stringSoundSadistic = "OhHiMark.mp3";
//		stringSoundKill = "OhHiMark.mp3";
//		stringSoundGotKilled = "FedUpWithThisWorld.mp3";
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

		setHorrorSound("sounds/OhHiMark.mp3");
		setSadisticSound("sounds/OhHiMark.mp3");
		setKillSound("sounds/OhHiMark.mp3");
		setDeathSound("sounds/FedUpWithThisWorld.mp3");


	}


	//runs away if the stalkerCheck is >= 50
	//WIP
	public void dash(Maneuver maneuver)

	{
		//dashes if we have the energy to do so other wise builds up energy
		if (getEnergyLevel() > 150) {
			maneuver.turnTowards(getWorld().getHeight(), getWorld().getWidth())
					.move(50);
		}
		else {
			maneuver.move(0);
			loiteringTax++;
		}

		if (loiteringTax >= 49) {
			maneuver.move(1);
			loiteringTax = 0;
		}
		//resets the check if the escape was successful
		if (isTouching(Competitor.class) == false) {
			stalkerCheck = 0;
		}
		else {
			stalkerCheck++;
		}
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
		int cycleCount = 0;
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

		// If there's another Actor out there within range, turn towards it
		// and start moving in that direction.
		// If not, move randomly (seek).
        
        
        /*if(cycleCount == 49)
        {
            legalTurnTowards(0 , 0);
            legalMove(1);
            cycleCount = 0;
            return;
        }
        */

		//if(cycleCount < 49 && getEnergyLevel() >= 1000)
		//{


		final int energyLevel = getEnergyLevel();

		if (isTouching(Competitor.class) == false) {
			stalkerCheck = 0;
		}

		if (energyLevel < 190)
			return maneuver;      // don't even attempt to move or turn unless we have lots of energy

		if (listInRange.size() > 0) {
			targetCompetitor = (Competitor) listInRange.get(0);     // get the [unlucky] first Actor
			// in the list...
			if (targetCompetitor.getEnergyLevel() >= getEnergyLevel() + 10 || getEnergyLevel() >= 200) {

				targetX = targetCompetitor.getX();                      // Get its x coordinate
				targetY = targetCompetitor.getY();                      // Get its y coordinate
				//legalTurnTowards(targetX, targetY);                     // Got its location, now go after 'em!
				int variationX = Greenfoot.getRandomNumber(30) - 15;
				int variationY = Greenfoot.getRandomNumber(30) - 15;

				maneuver.turnTowards(targetX + variationX, targetY + variationY);

				newDistance = Greenfoot.getRandomNumber(4);             // ...a  random distance, hopefully...

				if (energyLevel > ENERGY_NORMAL_RESERVE) {
					maneuver.move(newDistance);         // If we have enough energy, legalMove()
				}

				//deals with competitors sitting on top of my competitor
				if (stalkerCheck >= 50) {
					dash(maneuver);
				}


				//if it is too close to a wall it moves away so it does not get trapped in a corner
				//WIP
				if (this.getX() <= 75 && this.getY() <= 75)

				{
					maneuver.turnTowards(getWorld().getHeight(), getWorld().getWidth());

					if (energyLevel > 100) {
						maneuver.move(10);         // If we have enough energy, legalMove()
					}


				}
				else {


					//moves away after a hit in order to set it back up to get another hit
					if (isTouching(Competitor.class) == true) {
						maneuver.turnTowards(-targetX, -targetY)
								.move(4);
						stalkerCheck++;

						return maneuver;
					}
				}
			}
                    /*
                    else 
                    {
                        
                        if (isAtEdge() == true)     
                        {
                             return;  
                        }   
                                                                          // will allow it.  Otherwise we have 
                        targetX = targetCompetitor.getX();                      // Get its x coordinate
                        targetY = targetCompetitor.getY();                      // Get its y coordinate
                        targetX = -targetX;
                        targetY = -targetY;
                        //legalTurnTowards(targetX, targetY);                     // Got its location, now go after 'em!
                        int variationX = Greenfoot.getRandomNumber(30)-15;
                        int variationY = Greenfoot.getRandomNumber(30)-15;

                        legalTurnTowards(targetX+variationX, targetY+variationY);
            
                        newDistance = Greenfoot.getRandomNumber(1);             // ...a  random distance, hopefully...
                        if (energyLevel > ENERGY_NORMAL_RESERVE)
                            legalMove(newDistance);                 
                
                
                
                    } 
                    */
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


			// we've figured out which direction to turn, now figure out how much to move..
			if (energyLevel > ENERGY_NORMAL_RESERVE)
				maneuver.move(moveDistance);
		}
		// }
		//cycleCount = cycleCount + 1;

		return maneuver;
	}
}



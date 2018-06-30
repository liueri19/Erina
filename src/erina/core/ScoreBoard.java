package erina.core;

import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;

import java.util.List;

/**
 * Class ScoreBoard
 *
 * @author Brian Arnold
 * @version April 2017
 * <p>
 * This class is a modification of the Greenfoot ScoreBoard class.
 */


/**
 * An actor class that can display a scoreboard, using Greenfoot's
 * UserInfo class.
 * <p>
 * You typically use this by including some code into the world for when your game ends:
 *
 * <pre>
 *   addObject(new ScoreBoard(800, 600), getWidth() / 2, getHeight() / 2);
 * </pre>
 * <p>
 * Where 800 by 600 should be replaced by the desired size of the score board.
 *
 * @author Neil Brown
 * @version 1.0
 * <p>
 * Modified 5/2016 by Brian Arnold for ArnoldArena project
 * Modified 2018-06 by Eric for Erina project
 */
class ScoreBoard extends Actor {
	// The vertical gap between user images in the scoreboard:
	private static final int GAP = 10;
	// The height of the "All Players"/"Near Me" text at the top:
	private static final int HEADER_TEXT_HEIGHT = 20;
	// The main text color:
	private static final Color MAIN_COLOR = new Color(0x60, 0x60, 0x60); // dark grey
	// The score color:
	private static final Color SCORE_COLOR = new Color(0x40, 0x60, 0xA0); // blue-y
	// The background colors:
	private static final Color BACKGROUND_COLOR = new Color(0xFF, 0xFF, 0xFF, 0xE0);
	private static final Color BACKGROUND_HIGHLIGHT_COLOR = new Color(180, 230, 255, 0xB0);

	private static final int PLAYER_TEXT_HEIGHT = 24;

	/**
	 * Constructor for objects of class ScoreBoard.
	 * <p>
	 * You can specify the width and height that the score board should be, but
	 * a minimum width of 600 will be enforced.
	 */
	public ScoreBoard(int width, int height, List<Competitor> myRankedList) {
		setImage(new GreenfootImage(Math.max(600, width), height));

		drawScores(myRankedList);
	}

	private void drawString(String text, int x, int y, Color color, int height) {
		getImage().drawImage(new GreenfootImage(text, height, color, new Color(0, 0, 0, 0)), x, y);
	}

	private void drawScores(List<Competitor> finalList) {
		// 50 pixels is the max height of the user image
		final int pixelsPerUser = 50 + 2 * GAP;
		// Calculate how many users we have room for:
		final int numUsers = ((getImage().getHeight() - (HEADER_TEXT_HEIGHT + 10)) / pixelsPerUser);
		final int topSpace = getImage().getHeight() - (numUsers * pixelsPerUser) - GAP;

		getImage().setColor(BACKGROUND_COLOR);
		getImage().fill();

		int x = 70;
		drawString("Competitor", x, topSpace - HEADER_TEXT_HEIGHT - 5, MAIN_COLOR, HEADER_TEXT_HEIGHT);
		x += 150;
		drawString("Energy", x, topSpace - HEADER_TEXT_HEIGHT - 5, MAIN_COLOR, HEADER_TEXT_HEIGHT);
		x += 120;
		drawString("Kills", x, topSpace - HEADER_TEXT_HEIGHT - 5, MAIN_COLOR, HEADER_TEXT_HEIGHT);
		x += 75;
		drawString("Hits", x, topSpace - HEADER_TEXT_HEIGHT - 5, MAIN_COLOR, HEADER_TEXT_HEIGHT);
//		x += 65;
//		drawString("Hits Taken", x, topSpace - HEADER_TEXT_HEIGHT - 5, MAIN_COLOR, HEADER_TEXT_HEIGHT);
		// TODO add damage info?
		x += 110;
		drawString("Distance", x, topSpace - HEADER_TEXT_HEIGHT - 5, MAIN_COLOR, HEADER_TEXT_HEIGHT);
		x += 100;
		drawString("Cycles", x, topSpace - HEADER_TEXT_HEIGHT - 5, MAIN_COLOR, HEADER_TEXT_HEIGHT);
		x += 85;
		drawString("Nugs:Val", x, topSpace - HEADER_TEXT_HEIGHT - 5, MAIN_COLOR, HEADER_TEXT_HEIGHT);
		x += 140;
		drawString("Score", x, topSpace - HEADER_TEXT_HEIGHT - 5, MAIN_COLOR, HEADER_TEXT_HEIGHT);

		drawUsers(GAP, topSpace, (getImage().getWidth()) - GAP, topSpace + numUsers * pixelsPerUser, finalList);
		//drawString("Near You", 100 + getImage().getWidth() / 2, topSpace - HEADER_TEXT_HEIGHT - 5, MAIN_COLOR, HEADER_TEXT_HEIGHT);

		//drawUserPanel(GAP, topSpace, (getImage().getWidth() / 2) - GAP, topSpace + numUsers * pixelsPerUser, UserInfo.getTop(numUsers));
		//drawUserPanel(GAP + getImage().getWidth() / 2, topSpace, getImage().getWidth() - GAP, topSpace + numUsers * pixelsPerUser, UserInfo.getNearby(numUsers));
	}


	private void drawUsers(int left, int top, int right, int bottom, List<Competitor> competitors) {
		int rank = 1;
		getImage().setColor(MAIN_COLOR);
		getImage().drawRect(left, top, right - left, bottom - top);

		if (competitors == null)
			return;


		int y = top + GAP;
		//for (Competitor oneComp : competitors)
		for (int i = 0; i < competitors.size(); i++) {
			Competitor player = competitors.get(i);
			CompetitorStats stats = player.getStats();
			Color c;
			c = BACKGROUND_COLOR;

			getImage().setColor(c);
			getImage().fillRect(left + 5, y - GAP + 1, right - left - 10, 50 + 2 * GAP - 1);


			int x = left + 10;


			drawString("#" + rank, x, y + 18, MAIN_COLOR, PLAYER_TEXT_HEIGHT);
			x += 50;
			drawString(player.getName(), x, y + 18, SCORE_COLOR, PLAYER_TEXT_HEIGHT);
			x += 160;
			drawString("" + player.getEnergyLevel(), x, y + 18, SCORE_COLOR, PLAYER_TEXT_HEIGHT);
			x += 120;
			drawString("" + stats.getKills(), x, y + 18, SCORE_COLOR, PLAYER_TEXT_HEIGHT);
			x += 75;
			drawString("" + stats.getHitsInflicted(), x, y + 18, SCORE_COLOR, PLAYER_TEXT_HEIGHT);
//			x += 75;
//			drawString("" + stats.getHitsAbsorbed(), x, y + 18, SCORE_COLOR, PLAYER_TEXT_HEIGHT);
			// TODO add damage info?
			x += 95;
			drawString("" + stats.getTotalDistance(), x, y + 18, SCORE_COLOR, PLAYER_TEXT_HEIGHT);
			x += 100;
			drawString("" + stats.getCyclesSurvived(), x, y + 18, SCORE_COLOR, PLAYER_TEXT_HEIGHT);
			x += 85;
			drawString("" + stats.getNuggetsCount(), x, y + 18, SCORE_COLOR, PLAYER_TEXT_HEIGHT);
			x += 40;
			drawString(":" + stats.getNuggetsValue(), x, y + 18, SCORE_COLOR, PLAYER_TEXT_HEIGHT);
			x += 80;
			drawString("" + stats.getScore(), x, y + 18, SCORE_COLOR, PLAYER_TEXT_HEIGHT);


			y += 50 + 2 * GAP;
			rank++;
		}
	}

}

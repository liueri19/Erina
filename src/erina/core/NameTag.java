package erina.core;

import greenfoot.Actor;
import greenfoot.Color;
import greenfoot.GreenfootImage;

/**
 * The NameTag is a piece of text as a label next to a Competitor.
 *
 * @version 1.0
 * @author Eric
 */
final class NameTag extends Actor {
	NameTag(String text) {
		final GreenfootImage nameTag =
				new GreenfootImage(text, 10, Color.BLACK, Color.WHITE);
		setImage(nameTag);
	}
}

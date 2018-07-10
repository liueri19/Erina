import erina.core.Competitor;
import greenfoot.Color;
import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.Arrays;
import java.util.List;

import static erina.core.Erina.WORLD_HEIGHT;
import static erina.core.Erina.WORLD_WIDTH;

/**
 * This class is for circumventing Greenfoot's restriction on packages. The actual Erina
 * class is {@link erina.core.Erina}.
 * This class only serves as a display.
 *
 * @see greenfoot.World
 * @see erina.core.Erina
 *
 * @version 1.1
 * @author Eric
 */
public final class Erina extends World {

	private final erina.core.Erina ERINA;

	public Erina() {
		super(WORLD_WIDTH, WORLD_HEIGHT, 1);
		ERINA = new ConcreteErina(this);
	}


	// delegate methods to ConcreteErina
	@Override
	public void act() { ERINA.act(); }
	@Override
	public int numberOfObjects() { return ERINA.numberOfObjects(); }
	@Override
	public GreenfootImage getBackground() { return ERINA.getBackground(); }
	@Override
	public Color getColorAt(int x, int y) { return ERINA.getColorAt(x, y); }
	@Override
	public int getCellSize() { return ERINA.getCellSize(); }
	@Override
	public void setPaintOrder(Class... classes) { ERINA.setPaintOrder(classes); }
	@Override
	public void setActOrder(Class... classes) { ERINA.setActOrder(classes); }


	private static final class ConcreteErina extends erina.core.Erina {

		private ConcreteErina(World display) { super(display); }

		@Override
		protected List<Competitor> prepareCompetitors() {

			// add and remove competitors here

			return Arrays.asList(
					new TestCompetitor2(this, "TC_2"),
					new TestCompetitor3(this, "TC_3"),
					new TestCompetitor4(this, "TC_4"),
					new TestCompetitor5(this, "TC_5"),
					new EvanSchimberg(this, "Evan"),
					new Jstew(this, "Jstew")
			);
		}
	}
}

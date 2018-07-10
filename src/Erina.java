import erina.core.Competitor;

import java.util.Arrays;
import java.util.List;

/**
 * This class is for circumventing Greenfoot's restriction on packages. The actual Erina
 * class is {@link erina.core.Erina}.
 *
 * @see greenfoot.World
 * @see erina.core.Erina
 *
 * @version 1.1
 * @author Eric
 */
public final class Erina extends erina.core.Erina {
	@Override
	protected List<Competitor> prepareCompetitors() {
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

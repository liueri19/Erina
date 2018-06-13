package erina;

/**
 * A class that holds some statistics about a Competitor.
 *
 * @version alpha
 * @author Eric
 */
public final class CompetitorStats {

	private volatile Competitor lastAttacker, lastVictim;
	private volatile int kills, totalDistance, cycles, score, nuggetsCount, nuggetsValue,
			hitsInflicted, hitsAbsorbed;


	/**
	 * Creates a new CompetitorStats with all values null or 0.
	 */
	CompetitorStats() {
		this(null, null,
				0,
				0,
				0,
				0,
				0, 0,
				0, 0);
	}

	/**
	 * Creates a new CompetitorStats with the specified values.
	 * It is recommended to NOT use this constructor as the long argument list is error
	 * prone.
	 */
	@Deprecated
	CompetitorStats(Competitor lastAttacker, Competitor lastVictim,
					int kills,
					int totalDistance,
					int cycles,
					int score,
					int nuggetsCount, int nuggetsValue,
					int hitsInflicted, int hitsAbsorbed) {
		this.lastAttacker = lastAttacker;
		this.lastVictim = lastVictim;
		this.kills = kills;
		this.totalDistance = totalDistance;
		this.cycles = cycles;
		this.score = score;
		this.nuggetsCount = nuggetsCount;
		this.nuggetsValue = nuggetsValue;
		this.hitsInflicted = hitsInflicted;
		this.hitsAbsorbed = hitsAbsorbed;
	}


	/**
	 * Creates a CompetitorStats by copying values from the specified object.
	 */
	CompetitorStats(CompetitorStats stats) {
		lastAttacker = stats.getLastAttacker();
		lastVictim = stats.getLastVictim();
		kills = stats.getKills();
		totalDistance = stats.getTotalDistance();
		cycles = stats.getCyclesSurvived();
		score = stats.getScore();
		nuggetsCount = stats.getNuggetsCount();
		nuggetsValue = stats.getNuggetsValue();
		hitsInflicted = stats.getHitsInflicted();
		hitsAbsorbed = stats.getHitsAbsorbed();
	}



	/**
	 * Returns the last Competitor that inflicted damage on this Competitor.
	 */
	public Competitor getLastAttacker() { return lastAttacker; }

	/**
	 * Returns the last Competitor that this Competitor inflicted damage on.
	 */
	public Competitor getLastVictim() { return lastVictim; }

	/**
	 * Returns the number of hits this
	 */
	public int getHitsInflicted() { return hitsInflicted; }

	public int getHitsAbsorbed() { return hitsAbsorbed; }

	/**
	 * Returns the number of kills this Competitor achieved.
	 */
	public int getKills() { return kills; }

	/**
	 * Returns the total distance this Competitor has traveled.
	 */
	public int getTotalDistance() { return totalDistance; }

	/**
	 * Returns the number of cycles this Competitor has survived.
	 */
	public int getCyclesSurvived() { return cycles; }

	/**
	 * Returns the current score of this Competitor.
	 */
	public int getScore() { return score; }

	/**
	 * Returns the number of Nuggets this Competitor has consumed.
	 */
	public int getNuggetsCount() { return nuggetsCount; }

	/**
	 * Returns the total value of the Nuggets this Competitor has consumed.
	 */
	public int getNuggetsValue() { return nuggetsValue; }


	void setLastAttacker(Competitor lastAttacker) {
		this.lastAttacker = lastAttacker;
	}

	void setLastVictim(Competitor lastVictim) {
		this.lastVictim = lastVictim;
	}

	void setKills(int kills) {
		this.kills = kills;
	}

	synchronized void incrementKills() { kills++; }

	void setTotalDistance(int totalDistance) {
		this.totalDistance = totalDistance;
	}

	synchronized void incrementTotalDistanceBy(int increment) {
		totalDistance += increment;
	}

	void setCycles(int cycles) {
		this.cycles = cycles;
	}

	synchronized void incrementCycles() { cycles++; }

	void setScore(int score) {
		this.score = score;
	}

	synchronized void incrementScoreBy(int amount) { score += amount; }


	/**
	 * Updates stats for the consumption of the specified Nugget.
	 */
	synchronized void incrementNuggets(Nugget nugget) {
		incrementNuggetsCount();
		incrementNuggetsValueBy(nugget.getNuggetValue());
	}


	void setNuggetsCount(int nuggetsCount) {
		this.nuggetsCount = nuggetsCount;
	}

	synchronized void incrementNuggetsCount() { nuggetsCount++; }

	void setNuggetsValue(int nuggetsValue) {
		this.nuggetsValue = nuggetsValue;
	}

	synchronized void incrementNuggetsValueBy(int amount) {
		nuggetsValue += amount;
	}

	void setHitsInflicted(int hitsInflicted) {
		this.hitsInflicted = hitsInflicted;
	}

	synchronized void incrementHitsInflicted() { hitsInflicted++; }

	void setHitsAbsorbed(int hitsAbsorbed) {
		this.hitsAbsorbed = hitsAbsorbed;
	}

	synchronized void incrementHitsAbsorbed() { hitsAbsorbed++; }
}

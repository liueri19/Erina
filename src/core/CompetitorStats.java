package core;

/**
 * A class that holds some statistics about a Competitor.
 * This class is immutable.
 *
 * @version alpha
 * @author Eric
 */
public final class CompetitorStats {

	private final Competitor lastAttacker, lastVictim;
	private final int kills, totalDistance, cycles, score, nuggetsCount, nuggetsValue,
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
	 * It is recommended to NOT use this constructor and use {@link Builder} instead.
	 */
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
	 * Builder for creating CompetitorStats with values.
	 */
	static final class Builder {
		private Competitor lastAttacker, lastVictim;
		private int kills, totalDistance, cycles, score, nuggetsCount, nuggetsValue,
				hitsInflicted, hitsAbsorbed;

		/**
		 * Creates a Builder initialized to the stats from the specified CompetitorStats.
		 */
		Builder(CompetitorStats stats) {
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

		void setLastAttacker(Competitor lastAttacker) {
			this.lastAttacker = lastAttacker;
		}

		void setLastVictim(Competitor lastVictim) {
			this.lastVictim = lastVictim;
		}

		void setKills(int kills) {
			this.kills = kills;
		}

		void setTotalDistance(int totalDistance) {
			this.totalDistance = totalDistance;
		}

		void setCycles(int cycles) {
			this.cycles = cycles;
		}

		void setScore(int score) {
			this.score = score;
		}

		void setNuggetsCount(int nuggetsCount) {
			this.nuggetsCount = nuggetsCount;
		}

		void setNuggetsValue(int nuggetsValue) {
			this.nuggetsValue = nuggetsValue;
		}

		void setHitsInflicted(int hitsInflicted) {
			this.hitsInflicted = hitsInflicted;
		}

		void setHitsAbsorbed(int hitsAbsorbed) {
			this.hitsAbsorbed = hitsAbsorbed;
		}


		/**
		 * Builds a CompetitorStats with the values in this Builder.
		 */
		CompetitorStats build() {
			return new CompetitorStats(lastAttacker, lastVictim,
					kills,
					totalDistance,
					cycles,
					score,
					nuggetsCount, nuggetsValue,
					hitsInflicted, hitsAbsorbed);
		}
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
}

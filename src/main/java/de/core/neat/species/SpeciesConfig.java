package de.core.neat.species;

/**
 * @author muellermak
 *
 */
public class SpeciesConfig {

	/**
	 * How many percent of members of species are removed in each generation
	 */
	public double removeBadSpeciesMembersPercent;

	/**
	 * Rate that a genome is added to the next generation without crossover
	 */
	public double addGenomeWithoutCrossoverRate;

	/**
	 * 
	 */
	public SpeciesConfig() {
		this.removeBadSpeciesMembersPercent = 0.85;
		this.addGenomeWithoutCrossoverRate = 0.25;
	}

}

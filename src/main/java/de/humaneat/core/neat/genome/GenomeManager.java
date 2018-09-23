package de.humaneat.core.neat.genome;

import de.humaneat.core.global.genome.DefaultGenomeManager;

/**
 * @author MannoR
 *
 */
public class GenomeManager extends DefaultGenomeManager {

	public Genome genome;

	/**
	 * @param genome
	 */
	public GenomeManager(Genome genome) {
		super(genome);
	}

}

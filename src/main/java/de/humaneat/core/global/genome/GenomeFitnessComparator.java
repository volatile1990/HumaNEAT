package de.humaneat.core.global.genome;

import java.util.Comparator;

import de.humaneat.core.neat.ArtificialIntelligence;

/**
 * @author MannoR
 *
 */
public class GenomeFitnessComparator implements Comparator<ArtificialIntelligence> {

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ArtificialIntelligence first, ArtificialIntelligence second) {

		if (first.brain.fitness > second.brain.fitness) {
			return -1;
		} else if (first.brain.fitness < second.brain.fitness) {
			return 1;
		}
		return 0;
	}

}

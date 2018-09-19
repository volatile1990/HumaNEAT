package de.core.genome;

import java.util.Comparator;

import de.core.ArtificialIntelligence;

public class GenomeFitnessComparator implements Comparator<ArtificialIntelligence> {

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

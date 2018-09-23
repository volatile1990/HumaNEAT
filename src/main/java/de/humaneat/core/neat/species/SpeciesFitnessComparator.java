package de.humaneat.core.neat.species;

import java.util.Comparator;

/**
 * @author muellermak
 *
 */
public class SpeciesFitnessComparator implements Comparator<Species> {

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Species first, Species second) {

		if (first.bestFitness > second.bestFitness) {
			return -1;
		} else if (first.bestFitness < second.bestFitness) {
			return 1;
		}
		return 0;
	}

}

package de.humaneat.core.lstm.species;

import java.util.Comparator;

/**
 * @author muellermak
 *
 */
public class LstmSpeciesFitnessComparator implements Comparator<LstmSpecies> {

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(LstmSpecies first, LstmSpecies second) {

		if (first.bestFitness > second.bestFitness) {
			return -1;
		} else if (first.bestFitness < second.bestFitness) {
			return 1;
		}
		return 0;
	}

}

package de.humaneat.core.lstm.genome;

import java.util.Comparator;

import de.humaneat.core.lstm.ArtificialLstmIntelligence;

/**
 * @author MannoR
 *
 */
public class LstmGenomeFitnessComparator implements Comparator<ArtificialLstmIntelligence> {

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ArtificialLstmIntelligence first, ArtificialLstmIntelligence second) {

		if (first.brain.fitness > second.brain.fitness) {
			return -1;
		} else if (first.brain.fitness < second.brain.fitness) {
			return 1;
		}
		return 0;
	}

}

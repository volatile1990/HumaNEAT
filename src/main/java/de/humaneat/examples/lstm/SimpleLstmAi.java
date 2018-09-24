package de.humaneat.examples.lstm;

import de.humaneat.core.lstm.ArtificialLstmIntelligence;
import de.humaneat.core.lstm.genome.LstmGenome;

public class SimpleLstmAi extends ArtificialLstmIntelligence {

	/**
	 * @param anzInputs
	 * @param anzOutputs
	 */
	public SimpleLstmAi(int anzInputs, int anzOutputs) {
		super(anzInputs, anzOutputs);
	}

	/**
	 * @param genome
	 */
	public SimpleLstmAi(LstmGenome genome) {
		super(genome);
	}

	/**
	 * @return
	 */
	@Override
	public double calculateFitness() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param genome
	 * @return
	 */
	@Override
	public ArtificialLstmIntelligence getNewInstance(LstmGenome genome) {
		return new SimpleLstmAi(genome);
	}

}

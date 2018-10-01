package de.humaneat.examples.lstm;

import java.util.List;

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

	@Override
	public void doAiLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Double> getInputs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void think() {
		// TODO Auto-generated method stub

	}

	@Override
	public void takeAction(List<Double> output) {
		// TODO Auto-generated method stub

	}

}

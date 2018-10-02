package de.humaneat.examples.lstm;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.lstm.ArtificialLstmIntelligence;

public class SimpleLstmAi extends ArtificialLstmIntelligence {

	private int[] input = { 3, 6, 9, 12, 15, 18, 21, 24, 27 };

	private int currentInput = 0;

	/**
	 * @return
	 */
	@Override
	public double calculateFitness() {

		double unadjustedFitness = 1;
		if (brain.getPredictor().getNext()[0] == 30) {
			unadjustedFitness += 100;
		}

		return unadjustedFitness;
	}

	/**
	 * 
	 */
	@Override
	public void doAiLogic() {

		currentInput = 0;
		for (int i = 0; i < input.length; ++i) {
			think();

			++currentInput;
		}

	}

	/**
	 * @return
	 */
	@Override
	public List<Double> getInputs() {

		List<Double> in = new ArrayList<>();
		in.add((double) input[currentInput]);

		return in;
	}

	/**
	 * @param output
	 */
	@Override
	public void takeAction(List<Double> output) {
	}

}

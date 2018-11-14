package de.humaneat.examples.xor;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.neat.ArtificialIntelligence;

/**
 * @author MannoR
 *
 */
public class XorAI extends ArtificialIntelligence {

	private double XOR_INPUT[][] = { { 0, 0 }, { 0, 1 }, { 1, 1 }, { 1, 0 } };

	private int currentInput;

	/**
	 * 
	 */
	@Override
	public void doAiLogic() {

		// Do for all 4 XOR cases
		currentInput = 0;
		for (int j = 0; j < 4; ++j) {
			think();
		}
	}

	/**
	 * @return a list of all input datasets
	 */
	@Override
	public List<Double> getInputs() {

		List<Double> input = new ArrayList<>();
		input.add(XOR_INPUT[currentInput][0]);
		input.add(XOR_INPUT[currentInput][1]);

		++currentInput;

		return input;
	}

	/**
	 * @return the unadjusted fitness
	 */
	@Override
	public double calculateFitness() {

		double unadjustedFitness = 0;
		for (int i = 0; i < anzAccumulatedDatas; ++i) {

			double expectedOutput = Math.round(inputs.get(i)[0]) ^ Math.round(inputs.get(i)[1]);
			unadjustedFitness += 1 - (expectedOutput - outputs.get(i)[0]) * (expectedOutput - outputs.get(i)[0]);
		}

		return unadjustedFitness * unadjustedFitness;
	}

	/**
	 * @param output
	 */
	@Override
	public void takeAction(List<Double> output) {
		// Nothing to do for xor evaluation
	}

	@Override
	public void initializeNormalizer() {
		// No normalizer used here
	}

}

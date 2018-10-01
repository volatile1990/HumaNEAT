package de.humaneat.core.lstm;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.global.DefaultArtificialIntelligence;
import de.humaneat.core.lstm.genome.LstmGenome;

/**
 * @author muellermak
 *
 */
public abstract class ArtificialLstmIntelligence implements DefaultArtificialIntelligence {

	public int anzInputs;
	public int anzOutputs;

	public List<double[]> inputs;
	public List<double[]> outputs;

	public int anzAccumulatedDatas;

	public LstmGenome brain;

	/**
	 * @param anzInputs
	 * @param anzOutputs
	 */
	public ArtificialLstmIntelligence(int anzInputs, int anzOutputs) {

		anzAccumulatedDatas = 0;

		this.anzInputs = anzInputs;
		this.anzOutputs = anzOutputs;

		inputs = new ArrayList<>();
		outputs = new ArrayList<>();

		brain = new LstmGenome(anzInputs, anzOutputs);
	}

	/**
	 * @param brain
	 */
	public ArtificialLstmIntelligence(LstmGenome brain) {

		this(brain.anzInputs, brain.anzOutputs);
		this.brain = brain;
	}

	/**
	 * Converts the given inputs in the same order to be used for the neuronal net
	 * Takes them and feeds it through the genome to get a decision
	 */
	@Override
	public void think() {

		List<Double> inputs = getInputs();

		if (inputs.size() != anzInputs) {
			throw new RuntimeException("Invalid number of inputs");
		}

		// Get inputs and save them for fitness evaluation
		double[] input = new double[anzInputs];
		for (int i = 0; i < inputs.size(); ++i) {
			input[i] = inputs.get(i);
		}
		this.inputs.add(input);

		// Feed through the neural net and save decision
		double[] output = brain.getFeeder().feedForward(input);
		outputs.add(output);

		// Convert primitve array to list
		List<Double> out = new ArrayList<>();
		for (int i = 0; i < output.length; ++i) {
			out.add(output[i]);
		}

		takeAction(out);

		++anzAccumulatedDatas;
	}

	/**
	 * Calculates the current fitness of the AI
	 * The collected inputs and outputs can be used here
	 *
	 * @return the calculated unadjusted fitness
	 */
	@Override
	public abstract double calculateFitness();

	/**
	 * Automatically called after calculate fitness
	 */
	public void clearData() {
		inputs.clear();
		outputs.clear();
		anzAccumulatedDatas = 0;
	}

}

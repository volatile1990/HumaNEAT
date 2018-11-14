package de.humaneat.core.neat;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.global.DefaultArtificialIntelligence;
import de.humaneat.core.global.normalization.Normalizer;
import de.humaneat.core.neat.genome.Genome;

/**
 * @author MannoR
 * 
 */
public abstract class ArtificialIntelligence implements DefaultArtificialIntelligence {

	public int anzInputs;
	public int anzOutputs;

	public List<double[]> inputs;
	public List<double[]> outputs;

	public int anzAccumulatedDatas;

	public Normalizer normalizer;
	public Genome brain;

	/**
	 * @param anzInputs
	 * @param anzOutputs
	 */
	public ArtificialIntelligence() {

		anzAccumulatedDatas = 0;

		initializeNormalizer();

		// Use input count defined by normalizer if given
		anzInputs = normalizer != null ? normalizer.getInputCount() : (int) Property.INPUT_COUNT.getValue();
		anzOutputs = (int) Property.OUTPUT_COUNT.getValue();

		inputs = new ArrayList<>();
		outputs = new ArrayList<>();

		brain = new Genome(anzInputs, anzOutputs);
	}

	/**
	 * Converts the given inputs in the same order to be used for the neuronal net
	 * Takes them and feeds it through the genome to get a decision
	 * Does this for every given input dataset
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

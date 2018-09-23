package de.humaneat.examples.xor;

import de.humaneat.core.neat.ArtificialIntelligence;
import de.humaneat.core.neat.genome.Genome;

/**
 * @author MannoR
 *
 */
public class XorAI extends ArtificialIntelligence {

	/**
	 * @param anzInputs
	 * @param anzOutputs
	 */
	public XorAI(int anzInputs, int anzOutputs) {
		super(anzInputs, anzOutputs);
	}

	/**
	 * @param genome
	 */
	public XorAI(Genome genome) {
		super(genome);
	}

	/**
	 * @return
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
	 * @param genome
	 * @return
	 */
	@Override
	public ArtificialIntelligence getNewInstance(Genome genome) {
		return new XorAI(genome);
	}

}

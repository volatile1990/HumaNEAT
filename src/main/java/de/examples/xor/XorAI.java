package de.examples.xor;

import de.core.neat.ArtificialIntelligence;
import de.core.neat.genome.Genome;

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
		for (int i = 0; i < this.anzAccumulatedDatas; ++i) {

			double expectedOutput = Math.round(this.inputs.get(i)[0]) ^ Math.round(this.inputs.get(i)[1]);
			unadjustedFitness += 1 - (expectedOutput - this.outputs.get(i)[0]) * (expectedOutput - this.outputs.get(i)[0]);
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

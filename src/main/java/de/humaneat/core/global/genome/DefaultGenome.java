package de.humaneat.core.global.genome;

import de.humaneat.core.neat.genes.Counter;

/**
 * @author muellermak
 * 
 *         A Genome is representating a whole neural network
 */
public abstract class DefaultGenome {

	/**
	 * Innovation counters
	 */
	public Counter nodeInnovation;
	public Counter connectionInnovation;

	/**
	 * Amount of inputs and outputs this genome takes/produces
	 */
	public int anzInputs;
	public int anzOutputs;

	/**
	 * Fitness: unadjustedFitness / nodes.size()
	 * unadjustedFitness: The result calculated by 'calculateFitness()'
	 */
	public double fitness;
	public double unadjustedFitness;

}

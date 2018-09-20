package de.core.neat.genome;

/**
 * @author muellermak
 *
 *         The config for neat genome
 */
public class NeatGenomeConfig {

	/**
	 * Maximum range of a weight on random generation
	 */
	public double weightRandomRange;

	/**
	 * Chance that a connection of a child genome gets disabled if either of the parents connection is too
	 */
	public double disableConnectionOnChildRate;

	/**
	 * Chance that the weights of a genome mutate
	 */
	public double weightMutationRate;

	/**
	 * Chance that a weight is uniformly pertrubed instead of assigning a new one
	 */
	public double probabilityPerturbing;

	/**
	 * Maxium weight change of uniformly perturbing it
	 */
	public double uniformlyPertrubWeightRange;

	/**
	 * Chance that a connection is added during mutation
	 */
	public double addConnectionRate;

	/**
	 * Chance that a node is added during mutation
	 */
	public double addNodeRate;

	/**
	 * 
	 */
	public NeatGenomeConfig() {

		this.weightMutationRate = 0.7;
		this.weightRandomRange = 2;
		this.probabilityPerturbing = 0.9;
		this.uniformlyPertrubWeightRange = 0.2;
		this.addConnectionRate = 0.3;
		this.addNodeRate = 0.001;
		this.disableConnectionOnChildRate = 0.75;
	}
}

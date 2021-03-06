package de.core;

import java.util.List;

import de.core.genome.Genome;

/**
 * @author MannoR
 *
 */
public abstract class ArtificialIntelligence {

	public Genome brain;

	/**
	 * Converts the given inputs in the same order to be used for the neuronal net
	 *
	 * @param inputs
	 */
	public abstract void setInputs(List<Float> inputs);

	/**
	 * Takes its inputs and feeds it through the genome to get a decision
	 */
	public abstract void think();

	/**
	 * Calculates the current fitness of the AI
	 *
	 * @return the fitness
	 */
	public abstract float calculateFitness();

	/**
	 * Returns a copy of itself
	 *
	 * @return
	 */
	public abstract ArtificialIntelligence getNewInstance(Genome genome);
}

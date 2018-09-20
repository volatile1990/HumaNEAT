package de.core.lstm;

import java.util.List;

import de.core.lstm.genome.LSTMGenome;
import de.core.neat.genome.NeatGenome;

/**
 * @author MannoR
 *
 */
public abstract class ArtificialIntelligence {

	public LSTMGenome brain;

	/**
	 * Converts the given inputs in the same order to be used for the neuronal net
	 *
	 * @param inputs
	 */
	public abstract void setInputs(List<Double> inputs);

	/**
	 * Takes its inputs and feeds it through the genome to get a decision
	 */
	public abstract void think();

	/**
	 * Calculates the current fitness of the AI
	 *
	 * @return the fitness
	 */
	public abstract double calculateFitness();

	/**
	 * Returns a copy of itself
	 *
	 * @return
	 */
	public abstract ArtificialIntelligence getNewInstance(NeatGenome genome);
}

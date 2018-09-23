package de.humaneat.core.global;

import java.util.List;

import de.humaneat.core.lstm.genome.LSTMGenome;
import de.humaneat.core.neat.genome.Genome;

/**
 * @author MannoR
 *
 */
public abstract class DefaultArtificialIntelligence {

	public LSTMGenome brain;

	/**
	 * Takes its inputs and feeds it through the genome to get a decision
	 */
	public abstract void think(List<Double> inputs);

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
	public abstract DefaultArtificialIntelligence getNewInstance(Genome genome);
}

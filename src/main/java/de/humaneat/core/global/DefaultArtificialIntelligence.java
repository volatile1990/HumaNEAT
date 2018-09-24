package de.humaneat.core.global;

import java.util.List;

/**
 * @author MannoR
 *
 */
public interface DefaultArtificialIntelligence {

	/**
	 * Takes its inputs and feeds it through the genome to get a decision
	 */
	public void think(List<Double> inputs);

	/**
	 * Calculates the current fitness of the AI
	 *
	 * @return the fitness
	 */
	public double calculateFitness();

}

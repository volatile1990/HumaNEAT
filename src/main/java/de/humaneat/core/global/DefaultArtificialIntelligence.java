package de.humaneat.core.global;

import java.util.List;

/**
 * @author MannoR
 *
 */
public interface DefaultArtificialIntelligence {

	/**
	 * Returns all inputs for the next feed forward
	 */
	public List<List<Double>> getInputs();

	/**
	 * Takes its inputs and feeds it through the genome to get a decision
	 */
	public void think();

	/**
	 * Gets the output of the last feed to convert it into actions
	 */
	public void takeAction(List<Double> output);

	/**
	 * Calculates the current fitness of the AI
	 *
	 * @return the fitness
	 */
	public double calculateFitness();

}

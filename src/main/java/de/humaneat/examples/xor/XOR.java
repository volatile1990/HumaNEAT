package de.humaneat.examples.xor;

import de.humaneat.core.neat.population.Population;
import de.humaneat.core.neat.training.PopulationTrainer;

/**
 * @author MannoR
 *
 */
public class XOR {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Create new population
		Population population = new Population(XorAI.class);

		// Assign population to a population trainer
		PopulationTrainer trainer = new PopulationTrainer(population, true);

		// Train until desired fitness is reached
		trainer.trainToFitness(16);

	}

}

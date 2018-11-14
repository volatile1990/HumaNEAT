package de.humaneat.examples.innovationdays;

import de.humaneat.core.neat.population.Population;
import de.humaneat.core.neat.training.PopulationTrainer;

/**
 * @author muellermak
 *
 */
public class TicketClassification {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Create population
		Population population = new Population(TicketClassificationAI.class);

		// Train population
		PopulationTrainer trainer = new PopulationTrainer(population, false);
		trainer.train();

	}
}

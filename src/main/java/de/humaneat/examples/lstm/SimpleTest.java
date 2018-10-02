package de.humaneat.examples.lstm;

import de.humaneat.core.lstm.population.LstmPopulation;
import de.humaneat.core.lstm.training.LstmPopulationTrainer;

/**
 * @author muellermak
 *
 */
public class SimpleTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		LstmPopulation population = new LstmPopulation(SimpleLstmAi.class);

		LstmPopulationTrainer trainer = new LstmPopulationTrainer(population);

		trainer.trainToFitness(100);

	}
}

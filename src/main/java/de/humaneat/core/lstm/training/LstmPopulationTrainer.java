package de.humaneat.core.lstm.training;

import de.humaneat.core.global.components.node.NodeGeneType;
import de.humaneat.core.lstm.ArtificialLstmIntelligence;
import de.humaneat.core.lstm.population.LstmPopulation;
import de.humaneat.core.neat.Property;

/**
 * @author muellermak
 *
 */
public class LstmPopulationTrainer {

	private LstmPopulation population;
	private int generations;

	public ArtificialLstmIntelligence trainedAi;

	/**
	 * @param population
	 */
	public LstmPopulationTrainer(LstmPopulation population) {
		this.population = population;
		generations = (int) Property.GENERATIONS.getValue();
	}

	/**
	 * 
	 */
	public ArtificialLstmIntelligence train() {
		return trainToFitness(Double.MAX_VALUE);
	}

	/**
	 * @param fitness
	 * @return
	 */
	public ArtificialLstmIntelligence trainToFitness(double fitness) {

		for (int i = 0; i < generations; ++i) {

			for (ArtificialLstmIntelligence ai : population.artificialIntelligences) {

				ai.doAiLogic();

				// Check if current ai matches maximum fitness to reach
				if (checkFitness(ai, fitness)) {
					trainedAi = ai;
					print(trainedAi);
					return trainedAi;
				}
			}

			System.out.println("Generation: " + population.currentGeneration + " ## Best fitness: " + population.fittestAI.brain.unadjustedFitness);

			// Evolve a new generation
			population.evolve();
		}

		// Set the fittest ai as the trainedAi after all generations; May not reach fitness goal
		trainedAi = population.fittestAI;
		print(trainedAi);

		return trainedAi;
	}

	/**
	 * @param ai
	 */
	private void print(ArtificialLstmIntelligence ai) {

		System.out.println();
		System.out.println("################# FINISHED #################");
		System.out.println("# Training was done in " + population.currentGeneration + " generations");
		System.out.println("# Trained AI fitness		: " + ai.brain.unadjustedFitness);
		System.out.println("# Trained AI hidden nodes	: " + ai.brain.getNodesByType(NodeGeneType.HIDDEN).size());
		System.out.println("# Trained AI connections	: " + ai.brain.connections.size());
	}

	/**
	 * @param fitness
	 */
	private boolean checkFitness(ArtificialLstmIntelligence ai, double fitness) {

		if (ai.brain.unadjustedFitness >= fitness) {
			return true;
		}
		return false;
	}

}

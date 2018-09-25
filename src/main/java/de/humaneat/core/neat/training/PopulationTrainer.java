package de.humaneat.core.neat.training;

import de.humaneat.core.global.components.node.NodeGeneType;
import de.humaneat.core.neat.ArtificialIntelligence;
import de.humaneat.core.neat.Property;
import de.humaneat.core.neat.population.Population;
import de.humaneat.graphics.GenomeViewer;

/**
 * @author muellermak
 *
 */
public class PopulationTrainer {

	private Population population;
	private int generations;

	private boolean displayEvolution;

	public ArtificialIntelligence trainedAi;

	/**
	 * @param population
	 */
	public PopulationTrainer(Population population, boolean visualizeEvolution) {
		this.population = population;
		generations = (int) Property.GENERATIONS.getValue();
		displayEvolution = visualizeEvolution;
	}

	/**
	 * 
	 */
	public ArtificialIntelligence train() {
		return trainToFitness(Double.MAX_VALUE);
	}

	/**
	 * @param fitness
	 * @return
	 */
	public ArtificialIntelligence trainToFitness(double fitness) {

		// Launch 3D application
		GenomeViewer genomeViewer = null;
		if (displayEvolution) {
			genomeViewer = new GenomeViewer(population.artificialIntelligences.get(0).brain);
			genomeViewer.start();
			waitForGenomeViewer(genomeViewer);
		}

		for (int i = 0; i < generations; ++i) {

			for (ArtificialIntelligence ai : population.artificialIntelligences) {

				ai.think();

				// Check if current ai matches maximum fitness to reach
				if (checkFitness(ai, fitness)) {
					trainedAi = ai;
					print(trainedAi);
					return trainedAi;
				}
			}

			System.out.println("Generation: " + population.currentGeneration + " ## Best fitness: " + population.fittestAI.brain.unadjustedFitness);

			// Update 3D model
			if (displayEvolution) {
				genomeViewer.genome = population.fittestAI.brain;
			}

			// Evolve a new generation
			population.evolve();
		}

		// Set the fittest ai as the trainedAi after all generations; May not reach fitness goal
		trainedAi = population.fittestAI;
		print(trainedAi);

		return trainedAi;
	}

	private void waitForGenomeViewer(GenomeViewer genomeViewer) {
		while (!genomeViewer.initialized) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param ai
	 */
	private void print(ArtificialIntelligence ai) {

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
	private boolean checkFitness(ArtificialIntelligence ai, double fitness) {

		if (ai.brain.unadjustedFitness >= fitness) {
			return true;
		}
		return false;
	}

}

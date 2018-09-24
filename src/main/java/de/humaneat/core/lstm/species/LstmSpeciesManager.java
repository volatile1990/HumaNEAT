package de.humaneat.core.lstm.species;

import java.util.Collections;
import java.util.Random;

import de.humaneat.core.lstm.ArtificialLstmIntelligence;
import de.humaneat.core.lstm.genome.LstmGenomeFitnessComparator;

/**
 * @author muellermak
 *
 */
public class LstmSpeciesManager {

	private LstmSpecies species;

	/**
	 * @param species
	 */
	public LstmSpeciesManager(LstmSpecies species) {
		this.species = species;
	}

	/**
	 * Sets the average fitness for the species by dividing the sum of all members fitness by the amount of members
	 */
	public void setAverageFitness() {

		double sum = 0;
		for (ArtificialLstmIntelligence ai : species.members) {
			sum += ai.brain.fitness;
		}

		species.averageFitness = sum / species.members.size();
	}

	/**
	 * @param adjustedFitness
	 */
	public void addAjustedFitness(double adjustedFitness) {
		species.totalAdjustedFitness += adjustedFitness;
	}

	/**
	 * @param random
	 */
	public void reset(Random random) {

		// Assign new mascot before clearing
		int newMascotIndex = random.nextInt(species.members.size());
		species.champion = species.members.get(newMascotIndex);

		species.members.clear();
		species.totalAdjustedFitness = 0f;
	}

	/**
	 * Sorts all members of the species by its fitness
	 */
	public void sortMembersByFitness() {

		Collections.sort(species.members, new LstmGenomeFitnessComparator());

		ArtificialLstmIntelligence fittestAi = species.members.get(0);
		double currentBestFitness = fittestAi.brain.fitness;
		if (currentBestFitness > species.bestFitness) {
			species.bestFitness = currentBestFitness;
			species.staleness = 0;
			species.champion = fittestAi;
		} else {

			// Species didn't improve after last generation
			++species.staleness;
		}
	}

	/**
	 *
	 */
	public void fitnessSharing() {
		for (ArtificialLstmIntelligence ai : species.members) {
			ai.brain.unadjustedFitness = ai.brain.fitness;
			ai.brain.fitness /= species.members.size();
		}
	}
}

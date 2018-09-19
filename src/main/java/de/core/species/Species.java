package de.core.species;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.core.ArtificialIntelligence;
import de.core.Property;
import de.core.genes.ConnectionHistory;
import de.core.genome.Genome;
import de.core.genome.GenomeFitnessComparator;

/**
 * @author muellermak
 *
 */
public class Species {

	// Randomly choose as species representator for adding new genomes
	public ArtificialIntelligence champion;

	public List<ArtificialIntelligence> members;

	public int staleness;
	public float bestFitness;

	public float totalAdjustedFitness;
	public float averageFitness;

	/**
	 * @param mascot
	 */
	public Species(ArtificialIntelligence mascot) {
		this.champion = mascot;
		this.members = new LinkedList<>();
		this.members.add(mascot);

		this.staleness = 0;
		this.totalAdjustedFitness = 0f;
		this.averageFitness = 0f;
		this.bestFitness = 0f;
	}

	/**
	 *
	 */
	public void setAverageFitness() {

		float sum = 0;
		for (ArtificialIntelligence ai : this.members) {
			sum += ai.brain.fitness;
		}

		this.averageFitness = sum / this.members.size();
	}

	/**
	 * @param adjustedFitness
	 */
	public void addAjustedFitness(float adjustedFitness) {
		this.totalAdjustedFitness += adjustedFitness;
	}

	/**
	 * @param random
	 */
	public void reset(Random random) {

		// Assign new mascot before clearing
		int newMascotIndex = random.nextInt(this.members.size());
		this.champion = this.members.get(newMascotIndex);

		this.members.clear();
		this.totalAdjustedFitness = 0f;
	}

	/**
	 * Sorts all members of the species by its fitness
	 */
	public void sortMembersByFitness() {

		Collections.sort(this.members, new GenomeFitnessComparator());

		ArtificialIntelligence fittestAi = this.members.get(0);
		float currentBestFitness = fittestAi.brain.fitness;
		if (currentBestFitness > this.bestFitness) {
			this.bestFitness = currentBestFitness;
			this.staleness = 0;
			this.champion = fittestAi;
		} else {

			// Species didn't improve after last generation
			++this.staleness;
		}
	}

	/**
	 *
	 */
	public void cull() {

		if (this.members.size() <= 2) {
			return;
		}

		double keepPercentage = 1 - Property.REMOVE_BAD_SPECIES_MEMBERS_PERCENT.getValue();

		int removeEndIndex = (int) Math.floor(this.members.size() * keepPercentage);
		List<ArtificialIntelligence> toRemove = new ArrayList<>();
		for (int i = this.members.size() - 1; i >= removeEndIndex; --i) {
			toRemove.add(this.members.get(i));
		}

		for (ArtificialIntelligence ai : toRemove) {
			this.members.remove(ai);
		}
	}

	/**
	 * @param innovationHistory
	 * @return
	 */
	public Genome makeBaby(Map<Integer, List<ConnectionHistory>> innovationHistory) {

		Random random = new SecureRandom();

		// 25% chance to directly add the selected player without crossover
		Genome baby = null;
		if (random.nextFloat() <= Property.ADD_GENOME_WITHOUT_CROSSOVER_RATE.getValue()) {
			baby = this.selectGenomeForReproduction().copy();
			baby.mutate(innovationHistory);
		} else {

			// Get random parents
			Genome mum = this.selectGenomeForReproduction();
			Genome dad = this.selectGenomeForReproduction();

			// Fitter parent gotta do the work
			if (mum.fitness < dad.fitness) {
				baby = Genome.crossover(dad, mum);
			} else {
				baby = Genome.crossover(mum, dad);
			}
			baby.mutate(innovationHistory);
		}

		// Tschernobyl taking over

		return baby;
	}

	/**
	 * @return
	 */
	public Genome selectGenomeForReproduction() {

		float fitnessSum = 0;
		for (ArtificialIntelligence ai : this.members) {
			fitnessSum += ai.brain.fitness;
		}

		Random random = new SecureRandom();
		float randomFitness = random.nextFloat() * fitnessSum;

		float countingSum = 0;
		for (ArtificialIntelligence ai : this.members) {

			countingSum += ai.brain.fitness;
			if (countingSum > randomFitness) {
				return ai.brain;
			}
		}

		// Will never happen
		return this.members.get(0).brain;
	}

	/**
	 *
	 */
	public void fitnessSharing() {
		for (ArtificialIntelligence ai : this.members) {
			ai.brain.unadjustedFitness = ai.brain.fitness;
			ai.brain.fitness /= this.members.size();
		}
	}
}

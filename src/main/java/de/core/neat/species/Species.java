package de.core.neat.species;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.core.neat.ArtificialIntelligence;
import de.core.neat.Property;
import de.core.neat.genes.ConnectionHistory;
import de.core.neat.genome.Genome;
import de.core.neat.genome.GenomeFitnessComparator;
import de.core.neat.genome.GenomeHatchery;

/**
 * @author muellermak
 *
 */
public class Species {

	// Randomly choose as species representator for adding new genomes
	public ArtificialIntelligence champion;

	public List<ArtificialIntelligence> members;

	public int staleness;
	public double bestFitness;

	public double totalAdjustedFitness;
	public double averageFitness;

	/**
	 * @param mascot
	 */
	public Species(ArtificialIntelligence mascot) {
		champion = mascot;
		members = new LinkedList<>();
		members.add(mascot);

		staleness = 0;
		totalAdjustedFitness = 0f;
		averageFitness = 0f;
		bestFitness = 0f;
	}

	/**
	 *
	 */
	public void setAverageFitness() {

		double sum = 0;
		for (ArtificialIntelligence ai : members) {
			sum += ai.brain.fitness;
		}

		averageFitness = sum / members.size();
	}

	/**
	 * @param adjustedFitness
	 */
	public void addAjustedFitness(double adjustedFitness) {
		totalAdjustedFitness += adjustedFitness;
	}

	/**
	 * @param random
	 */
	public void reset(Random random) {

		// Assign new mascot before clearing
		int newMascotIndex = random.nextInt(members.size());
		champion = members.get(newMascotIndex);

		members.clear();
		totalAdjustedFitness = 0f;
	}

	/**
	 * Sorts all members of the species by its fitness
	 */
	public void sortMembersByFitness() {

		Collections.sort(members, new GenomeFitnessComparator());

		ArtificialIntelligence fittestAi = members.get(0);
		double currentBestFitness = fittestAi.brain.fitness;
		if (currentBestFitness > bestFitness) {
			bestFitness = currentBestFitness;
			staleness = 0;
			champion = fittestAi;
		} else {

			// Species didn't improve after last generation
			++staleness;
		}
	}

	/**
	 *
	 */
	public void cull() {

		if (members.size() <= 2) {
			return;
		}

		double keepPercentage = 1 - Property.REMOVE_BAD_SPECIES_MEMBERS_PERCENT.getValue();

		int removeEndIndex = (int) Math.floor(members.size() * keepPercentage);
		List<ArtificialIntelligence> toRemove = new ArrayList<>();
		for (int i = members.size() - 1; i >= removeEndIndex; --i) {
			toRemove.add(members.get(i));
		}

		for (ArtificialIntelligence ai : toRemove) {
			members.remove(ai);
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
			baby = selectGenomeForReproduction().copy();
			baby.getMutator().mutate(innovationHistory);
		} else {

			// Get random parents
			Genome mum = selectGenomeForReproduction();
			Genome dad = selectGenomeForReproduction();

			// Fitter parent gotta do the work
			if (mum.fitness < dad.fitness) {
				baby = GenomeHatchery.crossover(dad, mum);
			} else {
				baby = GenomeHatchery.crossover(mum, dad);
			}
			baby.getMutator().mutate(innovationHistory);
		}

		// Tschernobyl taking over

		return baby;
	}

	/**
	 * @return
	 */
	public Genome selectGenomeForReproduction() {

		double fitnessSum = 0;
		for (ArtificialIntelligence ai : members) {
			fitnessSum += ai.brain.fitness;
		}

		Random random = new SecureRandom();
		double randomFitness = random.nextFloat() * fitnessSum;

		double countingSum = 0;
		for (ArtificialIntelligence ai : members) {

			countingSum += ai.brain.fitness;
			if (countingSum > randomFitness) {
				return ai.brain;
			}
		}

		// Will never happen
		return members.get(0).brain;
	}

	/**
	 *
	 */
	public void fitnessSharing() {
		for (ArtificialIntelligence ai : members) {
			ai.brain.unadjustedFitness = ai.brain.fitness;
			ai.brain.fitness /= members.size();
		}
	}
}

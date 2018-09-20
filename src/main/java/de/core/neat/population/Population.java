package de.core.neat.population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.core.neat.ArtificialIntelligence;
import de.core.neat.Property;
import de.core.neat.genes.ConnectionHistory;
import de.core.neat.genome.GenomeSpeciation;
import de.core.neat.species.Species;
import de.core.neat.species.SpeciesFitnessComparator;

/**
 * @author muellermak
 *
 */
public class Population {

	// The population per generation (best genomes from last generation + X to fit number)
	private int populationSize;

	// All genomes to evaluate
	public List<ArtificialIntelligence> artificialIntelligences;

	// Genomes of the new generation after evaluation
	private List<ArtificialIntelligence> nextGenerationAis;

	// All species
	public List<Species> species;

	public ArtificialIntelligence fittestAI;

	public Map<Integer, List<ConnectionHistory>> innovationHistory;

	public double highestAchievedFitness;

	public Population(List<ArtificialIntelligence> population) {

		populationSize = population.size();

		// Initialize AIs
		artificialIntelligences = new ArrayList<>(populationSize);
		for (int i = 0; i < populationSize; ++i) {
			artificialIntelligences.add(population.get(i));
		}
		fittestAI = artificialIntelligences.get(0);

		nextGenerationAis = new ArrayList<>();
		species = new ArrayList<>();
		innovationHistory = new HashMap<>();
	}

	/**
	 *
	 */
	public void evolve() {

		// Place genomes into species
		assignGenomeToSpecies();

		// Evaluate genomes and assign fitness
		calculateFitness();

		// Sort all species by fitness
		sortSpecies();

		// Clean species that are bad, didn't improve or are empty
		cullSpecies();

		// Breed the rest of the genomes; fill up list to populationSize
		breedNewGeneration();

		if (fittestAI.brain.unadjustedFitness == 0) {
			System.out.println();
		}

		// Use next generation as current
		artificialIntelligences = nextGenerationAis;
		nextGenerationAis = new ArrayList<>();

		for (ArtificialIntelligence ai : artificialIntelligences) {
			ai.brain.getLinker().generateNetwork();
		}
	}

	/**
	 * Assigns all genomes to a species
	 */
	private void assignGenomeToSpecies() {

		// Clear all species before filling again
		for (Species species : this.species) {
			species.members.clear();
		}

		for (ArtificialIntelligence ai : artificialIntelligences) {

			boolean foundSpecies = false;
			for (Species species : this.species) {

				GenomeSpeciation speciation = new GenomeSpeciation(ai.brain, species.champion.brain);
				if (speciation.compatibilityDistance(Property.C1.getValue(), Property.C2.getValue(), Property.C3.getValue()) < Property.DT.getValue()) {
					species.members.add(ai);
					foundSpecies = true;
					break;
				}
			}

			// Create new species with current genome as mascot
			if (!foundSpecies) {
				species.add(new Species(ai));
			}
		}
	}

	/**
	 * Removes the bottom half of each species and empty species
	 */
	private void cullSpecies() {

		double averageSum = getAverageFitnessSum();

		Iterator<Species> iterator = species.iterator();
		while (iterator.hasNext()) {

			Species species = iterator.next();

			// Remove bottom half of each species
			species.cull();
			species.fitnessSharing();
			species.setAverageFitness();

			// Remove empty and stale species
			if (species.members.isEmpty() || species.staleness > 14) {
				iterator.remove();
				continue;
			}

			// Remove species that won't be allowed to bring one child into the next generation
			if (species.averageFitness / averageSum * populationSize < 1) {
				iterator.remove();
			}

		}

	}

	/**
	 *
	 */
	private void calculateFitness() {

		for (ArtificialIntelligence ai : artificialIntelligences) {
			double fitness = ai.calculateFitness();
			ai.clearData();

			ai.brain.fitness = fitness;
			ai.brain.unadjustedFitness = fitness;

			if (ai.brain.fitness > fittestAI.brain.fitness) {
				fittestAI = ai;
			}
		}
	}

	/**
	 * Sorts all species and members within the species by their fitness
	 */
	private void sortSpecies() {

		// Sort species members
		for (Species species : this.species) {
			if (species.members.size() > 0) {
				species.sortMembersByFitness();
			}
		}

		// Sort species by fitness of its fittest member
		Collections.sort(species, new SpeciesFitnessComparator());
	}

	/**
	 *
	 */
	private void breedNewGeneration() {

		// Put best genomes from each species into next generation
		populateNextGenerationGenomes();

		double averageFitnessSum = getAverageFitnessSum();

		boolean populatingDone = false;
		for (Species species : this.species) {

			int allowedChildrenCount = (int) (species.averageFitness / averageFitnessSum * populationSize);
//			System.out.println("CHILDREN ALLOWED : " + allowedChildrenCount + " ## AVG SPECIES FITNESS: " + species.averageFitness + " ## AVG FITNES SUM : " + averageFitnessSum);

			for (int i = 0; i < allowedChildrenCount; ++i) {
				ArtificialIntelligence ai = artificialIntelligences.get(0).getNewInstance(species.makeBaby(innovationHistory));
				nextGenerationAis.add(ai);

				if (nextGenerationAis.size() >= populationSize) {
					populatingDone = true;
					break;
				}
			}

			if (populatingDone) {
				break;
			}
		}

		while (nextGenerationAis.size() < populationSize) {
			ArtificialIntelligence ai = artificialIntelligences.get(0).getNewInstance(species.get(0).makeBaby(innovationHistory));
			nextGenerationAis.add(ai);
		}

		for (ArtificialIntelligence ai : nextGenerationAis) {
			ai.brain.getLinker().generateNetwork();
		}

	}

	/**
	 *
	 */
	private void populateNextGenerationGenomes() {

		for (Species species : this.species) {
			if (species.members.size() > 5) {
				nextGenerationAis.add(species.members.get(0));
			}
		}
	}

	/**
	 * @return
	 */
	private double getAverageFitnessSum() {

		double averageSum = 0;
		for (Species species : this.species) {
			averageSum += species.averageFitness;
		}
		return averageSum;
	}
}

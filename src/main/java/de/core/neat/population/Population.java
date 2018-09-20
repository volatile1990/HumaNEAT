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
	private List<Species> species;

	private ArtificialIntelligence fittestAI;

	public Map<Integer, List<ConnectionHistory>> innovationHistory;

	public double highestAchievedFitness;

	public Population(List<ArtificialIntelligence> population) {

		this.populationSize = population.size();

		// Initialize AIs
		this.artificialIntelligences = new ArrayList<>(this.populationSize);
		for (int i = 0; i < this.populationSize; ++i) {
			this.artificialIntelligences.add(population.get(i));
		}
		this.fittestAI = this.artificialIntelligences.get(0);

		this.nextGenerationAis = new ArrayList<>();
		this.species = new ArrayList<>();
		this.innovationHistory = new HashMap<>();
	}

	/**
	 *
	 */
	public void evolve() {

		// Place genomes into species
		this.assignGenomeToSpecies();

		// Evaluate genomes and assign fitness
		this.calculateFitness();

		// Sort all species by fitness
		this.sortSpecies();

		// Clean species that are bad, didn't improve or are empty
		this.cullSpecies();

		// Breed the rest of the genomes; fill up list to populationSize
		this.breedNewGeneration();

		if (this.fittestAI.brain.unadjustedFitness == 0) {
			System.out.println();
		}

		// Use next generation as current
		this.artificialIntelligences = this.nextGenerationAis;
		this.nextGenerationAis = new ArrayList<>();

		for (ArtificialIntelligence ai : this.artificialIntelligences) {
			ai.brain.generateNetwork();
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

		for (ArtificialIntelligence ai : this.artificialIntelligences) {

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
				this.species.add(new Species(ai));
			}
		}
	}

	/**
	 * Removes the bottom half of each species and empty species
	 */
	private void cullSpecies() {

		double averageSum = this.getAverageFitnessSum();

		Iterator<Species> iterator = this.species.iterator();
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
			if (species.averageFitness / averageSum * this.populationSize < 1) {
				iterator.remove();
			}

		}

	}

	/**
	 *
	 */
	private void calculateFitness() {

		for (ArtificialIntelligence ai : this.artificialIntelligences) {
			double fitness = ai.calculateFitness();
			ai.brain.fitness = fitness;
			ai.brain.unadjustedFitness = fitness;

			if (ai.brain.fitness > this.fittestAI.brain.fitness) {
				this.fittestAI = ai;
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
		Collections.sort(this.species, new SpeciesFitnessComparator());
	}

	/**
	 *
	 */
	private void breedNewGeneration() {

		// Put best genomes from each species into next generation
		this.populateNextGenerationGenomes();

		double averageFitnessSum = this.getAverageFitnessSum();

		boolean populatingDone = false;
		for (Species species : this.species) {

			int allowedChildrenCount = (int) (species.averageFitness / averageFitnessSum * this.populationSize);
//			System.out.println("CHILDREN ALLOWED : " + allowedChildrenCount + " ## AVG SPECIES FITNESS: " + species.averageFitness + " ## AVG FITNES SUM : " + averageFitnessSum);

			for (int i = 0; i < allowedChildrenCount; ++i) {
				ArtificialIntelligence ai = this.artificialIntelligences.get(0).getNewInstance(species.makeBaby(this.innovationHistory));
				this.nextGenerationAis.add(ai);

				if (this.nextGenerationAis.size() >= this.populationSize) {
					populatingDone = true;
					break;
				}
			}

			if (populatingDone) {
				break;
			}
		}

		while (this.nextGenerationAis.size() < this.populationSize) {
			ArtificialIntelligence ai = this.artificialIntelligences.get(0).getNewInstance(this.species.get(0).makeBaby(this.innovationHistory));
			this.nextGenerationAis.add(ai);
		}

		for (ArtificialIntelligence ai : this.nextGenerationAis) {
			ai.brain.generateNetwork();
		}

	}

	/**
	 *
	 */
	private void populateNextGenerationGenomes() {

		for (Species species : this.species) {
			if (species.members.size() > 5) {
				this.nextGenerationAis.add(species.members.get(0));
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

	/**
	 * @return the fittestGenome
	 */
	public ArtificialIntelligence getFittestAI() {
		return this.fittestAI;
	}

	/**
	 * @return the species
	 */
	public List<Species> getSpecies() {
		return this.species;
	}
}

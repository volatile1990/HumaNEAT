package de.core.neat.population;

import java.util.Collections;
import java.util.Iterator;

import de.core.neat.ArtificialIntelligence;
import de.core.neat.Property;
import de.core.neat.genome.GenomeSpeciation;
import de.core.neat.species.Species;
import de.core.neat.species.SpeciesFitnessComparator;

/**
 * @author MannoR
 *         Provides core functionality for a population
 */
public class PopulationCore {

	private Population population;

	/**
	 *
	 */
	public PopulationCore(Population population) {
		this.population = population;
	}

	/**
	 * Assigns all genomes to a species
	 */
	public void assignGenomeToSpecies() {

		// Clear all species before filling again
		for (Species species : population.species) {
			species.members.clear();
		}

		for (ArtificialIntelligence ai : population.artificialIntelligences) {

			boolean foundSpecies = false;
			for (Species species : population.species) {

				GenomeSpeciation speciation = new GenomeSpeciation(ai.brain, species.champion.brain);
				if (speciation.compatibilityDistance(Property.C1.getValue(), Property.C2.getValue(), Property.C3.getValue()) < Property.DT.getValue()) {
					species.members.add(ai);
					foundSpecies = true;
					break;
				}
			}

			// Create new species with current genome as mascot
			if (!foundSpecies) {
				population.species.add(new Species(ai));
			}
		}
	}

	/**
	 * Removes the bottom half of each species and empty species
	 */
	public void cullSpecies() {

		Iterator<Species> iterator = population.species.iterator();
		while (iterator.hasNext()) {

			Species species = iterator.next();

			// Remove bottom half of each species
			species.getHatchery().cull();
			species.getManager().fitnessSharing();
			species.getManager().setAverageFitness();

			// Remove empty and stale species
			if (species.members.isEmpty() || species.staleness > 14) {
				iterator.remove();
				continue;
			}

			// Remove species that won't be allowed to bring one child into the next generation
			if (allowedChildrenForNextGeneration(species) < 1) {
				iterator.remove();
			}

		}

	}

	/**
	 * Calls the calculateFitness function of every ai in the population
	 */
	public void calculateFitness() {

		for (ArtificialIntelligence ai : population.artificialIntelligences) {
			double fitness = ai.calculateFitness();
			ai.clearData();

			ai.brain.fitness = fitness;
			ai.brain.unadjustedFitness = fitness;

			if (ai.brain.fitness > population.fittestAI.brain.fitness) {
				population.fittestAI = ai;
			}
		}
	}

	/**
	 * Sorts all species and members within the species by their fitness
	 */
	public void sortSpecies() {

		// Sort species members
		for (Species species : population.species) {
			if (species.members.size() > 0) {
				species.getManager().sortMembersByFitness();
			}
		}

		// Sort species by fitness of its fittest member
		Collections.sort(population.species, new SpeciesFitnessComparator());
	}

	/**
	 * Populates the next generation of genomes by the current species champions and new babies
	 */
	public void breedNewGeneration() {

		// Put best genomes from each species into next generation
		populateNextGenerationGenomes();

		boolean populatingDone = false;
		for (Species species : population.species) {

			int allowedChildrenCount = allowedChildrenForNextGeneration(species);
//			System.out.println("CHILDREN ALLOWED : " + allowedChildrenCount + " ## AVG SPECIES FITNESS: " + species.averageFitness + " ## AVG FITNES SUM : " + averageFitnessSum);

			for (int i = 0; i < allowedChildrenCount; ++i) {
				ArtificialIntelligence ai = population.artificialIntelligences.get(0).getNewInstance(species.getHatchery().makeBaby(population.innovationHistory));
				population.nextGenerationAis.add(ai);

				if (population.nextGenerationAis.size() >= population.populationSize) {
					populatingDone = true;
					break;
				}
			}

			if (populatingDone) {
				break;
			}
		}

		while (population.nextGenerationAis.size() < population.populationSize) {
			ArtificialIntelligence ai = population.artificialIntelligences.get(0).getNewInstance(population.species.get(0).getHatchery().makeBaby(population.innovationHistory));
			population.nextGenerationAis.add(ai);
		}

		for (ArtificialIntelligence ai : population.nextGenerationAis) {
			ai.brain.getLinker().generateNetwork();
		}

	}

	/**
	 * Populate the current species champions for the next generation
	 */
	public void populateNextGenerationGenomes() {

		for (Species species : population.species) {
			if (species.members.size() > 5) {
				population.nextGenerationAis.add(species.members.get(0));
			}
		}
	}

	/**
	 * @return how many children this species is allowed to produce for the next generation
	 */
	public int allowedChildrenForNextGeneration(Species species) {

		double averageFitnessSum = getAverageFitnessSum();
		return (int) (species.averageFitness / averageFitnessSum * population.populationSize);
	}

	/**
	 * @return the sum of the average fitness of all species
	 */
	private double getAverageFitnessSum() {

		double averageSum = 0;
		for (Species species : population.species) {
			averageSum += species.averageFitness;
		}
		return averageSum;
	}

}

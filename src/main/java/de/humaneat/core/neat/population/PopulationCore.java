package de.humaneat.core.neat.population;

import java.util.Collections;
import java.util.Iterator;

import de.humaneat.core.global.Random;
import de.humaneat.core.neat.ArtificialIntelligence;
import de.humaneat.core.neat.Property;
import de.humaneat.core.neat.genome.Genome;
import de.humaneat.core.neat.genome.GenomeSpeciation;
import de.humaneat.core.neat.species.Species;
import de.humaneat.core.neat.species.SpeciesFitnessComparator;

/**
 * @author muellermak
 *
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

			// Remove species which won't be allowed to bring even a single child into the next generation
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

		// End if every species is dead
		if (population.species.size() <= 0) {
			throw new RuntimeException("All species died. Retry with different parameters.");
		}

		// Put best genomes from each species into next generation
		populateNextGenerationGenomes();

		boolean populatingDone = false;
		for (Species species : population.species) {

			int allowedChildrenCount = allowedChildrenForNextGeneration(species);

			for (int i = 0; i < allowedChildrenCount; ++i) {

				// Get child from current species
				Genome babyBrain = species.getHatchery().makeBaby(population.innovationHistory);

				// Create new ai using that child genome as brain
				ArtificialIntelligence ai = generateNewAi();
				ai.brain = babyBrain;
				population.nextGenerationAis.add(ai);

				// Until desired population size is reached
				if (population.nextGenerationAis.size() >= population.populationSize) {
					populatingDone = true;
					break;
				}
			}

			if (populatingDone) {
				break;
			}
		}

		// May happen if the species were not allowed to produce enough children for the next generation
		while (population.nextGenerationAis.size() < population.populationSize) {

			// Create new baby using a random species
			Species randomSpecies = Random.random(population.species);
			Genome babyBrain = randomSpecies.getHatchery().makeBaby(population.innovationHistory);

			// Generate a new AI using that genome
			ArtificialIntelligence ai = generateNewAi();
			ai.brain = babyBrain;

			// Populate for next generation
			population.nextGenerationAis.add(ai);
		}

		for (ArtificialIntelligence ai : population.nextGenerationAis) {
			ai.brain.getLinker().generateNetwork();
		}

	}

	/**
	 * Generations a new ai using the type the population is initialized with
	 */
	private ArtificialIntelligence generateNewAi() {

		try {
			return population.aiClazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Couldn't create new ArtificialIntelligences for the population");
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

package de.humaneat.core.lstm.population;

import java.util.Collections;
import java.util.Iterator;

import de.humaneat.core.global.Random;
import de.humaneat.core.lstm.ArtificialLstmIntelligence;
import de.humaneat.core.lstm.genome.LstmGenome;
import de.humaneat.core.lstm.genome.LstmGenomeSpeciation;
import de.humaneat.core.lstm.species.LstmSpecies;
import de.humaneat.core.lstm.species.LstmSpeciesFitnessComparator;
import de.humaneat.core.neat.Property;

/**
 * @author muellermak
 *
 */
public class LstmPopulationCore {

	private LstmPopulation population;

	/**
	 *
	 */
	public LstmPopulationCore(LstmPopulation population) {
		this.population = population;
	}

	/**
	 * Assigns all genomes to a species
	 */
	public void assignGenomeToSpecies() {

		// Clear all species before filling again
		for (LstmSpecies species : population.species) {
			species.members.clear();
		}

		for (ArtificialLstmIntelligence ai : population.artificialIntelligences) {

			boolean foundSpecies = false;
			for (LstmSpecies species : population.species) {

				LstmGenomeSpeciation speciation = new LstmGenomeSpeciation(ai.brain, species.champion.brain);
				if (speciation.compatibilityDistance(Property.C1.getValue(), Property.C2.getValue(), Property.C3.getValue()) < Property.DT.getValue()) {
					species.members.add(ai);
					foundSpecies = true;
					break;
				}
			}

			// Create new species with current genome as mascot
			if (!foundSpecies) {
				population.species.add(new LstmSpecies(ai));
			}
		}
	}

	/**
	 * Removes the bottom half of each species and empty species
	 */
	public void cullSpecies() {

		Iterator<LstmSpecies> iterator = population.species.iterator();
		while (iterator.hasNext()) {

			LstmSpecies species = iterator.next();

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

		for (ArtificialLstmIntelligence ai : population.artificialIntelligences) {
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
		for (LstmSpecies species : population.species) {
			if (species.members.size() > 0) {
				species.getManager().sortMembersByFitness();
			}
		}

		// Sort species by fitness of its fittest member
		Collections.sort(population.species, new LstmSpeciesFitnessComparator());
	}

	/**
	 * Populates the next generation of genomes by the current species champions and new babies
	 */
	public void breedNewGeneration() {

		// Put best genomes from each species into next generation
		populateNextGenerationGenomes();

		boolean populatingDone = false;
		for (LstmSpecies species : population.species) {

			int allowedChildrenCount = allowedChildrenForNextGeneration(species);

			for (int i = 0; i < allowedChildrenCount; ++i) {

				// Get child from current species
				LstmGenome babyBrain = species.getHatchery().makeBaby(population.innovationHistory);

				// Create new ai using that child genome as brain
				ArtificialLstmIntelligence ai = generateNewAi();
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

		while (population.nextGenerationAis.size() < population.populationSize) {

			// Create new baby using a random species
			LstmSpecies randomSpecies = Random.random(population.species);
			LstmGenome babyBrain = randomSpecies.getHatchery().makeBaby(population.innovationHistory);

			// Generate a new AI using that genome
			ArtificialLstmIntelligence ai = generateNewAi();
			ai.brain = babyBrain;

			// Populate for next generation
			population.nextGenerationAis.add(ai);
		}

		for (ArtificialLstmIntelligence ai : population.nextGenerationAis) {
			ai.brain.getLinker().generateNetwork();
		}

	}

	/**
	 * Generations a new ai using the type the population is initialized with
	 */
	private ArtificialLstmIntelligence generateNewAi() {

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

		for (LstmSpecies species : population.species) {
			if (species.members.size() > 5) {
				population.nextGenerationAis.add(species.members.get(0));
			}
		}
	}

	/**
	 * @return how many children this species is allowed to produce for the next generation
	 */
	public int allowedChildrenForNextGeneration(LstmSpecies species) {

		double averageFitnessSum = getAverageFitnessSum();
		return (int) (species.averageFitness / averageFitnessSum * population.populationSize);
	}

	/**
	 * @return the sum of the average fitness of all species
	 */
	private double getAverageFitnessSum() {

		double averageSum = 0;
		for (LstmSpecies species : population.species) {
			averageSum += species.averageFitness;
		}
		return averageSum;
	}
}

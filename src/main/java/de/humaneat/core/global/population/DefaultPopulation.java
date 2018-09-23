package de.humaneat.core.global.population;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.humaneat.core.neat.ArtificialIntelligence;
import de.humaneat.core.neat.genes.connection.ConnectionHistory;
import de.humaneat.core.neat.species.Species;

public class DefaultPopulation {

	public int currentGeneration;

	/**
	 * The population and its size
	 */
	public List<ArtificialIntelligence> artificialIntelligences;
	public int populationSize;

	/**
	 * Genomes of the new generation after evaluation
	 */
	public List<ArtificialIntelligence> nextGenerationAis;

	/**
	 * All species in this population
	 */
	public List<Species> species;

	/**
	 * Fitness tracker
	 */
	public ArtificialIntelligence fittestAI;
	public double highestAchievedFitness;

	/**
	 * The history of all connection innovations of this population; used for reproduction
	 */
	public Map<Integer, List<ConnectionHistory>> innovationHistory;

	/**
	 * Provides core functionality for the population
	 */
	private DefaultPopulationCore core;

	/**
	 * The initial population
	 */
	public List<ArtificialIntelligence> startPopulation;

	/**
	 * @param population
	 */
	public DefaultPopulation(List<ArtificialIntelligence> population) {

		if (population.isEmpty()) {
			throw new RuntimeException("Population must not be empty");
		}

		initialize(population);
	}

	/**
	 * @param population
	 */
	private void initialize(List<ArtificialIntelligence> population) {
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

		core = new DefaultPopulationCore(this);
		currentGeneration = 1;

		startPopulation = population;
	}

	/**
	 * @param population
	 */
	public void restart(List<ArtificialIntelligence> population) {
		initialize(population);
	}

	/**
	 * Evolves a new generation of this population
	 */
	public void evolve() {

		// Place genomes into species
		core.assignGenomeToSpecies();

		// Evaluate genomes and assign fitness
		core.calculateFitness();

		// Sort all species by fitness
		core.sortSpecies();

		// Clean species that are bad, didn't improve or are empty
		core.cullSpecies();

		// Breed the rest of the genomes; fill up list to populationSize
		core.breedNewGeneration();

		// Use next generation as current
		artificialIntelligences = nextGenerationAis;
		nextGenerationAis = new ArrayList<>();

		// Link all brains of the new generation
		for (ArtificialIntelligence ai : artificialIntelligences) {
			ai.brain.getLinker().generateNetwork();
		}

		// Back in my days, ...
		++currentGeneration;
	}

}

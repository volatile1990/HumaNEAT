package de.humaneat.core.lstm.population;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.humaneat.core.global.components.connection.ConnectionHistory;
import de.humaneat.core.lstm.ArtificialLstmIntelligence;
import de.humaneat.core.lstm.species.LstmSpecies;

/**
 * @author muellermak
 *
 */
public class LstmPopulation {

	public int currentGeneration;

	/**
	 * The population and its size
	 */
	public List<ArtificialLstmIntelligence> artificialIntelligences;
	public int populationSize;

	/**
	 * Genomes of the new generation after evaluation
	 */
	public List<ArtificialLstmIntelligence> nextGenerationAis;

	/**
	 * All species in this population
	 */
	public List<LstmSpecies> species;

	/**
	 * Fitness tracker
	 */
	public ArtificialLstmIntelligence fittestAI;
	public double highestAchievedFitness;

	/**
	 * The history of all connection innovations of this population; used for reproduction
	 */
	public Map<Integer, List<ConnectionHistory>> innovationHistory;

	/**
	 * Provides core functionality for the population
	 */
	private LstmPopulationCore core;

	/**
	 * The initial population
	 */
	public List<ArtificialLstmIntelligence> startPopulation;

	/**
	 * @param population
	 */
	public LstmPopulation(List<ArtificialLstmIntelligence> population) {

		if (population.isEmpty()) {
			throw new RuntimeException("Population must not be empty");
		}

		initialize(population);
	}

	/**
	 * @param population
	 */
	private void initialize(List<ArtificialLstmIntelligence> population) {
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

		core = new LstmPopulationCore(this);
		currentGeneration = 1;

		startPopulation = population;
	}

	/**
	 * @param population
	 */
	public void restart(List<ArtificialLstmIntelligence> population) {
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
		for (ArtificialLstmIntelligence ai : artificialIntelligences) {
			ai.brain.getLinker().generateNetwork();
		}

		// Back in my days, ...
		++currentGeneration;
	}

}

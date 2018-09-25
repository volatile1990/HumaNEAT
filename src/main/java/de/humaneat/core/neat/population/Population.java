package de.humaneat.core.neat.population;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.humaneat.core.global.components.connection.ConnectionHistory;
import de.humaneat.core.neat.ArtificialIntelligence;
import de.humaneat.core.neat.Property;
import de.humaneat.core.neat.species.Species;

/**
 * @author muellermak
 *
 */
public class Population {

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

	/**
	 * The history of all connection innovations of this population; used for reproduction
	 */
	public Map<Integer, List<ConnectionHistory>> innovationHistory;

	/**
	 * Provides core functionality for the population
	 */
	private PopulationCore core;

	/**
	 * The Class of the ArtificialIntelligence implementation
	 */
	Class<? extends ArtificialIntelligence> aiClazz;

	/**
	 * @param population
	 */
	public Population(Class<? extends ArtificialIntelligence> aiClazz) {

		if (aiClazz == null) {
			throw new RuntimeException("Network needs an AI - extending - Class to operate with");
		}
		this.aiClazz = aiClazz;

		// Initialize population
		try {
			initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param population
	 * @throws Exception
	 */
	private void initialize() throws Exception {

		populationSize = (int) Property.POPULATION_SIZE.getValue();

		// Create AIs for this population
		artificialIntelligences = new ArrayList<>();
		for (int i = 0; i < populationSize; ++i) {

			// Create new AI
			ArtificialIntelligence ai = aiClazz.newInstance();

			// Add to population
			artificialIntelligences.add(ai);
		}

		// Select random element as first fittest ai
		fittestAI = artificialIntelligences.get(0);

		nextGenerationAis = new ArrayList<>();
		species = new ArrayList<>();
		innovationHistory = new HashMap<>();

		core = new PopulationCore(this);
		currentGeneration = 1;

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

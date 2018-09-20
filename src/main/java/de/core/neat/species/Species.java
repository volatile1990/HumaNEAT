package de.core.neat.species;

import java.util.LinkedList;
import java.util.List;

import de.core.neat.ArtificialIntelligence;

/**
 * @author muellermak
 *
 */
public class Species {

	/**
	 * Best performing member of this species; used for comparison
	 */
	public ArtificialIntelligence champion;

	/**
	 * All members of this species
	 */
	public List<ArtificialIntelligence> members;

	/**
	 * Fitness tracker
	 */
	public double bestFitness;
	public double totalAdjustedFitness;
	public double averageFitness;

	/**
	 * For how many generations this species did not improve in fitness
	 */
	public int staleness;

	/**
	 * Species related operators
	 */
	private SpeciesManager manager;
	private SpeciesHatchery hatchery;

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

		manager = new SpeciesManager(this);
		hatchery = new SpeciesHatchery(this);
	}

	/**
	 * @return
	 */
	public SpeciesManager getManager() {
		return manager;
	}

	/**
	 * @return
	 */
	public SpeciesHatchery getHatchery() {
		return hatchery;
	}
}

package de.humaneat.core.lstm.species;

import java.util.LinkedList;
import java.util.List;

import de.humaneat.core.lstm.ArtificialLstmIntelligence;

/**
 * @author muellermak
 *
 */
public class LstmSpecies {

	/**
	 * Best performing member of this species; used for comparison
	 */
	public ArtificialLstmIntelligence champion;

	/**
	 * All members of this species
	 */
	public List<ArtificialLstmIntelligence> members;

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
	private LstmSpeciesManager manager;
	private LstmSpeciesHatchery hatchery;

	/**
	 * @param mascot
	 */
	public LstmSpecies(ArtificialLstmIntelligence mascot) {

		champion = mascot;
		members = new LinkedList<>();
		members.add(mascot);

		staleness = 0;
		totalAdjustedFitness = 0f;
		averageFitness = 0f;
		bestFitness = 0f;

		manager = new LstmSpeciesManager(this);
		hatchery = new LstmSpeciesHatchery(this);
	}

	/**
	 * @return
	 */
	public LstmSpeciesManager getManager() {
		return manager;
	}

	/**
	 * @return
	 */
	public LstmSpeciesHatchery getHatchery() {
		return hatchery;
	}
}

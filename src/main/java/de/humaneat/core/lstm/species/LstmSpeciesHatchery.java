package de.humaneat.core.lstm.species;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.humaneat.core.global.components.connection.ConnectionHistory;
import de.humaneat.core.lstm.ArtificialLstmIntelligence;
import de.humaneat.core.lstm.genome.LstmGenome;
import de.humaneat.core.neat.Property;

/**
 * @author muellermak
 *
 */
public class LstmSpeciesHatchery {

	private LstmSpecies species;

	/**
	 * @param species
	 */
	public LstmSpeciesHatchery(LstmSpecies species) {
		this.species = species;
	}

	/**
	 *
	 */
	public void cull() {

		if (species.members.size() <= 2) {
			return;
		}

		double keepPercentage = 1 - Property.REMOVE_BAD_SPECIES_MEMBERS_PERCENT.getValue();

		int removeEndIndex = (int) Math.floor(species.members.size() * keepPercentage);
		List<ArtificialLstmIntelligence> toRemove = new ArrayList<>();
		for (int i = species.members.size() - 1; i >= removeEndIndex; --i) {
			toRemove.add(species.members.get(i));
		}

		for (ArtificialLstmIntelligence ai : toRemove) {
			species.members.remove(ai);
		}
	}

	/**
	 * @param innovationHistory
	 * @return
	 */
	public LstmGenome makeBaby(Map<Integer, List<ConnectionHistory>> innovationHistory) {

		Random random = new SecureRandom();

		// 25% chance to directly add the selected player without crossover
		LstmGenome baby = null;
		if (random.nextFloat() <= Property.ADD_GENOME_WITHOUT_CROSSOVER_RATE.getValue()) {
			baby = selectGenomeForReproduction().copy();
			baby.getMutator().mutate(innovationHistory);
		} else {

			// Get random parents
			LstmGenome mum = selectGenomeForReproduction();
			LstmGenome dad = selectGenomeForReproduction();

			// Fitter parent gotta do the work
			if (dad.fitness > mum.fitness) {
				baby = dad.getHatchery().crossover(mum);
			} else {
				baby = mum.getHatchery().crossover(dad);
			}
			baby.getMutator().mutate(innovationHistory);
		}

		// Tschernobyl taking over

		return baby;
	}

	/**
	 * @return
	 */
	public LstmGenome selectGenomeForReproduction() {

		double fitnessSum = 0;
		for (ArtificialLstmIntelligence ai : species.members) {
			fitnessSum += ai.brain.fitness;
		}

		Random random = new SecureRandom();
		double randomFitness = random.nextFloat() * fitnessSum;

		double countingSum = 0;
		for (ArtificialLstmIntelligence ai : species.members) {

			countingSum += ai.brain.fitness;
			if (countingSum > randomFitness) {
				return ai.brain;
			}
		}

		// Will never happen
		return species.members.get(0).brain;
	}

}

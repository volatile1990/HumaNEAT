package de.humaneat.core.neat.species;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.humaneat.core.neat.ArtificialIntelligence;
import de.humaneat.core.neat.Property;
import de.humaneat.core.neat.genes.connection.ConnectionHistory;
import de.humaneat.core.neat.genome.Genome;
import de.humaneat.core.neat.genome.GenomeHatchery;

/**
 * @author MannoR
 *
 */
public class SpeciesHatchery {

	private Species species;

	/**
	 * @param species
	 */
	public SpeciesHatchery(Species species) {
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
		List<ArtificialIntelligence> toRemove = new ArrayList<>();
		for (int i = species.members.size() - 1; i >= removeEndIndex; --i) {
			toRemove.add(species.members.get(i));
		}

		for (ArtificialIntelligence ai : toRemove) {
			species.members.remove(ai);
		}
	}

	/**
	 * @param innovationHistory
	 * @return
	 */
	public Genome makeBaby(Map<Integer, List<ConnectionHistory>> innovationHistory) {

		Random random = new SecureRandom();

		// 25% chance to directly add the selected player without crossover
		Genome baby = null;
		if (random.nextFloat() <= Property.ADD_GENOME_WITHOUT_CROSSOVER_RATE.getValue()) {
			baby = selectGenomeForReproduction().copy();
			baby.getMutator().mutate(innovationHistory);
		} else {

			// Get random parents
			Genome mum = selectGenomeForReproduction();
			Genome dad = selectGenomeForReproduction();

			// Fitter parent gotta do the work
			if (mum.fitness < dad.fitness) {
				baby = GenomeHatchery.crossover(dad, mum);
			} else {
				baby = GenomeHatchery.crossover(mum, dad);
			}
			baby.getMutator().mutate(innovationHistory);
		}

		// Tschernobyl taking over

		return baby;
	}

	/**
	 * @return
	 */
	public Genome selectGenomeForReproduction() {

		double fitnessSum = 0;
		for (ArtificialIntelligence ai : species.members) {
			fitnessSum += ai.brain.fitness;
		}

		Random random = new SecureRandom();
		double randomFitness = random.nextFloat() * fitnessSum;

		double countingSum = 0;
		for (ArtificialIntelligence ai : species.members) {

			countingSum += ai.brain.fitness;
			if (countingSum > randomFitness) {
				return ai.brain;
			}
		}

		// Will never happen
		return species.members.get(0).brain;
	}

}
